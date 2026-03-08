package com.tecsup.app.micro.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
public class OrderItem {

    private final Long id;
    private final Long productId;
    private final String productName;
    private final BigDecimal unitPrice;
    private final Integer quantity;
    private final BigDecimal subtotal;
}
