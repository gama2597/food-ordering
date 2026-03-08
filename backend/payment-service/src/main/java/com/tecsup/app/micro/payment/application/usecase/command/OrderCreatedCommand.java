package com.tecsup.app.micro.payment.application.usecase.command;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record OrderCreatedCommand(
        Long orderId,
        String customerAuthUserId,
        BigDecimal amount,
        String currency,
        Instant occurredAt
) {
}
