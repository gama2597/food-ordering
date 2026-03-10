package com.tecsup.app.micro.order.infrastructure.config;

import com.tecsup.app.micro.order.infrastructure.observability.CorrelationIdSupport;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuración para el cliente Feign.
 */
@Configuration
@Slf4j
public class FeignConfig {

    /**
     * Interceptor que se ejecuta antes de cada petición de Feign.
     * Su trabajo es copiar el Token JWT (Authorization) y el Correlation ID
     * de la petición original del usuario y pegarlos en la nueva petición HTTP que hace Feign.
     * Esto mantiene la seguridad y la trazabilidad a través de la cadena de microservicios.
     */
    @Bean
    public RequestInterceptor authHeaderPropagationInterceptor() {
        return template -> {
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

            if (authorization != null && !authorization.isBlank()) {
                template.header("Authorization", authorization);
            } else if (!template.headers().containsKey("Authorization")) {
                log.warn("Feign sin Authorization para {} {}", template.method(), template.url());
            }

            String correlationId = request != null
                    ? request.getHeader(CorrelationIdSupport.CORRELATION_ID_HEADER)
                    : null;
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = CorrelationIdSupport.currentOrCreate();
            }
            template.header(CorrelationIdSupport.CORRELATION_ID_HEADER, correlationId);
        };
    }
}

