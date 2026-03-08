package com.tecsup.app.micro.delivery.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Delivery {

    private final Long id;
    private final Long orderId;
    private final String customerAuthUserId;
    private final DeliveryStatus status;
    private final Instant assignedAt;
    private final Instant startedAt;
    private final Instant deliveredAt;
    private final Instant createdAt;
    private final Instant updatedAt;
}
