package com.tecsup.app.micro.order.infrastructure.messaging.event;

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
