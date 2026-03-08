package com.tecsup.app.micro.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
public class Order {

    private final Long id;
    private final String customerAuthUserId;
    private final Long restaurantId;
    private final OrderStatus status;
    private final BigDecimal totalAmount;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final List<OrderItem> items;
}
