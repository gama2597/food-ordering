package com.tecsup.app.micro.order.domain.model;

public enum OrderStatus {
    CREATED,
    PAYMENT_PENDING,
    PAID,
    ASSIGNED,
    DELIVERING,
    DELIVERED,
    CANCELLED
}
