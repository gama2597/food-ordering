package com.tecsup.app.micro.payment.application.usecase;

import com.tecsup.app.micro.payment.application.usecase.command.OrderCreatedCommand;
import com.tecsup.app.micro.payment.domain.exception.PaymentDomainException;
import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.domain.model.PaymentStatus;
import com.tecsup.app.micro.payment.domain.port.PaymentEventPublisherPort;
import com.tecsup.app.micro.payment.domain.port.PaymentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProcessOrderCreatedUseCase {

    private static final BigDecimal APPROVAL_LIMIT = new BigDecimal("250.00");
    private static final String DEFAULT_CURRENCY = "PEN";

    private final PaymentRepositoryPort paymentRepository;
    private final PaymentEventPublisherPort paymentEventPublisher;

    public Payment execute(OrderCreatedCommand command) {
        validate(command);

        var existingPayment = paymentRepository.findByOrderId(command.orderId());
        if (existingPayment.isPresent()) {
            return existingPayment.get();
        }

        Instant now = Instant.now();
        PaymentStatus status = resolveStatus(command.amount());
        String reason = status == PaymentStatus.APPROVED
                ? "Pago aprobado"
                : "Pago rechazado por simulacion";

        Payment paymentToSave = Payment.builder()
                .id(null)
                .orderId(command.orderId())
                .customerAuthUserId(command.customerAuthUserId())
                .amount(command.amount())
                .currency(command.currency() == null || command.currency().isBlank() ? DEFAULT_CURRENCY : command.currency())
                .status(status)
                .reason(reason)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Payment saved = paymentRepository.save(paymentToSave);
        if (saved.getStatus() == PaymentStatus.APPROVED) {
            paymentEventPublisher.publishApproved(saved);
        } else {
            paymentEventPublisher.publishRejected(saved);
        }
        return saved;
    }

    private void validate(OrderCreatedCommand command) {
        if (command == null) {
            throw new PaymentDomainException("La solicitud de pago es obligatoria");
        }
        if (command.orderId() == null) {
            throw new PaymentDomainException("El orderId es obligatorio");
        }
        if (command.customerAuthUserId() == null || command.customerAuthUserId().isBlank()) {
            throw new PaymentDomainException("El customerAuthUserId es obligatorio");
        }
        if (command.amount() == null || command.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentDomainException("El monto del pedido es invalido");
        }
    }

    private PaymentStatus resolveStatus(BigDecimal amount) {
        return amount.compareTo(APPROVAL_LIMIT) <= 0
                ? PaymentStatus.APPROVED
                : PaymentStatus.REJECTED;
    }
}
