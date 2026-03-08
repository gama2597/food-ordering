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
@Builder
@AllArgsConstructor
public class CatalogProductSnapshot {

    private final Long productId;
    private final String name;
    private final BigDecimal price;
    private final boolean available;
}
