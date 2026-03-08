package com.tecsup.app.micro.order.infrastructure.client;

import com.tecsup.app.micro.order.infrastructure.client.dto.CatalogProductResponse;
import com.tecsup.app.micro.order.infrastructure.client.dto.CatalogRestaurantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "catalog-service", url = "${clients.catalog-service.url}")
public interface CatalogServiceClient {

    @GetMapping("/api/v1/catalog/restaurants/{id}")
    CatalogRestaurantResponse getRestaurantById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/catalog/products/restaurant/{restaurantId}")
    List<CatalogProductResponse> getProductsByRestaurant(@PathVariable("restaurantId") Long restaurantId);
}
