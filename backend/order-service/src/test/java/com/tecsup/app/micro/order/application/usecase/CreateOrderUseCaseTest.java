package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.CatalogProductSnapshot;
import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.model.OrderItem;
import com.tecsup.app.micro.order.domain.port.CatalogQueryPort;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private CatalogQueryPort catalogQueryPort;

    @InjectMocks
    private CreateOrderUseCase useCase;

    @Test
    void execute_shouldCreateOrderWithComputedTotal() {
        OrderItem item = OrderItem.builder()
                .productId(10L)
                .productName("Pizza")
                .unitPrice(new BigDecimal("20.00"))
                .quantity(2)
                .build();

        Order input = Order.builder()
                .restaurantId(1L)
                .items(List.of(item))
                .build();

        doNothing().when(catalogQueryPort).validateRestaurantIsActive(1L);
        when(catalogQueryPort.getProductsByRestaurant(1L)).thenReturn(java.util.Map.of(
                10L,
                CatalogProductSnapshot.builder()
                        .productId(10L)
                        .name("Pizza")
                        .price(new BigDecimal("20.00"))
                        .available(true)
                        .build()
        ));

        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order toSave = inv.getArgument(0);
            return toSave.toBuilder().id(100L).build();
        });

        Order result = useCase.execute("sub-1", input);

        assertEquals(100L, result.getId());
        assertEquals(new BigDecimal("40.00"), result.getTotalAmount());
        assertTrue(result.getItems().getFirst().getSubtotal().compareTo(new BigDecimal("40.00")) == 0);
    }

    @Test
    void execute_shouldFailWhenItemsAreMissing() {
        Order input = Order.builder().restaurantId(1L).items(List.of()).build();

        OrderDomainException ex = assertThrows(OrderDomainException.class, () -> useCase.execute("sub-1", input));
        assertEquals("El pedido debe tener al menos un item", ex.getMessage());
    }

    @Test
    void execute_shouldFailWhenProductDoesNotBelongToRestaurant() {
        OrderItem item = OrderItem.builder()
                .productId(99L)
                .productName("Unknown")
                .unitPrice(new BigDecimal("20.00"))
                .quantity(1)
                .build();

        Order input = Order.builder()
                .restaurantId(1L)
                .items(List.of(item))
                .build();

        doNothing().when(catalogQueryPort).validateRestaurantIsActive(1L);
        when(catalogQueryPort.getProductsByRestaurant(1L)).thenReturn(java.util.Map.of());

        OrderDomainException ex = assertThrows(OrderDomainException.class, () -> useCase.execute("sub-1", input));
        assertEquals("El producto con ID 99 no existe en el restaurante", ex.getMessage());
    }
}
