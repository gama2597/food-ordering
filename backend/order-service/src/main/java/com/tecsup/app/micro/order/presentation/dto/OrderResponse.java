package com.tecsup.app.micro.order.presentation.dto;

import com.tecsup.app.micro.order.domain.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;
    private String customerAuthUserId;
    private Long restaurantId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private Instant createdAt;
    private Instant updatedAt;
    private List<OrderItemResponse> items;
}
