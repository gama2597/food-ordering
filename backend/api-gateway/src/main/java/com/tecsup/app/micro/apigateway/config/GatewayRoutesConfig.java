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
            // Inyectamos las URLs de los microservicios desde el application.yml o variables de entorno.
            // Si la variable no existe, usa el localhost por defecto (ideal para desarrollo local).
            @Value("${CATALOG_SERVICE_URL:http://localhost:8082}") String catalogServiceUrl,
            @Value("${USER_SERVICE_URL:http://localhost:8081}") String userServiceUrl,
            @Value("${ORDER_SERVICE_URL:http://localhost:8083}") String orderServiceUrl,
            @Value("${PAYMENT_SERVICE_URL:http://localhost:8084}") String paymentServiceUrl,
            @Value("${DELIVERY_SERVICE_URL:http://localhost:8085}") String deliveryServiceUrl
    ) {
        // El builder define las reglas (rutas) de redirección.
        return builder.routes()
                // 1. Reglas para el Catálogo
                .route("catalog-service", r -> r
                        .path("/api/v1/catalog/**") // Si la petición entra por esta ruta...
                        .uri(catalogServiceUrl)) // ...envíala a este microservicio.
                // Regla especial para centralizar la documentación (Swagger) de Catálogo
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
                        
                .route("order-service", r -> r
                        .path("/api/v1/orders/**")
                        .uri(orderServiceUrl))
                .route("order-service-docs", r -> r
                        .path("/v3/api-docs/order-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/order-service", "/v3/api-docs"))
                        .uri(orderServiceUrl))

                .route("payment-service", r -> r
                        .path("/api/v1/payments/**")
                        .uri(paymentServiceUrl))
                .route("payment-service-docs", r -> r
                        .path("/v3/api-docs/payment-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/payment-service", "/v3/api-docs"))
                        .uri(paymentServiceUrl))

                .route("delivery-service", r -> r
                        .path("/api/v1/deliveries/**")
                        .uri(deliveryServiceUrl))
                .route("delivery-service-docs", r -> r
                        .path("/v3/api-docs/delivery-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/delivery-service", "/v3/api-docs"))
                        .uri(deliveryServiceUrl))
                        
                .build();
    }
}
