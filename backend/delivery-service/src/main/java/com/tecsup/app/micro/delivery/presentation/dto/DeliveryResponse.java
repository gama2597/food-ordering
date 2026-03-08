package com.tecsup.app.micro.delivery.presentation.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class DeliveryResponse {

    private Long id;
    private Long orderId;
    private String customerAuthUserId;
    private String status;
    private Instant assignedAt;
    private Instant startedAt;
    private Instant deliveredAt;
    private Instant createdAt;
    private Instant updatedAt;
}
