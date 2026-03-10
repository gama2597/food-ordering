package com.tecsup.app.micro.order.infrastructure.client;

import com.tecsup.app.micro.order.infrastructure.client.dto.CatalogProductResponse;
import com.tecsup.app.micro.order.infrastructure.client.dto.CatalogRestaurantResponse;
import com.tecsup.app.micro.order.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * Cliente OpenFeign.
 * Esta interfaz le dice a Spring cómo hacer las peticiones HTTP al catalog-service
 * sin tener que escribir código usando RestTemplate o WebClient.
 */
@FeignClient(name = "catalog-service", url = "${clients.catalog-service.url}", configuration = FeignConfig.class)
public interface CatalogServiceClient {

    @GetMapping("/api/v1/catalog/restaurants/{id}")
    CatalogRestaurantResponse getRestaurantById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId
    );

    @GetMapping("/api/v1/catalog/products/restaurant/{restaurantId}")
    List<CatalogProductResponse> getProductsByRestaurant(
            @PathVariable("restaurantId") Long restaurantId,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId
    );
}
