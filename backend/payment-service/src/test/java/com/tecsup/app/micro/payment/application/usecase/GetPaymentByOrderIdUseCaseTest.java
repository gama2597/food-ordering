package com.tecsup.app.micro.payment.application.usecase;

import com.tecsup.app.micro.payment.domain.exception.PaymentDomainException;
import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.domain.port.PaymentRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPaymentByOrderIdUseCaseTest {

    @Mock
    private PaymentRepositoryPort paymentRepository;

    @InjectMocks
    private GetPaymentByOrderIdUseCase useCase;

    @Test
    void execute_shouldReturnPaymentWhenExists() {
        Payment payment = Payment.builder().id(1L).orderId(11L).build();
        when(paymentRepository.findByOrderId(11L)).thenReturn(Optional.of(payment));

        Payment result = useCase.execute(11L);

        assertEquals(1L, result.getId());
    }

    @Test
    void execute_shouldFailWhenOrderIdIsNull() {
        PaymentDomainException ex = assertThrows(PaymentDomainException.class, () -> useCase.execute(null));
        assertEquals("El orderId es obligatorio", ex.getMessage());
    }
}
