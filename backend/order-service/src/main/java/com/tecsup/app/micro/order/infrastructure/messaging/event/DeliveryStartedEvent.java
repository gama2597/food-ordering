package com.tecsup.app.micro.order.infrastructure.messaging.event;

public record DeliveryStartedEvent(
        Long orderId
) {
}
