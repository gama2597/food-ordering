package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.model.OrderStatus;
import com.tecsup.app.micro.order.domain.port.OrderEventPublisherPort;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RequestMyOrderPaymentUseCase {

    private final OrderRepositoryPort orderRepository;
    private final OrderEventPublisherPort orderEventPublisher;

    public Order execute(String customerAuthUserId, Long orderId) {
        if (customerAuthUserId == null || customerAuthUserId.isBlank()) {
            throw new OrderDomainException("El usuario autenticado es obligatorio");
        }
        if (orderId == null) {
            throw new OrderDomainException("El orderId es obligatorio");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderDomainException("No existe el pedido con ID " + orderId));

        if (!customerAuthUserId.equals(order.getCustomerAuthUserId())) {
            throw new OrderDomainException("No tienes permisos para operar este pedido");
        }
        if (order.getStatus() == OrderStatus.PAID) {
            throw new OrderDomainException("El pedido ya fue pagado");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderDomainException("No se puede pagar un pedido cancelado");
        }
        if (order.getStatus() == OrderStatus.PAYMENT_PENDING) {
            return order;
        }

        Order pendingOrder = orderRepository.save(order.toBuilder()
                .status(OrderStatus.PAYMENT_PENDING)
                .updatedAt(Instant.now())
                .build());

        orderEventPublisher.publishPaymentRequested(pendingOrder);
        return pendingOrder;
    }
}
