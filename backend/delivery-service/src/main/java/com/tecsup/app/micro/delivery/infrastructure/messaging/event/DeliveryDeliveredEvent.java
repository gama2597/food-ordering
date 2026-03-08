package com.tecsup.app.micro.delivery.infrastructure.messaging.event;

import java.time.Instant;

public record DeliveryDeliveredEvent(
        String eventId,
        int eventVersion,
        String eventType,
        Instant occurredAt,
        Long orderId,
        Long deliveryId,
        String status
) {
}
