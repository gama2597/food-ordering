package com.tecsup.app.micro.catalog.domain.model;

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
public class Product {
    private final Long id;
    private final Long restaurantId;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean available;
}
