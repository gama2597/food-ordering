package com.tecsup.app.micro.payment.infrastructure.messaging.mapper;

import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.infrastructure.messaging.event.PaymentApprovedEvent;
import com.tecsup.app.micro.payment.infrastructure.messaging.event.PaymentRejectedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class PaymentEventMapper {

    public PaymentApprovedEvent toApprovedEvent(Payment payment) {
        return new PaymentApprovedEvent(
                UUID.randomUUID().toString(),
                1,
                "PAYMENT_APPROVED",
                Instant.now(),
                payment.getOrderId(),
                payment.getId(),
                payment.getCustomerAuthUserId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus().name(),
                payment.getReason()
        );
    }

    public PaymentRejectedEvent toRejectedEvent(Payment payment) {
        return new PaymentRejectedEvent(
                UUID.randomUUID().toString(),
                1,
                "PAYMENT_REJECTED",
                Instant.now(),
                payment.getOrderId(),
                payment.getId(),
                payment.getCustomerAuthUserId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus().name(),
                payment.getReason()
        );
    }
}
