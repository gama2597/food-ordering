package com.tecsup.app.micro.delivery.application.usecase.command;

import lombok.Builder;

@Builder
public record PaymentApprovedCommand(
        Long orderId,
        String customerAuthUserId
) {
}
