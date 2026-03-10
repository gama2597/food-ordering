package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.CatalogProductSnapshot;
import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.model.OrderItem;
import com.tecsup.app.micro.order.domain.model.OrderStatus;
import com.tecsup.app.micro.order.domain.port.CatalogQueryPort;
import com.tecsup.app.micro.order.domain.port.OrderEventPublisherPort;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Caso de Uso central: Crea una orden validando precios e inventario.
 */
@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final CatalogQueryPort catalogQueryPort;
    private final OrderEventPublisherPort orderEventPublisher;

    public Order execute(String customerAuthUserId, Order order) {
        if (customerAuthUserId == null || customerAuthUserId.isBlank()) {
            throw new OrderDomainException("El usuario autenticado es obligatorio");
        }
        if (order == null) {
            throw new OrderDomainException("El pedido es obligatorio");
        }
        if (order.getRestaurantId() == null) {
            throw new OrderDomainException("El restaurante del pedido es obligatorio");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new OrderDomainException("El pedido debe tener al menos un item");
        }

        // 2. Comunicación síncrona: Preguntamos al Catalog-Service si el restaurante existe y está abierto
        catalogQueryPort.validateRestaurantIsActive(order.getRestaurantId());

        // 3. Obtenemos los precios reales del catálogo (para que el usuario no mande precios falsos)
        var catalogProductsById = catalogQueryPort.getProductsByRestaurant(order.getRestaurantId());

        // 4. Mapeamos y calculamos subtotales basados en los precios reales del Catálogo
        List<OrderItem> normalizedItems = order.getItems().stream()
                .map(item -> normalizeAndValidateItem(item, catalogProductsById))
                .toList();

        BigDecimal totalAmount = normalizedItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order orderToSave = order.toBuilder()
                .id(null)
                .customerAuthUserId(customerAuthUserId)
                .status(OrderStatus.CREATED)
                .totalAmount(totalAmount)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .items(normalizedItems)
                .build();

        Order savedOrder = orderRepository.save(orderToSave);

        // 7. Evento Asíncrono: Publicamos en Kafka que la orden fue creada
        orderEventPublisher.publishOrderCreated(savedOrder);
        return savedOrder;
    }

    /**
     * Valida que el producto exista, esté disponible y le asigna el precio REAL del catálogo.
     */
    private OrderItem normalizeAndValidateItem(OrderItem item, java.util.Map<Long, CatalogProductSnapshot> catalogProductsById) {
        if (item.getProductId() == null) {
            throw new OrderDomainException("Cada item debe tener productId");
        }
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new OrderDomainException("La cantidad de cada item debe ser mayor a cero");
        }

        CatalogProductSnapshot catalogProduct = catalogProductsById.get(item.getProductId());
        if (catalogProduct == null) {
            throw new OrderDomainException("El producto con ID " + item.getProductId() + " no existe en el restaurante");
        }
        if (!catalogProduct.isAvailable()) {
            throw new OrderDomainException("El producto con ID " + item.getProductId() + " no esta disponible");
        }
        if (catalogProduct.getPrice() == null || catalogProduct.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderDomainException("El precio del producto con ID " + item.getProductId() + " es invalido");
        }

        BigDecimal subtotal = catalogProduct.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return item.toBuilder()
                .id(null)
                .productName(catalogProduct.getName())
                .unitPrice(catalogProduct.getPrice())
                .subtotal(subtotal)
                .build();
    }
}
