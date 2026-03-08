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
class ApplyPaymentResultUseCaseTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @InjectMocks
    private ApplyPaymentResultUseCase useCase;

    @Test
    void applyApproved_shouldUpdateToPaidWhenPending() {
        Order order = Order.builder().id(1L).status(OrderStatus.PAYMENT_PENDING).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        useCase.applyApproved(1L);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void applyApproved_shouldIgnoreWhenAlreadyPaid() {
        Order order = Order.builder().id(1L).status(OrderStatus.PAID).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        useCase.applyApproved(1L);

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void applyRejected_shouldUpdateToCancelledWhenPending() {
        Order order = Order.builder().id(1L).status(OrderStatus.PAYMENT_PENDING).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        useCase.applyRejected(1L);

        verify(orderRepository).save(any(Order.class));
    }
}
