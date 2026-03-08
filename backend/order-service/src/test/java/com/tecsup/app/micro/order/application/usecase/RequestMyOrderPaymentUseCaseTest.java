package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.model.OrderStatus;
import com.tecsup.app.micro.order.domain.port.OrderEventPublisherPort;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestMyOrderPaymentUseCaseTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private OrderEventPublisherPort orderEventPublisher;

    @InjectMocks
    private RequestMyOrderPaymentUseCase useCase;

    @Test
    void execute_shouldSetPaymentPendingAndPublishEvent() {
        Order order = Order.builder().id(1L).customerAuthUserId("sub-1").status(OrderStatus.CREATED).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = useCase.execute("sub-1", 1L);

        assertEquals(OrderStatus.PAYMENT_PENDING, result.getStatus());
        verify(orderRepository).save(any(Order.class));
        verify(orderEventPublisher).publishPaymentRequested(any(Order.class));
    }

    @Test
    void execute_shouldFailWhenOwnerDoesNotMatch() {
        Order order = Order.builder().id(1L).customerAuthUserId("sub-2").status(OrderStatus.CREATED).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDomainException ex = assertThrows(OrderDomainException.class, () -> useCase.execute("sub-1", 1L));

        assertEquals("No tienes permisos para operar este pedido", ex.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderEventPublisher, never()).publishPaymentRequested(any(Order.class));
    }

    @Test
    void execute_shouldReturnSameOrderWhenAlreadyPending() {
        Order order = Order.builder().id(1L).customerAuthUserId("sub-1").status(OrderStatus.PAYMENT_PENDING).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = useCase.execute("sub-1", 1L);

        assertEquals(OrderStatus.PAYMENT_PENDING, result.getStatus());
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderEventPublisher, never()).publishPaymentRequested(any(Order.class));
    }
}
