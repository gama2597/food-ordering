package com.tecsup.app.micro.order.infrastructure.messaging.event;

public record PaymentApprovedEvent(
        Long orderId
) {
}
