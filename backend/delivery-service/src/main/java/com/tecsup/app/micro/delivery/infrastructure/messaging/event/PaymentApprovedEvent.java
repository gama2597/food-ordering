package com.tecsup.app.micro.delivery.infrastructure.messaging.event;

public record PaymentApprovedEvent(
        Long orderId,
        String customerAuthUserId
) {
}
