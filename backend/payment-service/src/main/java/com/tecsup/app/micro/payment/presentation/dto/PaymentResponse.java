package com.tecsup.app.micro.payment.presentation.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private String customerAuthUserId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String reason;
    private Instant createdAt;
    private Instant updatedAt;
}
