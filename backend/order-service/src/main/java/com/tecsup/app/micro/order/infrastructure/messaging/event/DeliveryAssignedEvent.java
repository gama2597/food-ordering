package com.tecsup.app.micro.order.infrastructure.messaging.event;

public record DeliveryAssignedEvent(
        Long orderId
) {
}
