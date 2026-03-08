package com.tecsup.app.micro.order.infrastructure.messaging.event;

import com.tecsup.app.micro.order.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
        Long orderId,
        String customerAuthUserId,
        Long restaurantId,
        OrderStatus status,
        BigDecimal totalAmount,
        Instant createdAt,
        List<OrderCreatedItemEvent> items
) {
    public record OrderCreatedItemEvent(
            Long productId,
            String productName,
            BigDecimal unitPrice,
            Integer quantity,
            BigDecimal subtotal
    ) {
    }
}
