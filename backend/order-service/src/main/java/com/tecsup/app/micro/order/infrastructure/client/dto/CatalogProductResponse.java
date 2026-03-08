package com.tecsup.app.micro.order.infrastructure.client.dto;

import java.math.BigDecimal;

public record CatalogProductResponse(
        Long id,
        Long restaurantId,
        String name,
        String description,
        BigDecimal price,
        boolean available
) {
}
