package com.tecsup.app.micro.order.infrastructure.client.dto;

public record CatalogRestaurantResponse(
        Long id,
        String name,
        String description,
        String address,
        boolean active
) {
}
