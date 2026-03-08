package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.model.OrderStatus;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplyDeliveryProgressUseCaseTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @InjectMocks
    private ApplyDeliveryProgressUseCase useCase;

    @Test
    void applyAssigned_shouldUpdateWhenOrderIsPaid() {
        Order order = Order.builder().id(1L).status(OrderStatus.PAID).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        useCase.applyAssigned(1L);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void applyDelivered_shouldAllowMonotonicJump() {
        Order order = Order.builder().id(1L).status(OrderStatus.ASSIGNED).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        useCase.applyDelivered(1L);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void applyStarted_shouldIgnoreCancelledOrder() {
        Order order = Order.builder().id(1L).status(OrderStatus.CANCELLED).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        useCase.applyStarted(1L);

        verify(orderRepository, never()).save(any(Order.class));
    }
}
