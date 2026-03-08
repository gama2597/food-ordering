package com.tecsup.app.micro.payment.application.usecase;

import com.tecsup.app.micro.payment.domain.exception.PaymentDomainException;
import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.domain.port.PaymentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPaymentByOrderIdUseCase {

    private final PaymentRepositoryPort paymentRepository;

    public Payment execute(Long orderId) {
        if (orderId == null) {
            throw new PaymentDomainException("El orderId es obligatorio");
        }
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentDomainException("Pago no encontrado para el pedido " + orderId));
    }
}
