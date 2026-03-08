package com.tecsup.app.micro.order.infrastructure.client.adapter;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.CatalogProductSnapshot;
import com.tecsup.app.micro.order.domain.port.CatalogQueryPort;
import com.tecsup.app.micro.order.infrastructure.client.CatalogServiceClient;
import com.tecsup.app.micro.order.infrastructure.observability.CorrelationIdSupport;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogQueryAdapter implements CatalogQueryPort {

    private final CatalogServiceClient catalogServiceClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Override
    public void validateRestaurantIsActive(Long restaurantId) {
        String authorization = resolveAuthorizationHeader();
        String correlationId = resolveCorrelationId();

        var circuitBreaker = circuitBreakerFactory.create("catalogService");
        try {
            var restaurant = circuitBreaker.run(() ->
                    catalogServiceClient.getRestaurantById(restaurantId, authorization, correlationId));
            if (restaurant == null) {
                throw new OrderDomainException("No se pudo validar el restaurante del pedido");
            }
            if (!restaurant.active()) {
                throw new OrderDomainException("No se puede crear el pedido: el restaurante no esta activo");
            }
        } catch (NoFallbackAvailableException ex) {
            handleValidationException(restaurantId, ex.getCause() != null ? ex.getCause() : ex);
        } catch (RuntimeException ex) {
            handleValidationException(restaurantId, ex);
        } catch (Exception ex) {
            log.warn("Error general validando restaurante {} cause={}", restaurantId, ex.getMessage());
            throw new OrderDomainException("No se pudo validar el restaurante del pedido");
        }
    }

    @Override
    public Map<Long, CatalogProductSnapshot> getProductsByRestaurant(Long restaurantId) {
        String authorization = resolveAuthorizationHeader();
        String correlationId = resolveCorrelationId();

        var circuitBreaker = circuitBreakerFactory.create("catalogService");
        try {
            List<com.tecsup.app.micro.order.infrastructure.client.dto.CatalogProductResponse> response =
                    circuitBreaker.run(() -> catalogServiceClient.getProductsByRestaurant(restaurantId, authorization, correlationId));

            return response.stream()
                    .map(p -> CatalogProductSnapshot.builder()
                            .productId(p.id())
                            .name(p.name())
                            .price(p.price())
                            .available(p.available())
                            .build())
                    .collect(Collectors.toMap(CatalogProductSnapshot::getProductId, Function.identity()));
        } catch (NoFallbackAvailableException ex) {
            handleProductsException(restaurantId, ex.getCause() != null ? ex.getCause() : ex);
            return Map.of();
        } catch (RuntimeException ex) {
            handleProductsException(restaurantId, ex);
            return Map.of();
        } catch (Exception ex) {
            log.warn("Error general consultando productos de restaurante {} cause={}", restaurantId, ex.getMessage());
            throw new OrderDomainException("No se pudo consultar los productos del restaurante");
        }
    }

    private void handleValidationException(Long restaurantId, Throwable ex) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

        if (cause instanceof CallNotPermittedException) {
            log.warn("Circuit breaker abierto validando restaurante {}", restaurantId);
            throw new OrderDomainException("Servicio de catalogo temporalmente no disponible");
        }
        if (cause instanceof FeignException.NotFound) {
            throw new OrderDomainException("No se puede crear el pedido: el restaurante no existe");
        }
        if (cause instanceof FeignException feignException) {
            log.warn("Error Feign validando restaurante {} status={} message={}",
                    restaurantId, feignException.status(), feignException.getMessage());
            throw new OrderDomainException("No se pudo validar el restaurante del pedido");
        }

        log.warn("Error runtime validando restaurante {} cause={}", restaurantId, cause.getMessage());
        throw new OrderDomainException("No se pudo validar el restaurante del pedido");
    }

    private void handleProductsException(Long restaurantId, Throwable ex) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

        if (cause instanceof CallNotPermittedException) {
            log.warn("Circuit breaker abierto consultando productos de restaurante {}", restaurantId);
            throw new OrderDomainException("Servicio de catalogo temporalmente no disponible");
        }
        if (cause instanceof FeignException feignException) {
            log.warn("Error Feign consultando productos de restaurante {} status={} message={}",
                    restaurantId, feignException.status(), feignException.getMessage());
            throw new OrderDomainException("No se pudo consultar los productos del restaurante");
        }

        log.warn("Error runtime consultando productos de restaurante {} cause={}", restaurantId, cause.getMessage());
        throw new OrderDomainException("No se pudo consultar los productos del restaurante");
    }

    private String resolveAuthorizationHeader() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String authorization = request != null ? request.getHeader("Authorization") : null;

        if ((authorization == null || authorization.isBlank())
                && SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken jwtAuth
                && jwtAuth.getToken() != null
                && jwtAuth.getToken().getTokenValue() != null
                && !jwtAuth.getToken().getTokenValue().isBlank()) {
            authorization = "Bearer " + jwtAuth.getToken().getTokenValue();
        }

        if (authorization == null || authorization.isBlank()) {
            throw new OrderDomainException("No se pudo validar la autenticacion para consultar catalogo");
        }
        return authorization;
    }

    private String resolveCorrelationId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String correlationId = request != null ? request.getHeader(CorrelationIdSupport.CORRELATION_ID_HEADER) : null;
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = CorrelationIdSupport.currentOrCreate();
        }
        return correlationId;
    }
}
