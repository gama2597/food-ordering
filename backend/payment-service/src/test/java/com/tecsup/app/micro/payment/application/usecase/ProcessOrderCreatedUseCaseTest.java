package com.tecsup.app.micro.payment.application.usecase;

import com.tecsup.app.micro.payment.application.usecase.command.OrderCreatedCommand;
import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.domain.model.PaymentStatus;
import com.tecsup.app.micro.payment.domain.port.PaymentEventPublisherPort;
import com.tecsup.app.micro.payment.domain.port.PaymentRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessOrderCreatedUseCaseTest {

    @Mock
    private PaymentRepositoryPort paymentRepository;

    @Mock
    private PaymentEventPublisherPort paymentEventPublisher;

    @InjectMocks
    private ProcessOrderCreatedUseCase useCase;

    @Test
    void execute_shouldApprovePaymentForLowAmount() {
        OrderCreatedCommand command = OrderCreatedCommand.builder()
                .orderId(1L)
                .customerAuthUserId("sub-1")
                .amount(new BigDecimal("100.00"))
                .currency("PEN")
                .occurredAt(Instant.now())
                .build();

        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                ((Payment) invocation.getArgument(0)).toBuilder().id(99L).status(PaymentStatus.APPROVED).build());

        Payment result = useCase.execute(command);

        assertEquals(PaymentStatus.APPROVED, result.getStatus());
        verify(paymentEventPublisher).publishApproved(any(Payment.class));
    }

    @Test
    void execute_shouldRejectPaymentForHighAmount() {
        OrderCreatedCommand command = OrderCreatedCommand.builder()
                .orderId(2L)
                .customerAuthUserId("sub-1")
                .amount(new BigDecimal("400.00"))
                .currency("PEN")
                .occurredAt(Instant.now())
                .build();

        when(paymentRepository.findByOrderId(2L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                ((Payment) invocation.getArgument(0)).toBuilder().id(100L).status(PaymentStatus.REJECTED).build());

        Payment result = useCase.execute(command);

        assertEquals(PaymentStatus.REJECTED, result.getStatus());
        verify(paymentEventPublisher).publishRejected(any(Payment.class));
    }

    @Test
    void execute_shouldReturnExistingPaymentWithoutPublishing() {
        Payment existing = Payment.builder().id(10L).orderId(3L).status(PaymentStatus.APPROVED).build();
        when(paymentRepository.findByOrderId(3L)).thenReturn(Optional.of(existing));

        Payment result = useCase.execute(OrderCreatedCommand.builder()
                .orderId(3L)
                .customerAuthUserId("sub-1")
                .amount(new BigDecimal("50.00"))
                .currency("PEN")
                .occurredAt(Instant.now())
                .build());

        assertEquals(10L, result.getId());
        verify(paymentRepository, never()).save(any(Payment.class));
        verify(paymentEventPublisher, never()).publishApproved(any(Payment.class));
        verify(paymentEventPublisher, never()).publishRejected(any(Payment.class));
    }
}
