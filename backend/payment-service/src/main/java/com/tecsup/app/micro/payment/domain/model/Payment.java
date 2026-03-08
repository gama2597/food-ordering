package com.tecsup.app.micro.payment.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Payment {

    private final Long id;
    private final Long orderId;
    private final String customerAuthUserId;
    private final BigDecimal amount;
    private final String currency;
    private final PaymentStatus status;
    private final String reason;
    private final Instant createdAt;
    private final Instant updatedAt;
}
