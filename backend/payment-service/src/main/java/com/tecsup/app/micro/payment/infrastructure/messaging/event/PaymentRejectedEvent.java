package com.tecsup.app.micro.payment.infrastructure.messaging.event;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentRejectedEvent(
        String eventId,
        int eventVersion,
        String eventType,
        Instant occurredAt,
        Long orderId,
        Long paymentId,
        String customerAuthUserId,
        BigDecimal amount,
        String currency,
        String status,
        String reason
) {
}
