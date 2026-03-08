package com.tecsup.app.micro.payment.infrastructure.messaging.event;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentRequestedEvent(
        Long orderId,
        String customerAuthUserId,
        BigDecimal amount,
        String currency,
        Instant requestedAt
) {
}
