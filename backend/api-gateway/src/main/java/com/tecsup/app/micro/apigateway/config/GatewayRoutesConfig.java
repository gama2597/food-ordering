package com.tecsup.app.micro.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(
            RouteLocatorBuilder builder,
            @Value("${CATALOG_SERVICE_URL:http://localhost:8082}") String catalogServiceUrl,
            @Value("${USER_SERVICE_URL:http://localhost:8081}") String userServiceUrl
    ) {
        return builder.routes()
                .route("catalog-service", r -> r
                        .path("/api/v1/catalog/**")
                        .uri(catalogServiceUrl))
                .route("catalog-service-docs", r -> r
                        .path("/v3/api-docs/catalog-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/catalog-service", "/v3/api-docs"))
                        .uri(catalogServiceUrl))
                .route("user-service", r -> r
                        .path("/api/v1/users/**")
                        .uri(userServiceUrl))
                .route("user-service-docs", r -> r
                        .path("/v3/api-docs/user-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/user-service", "/v3/api-docs"))
                        .uri(userServiceUrl))
                .build();
    }
}
