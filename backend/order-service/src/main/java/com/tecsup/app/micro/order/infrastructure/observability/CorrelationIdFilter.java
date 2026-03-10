package com.tecsup.app.micro.order.infrastructure.observability;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro web que atrapa todas las peticiones HTTP que entran a este microservicio.
 */
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String correlationId = request.getHeader(CorrelationIdSupport.CORRELATION_ID_HEADER);
            CorrelationIdSupport.set(correlationId);
            response.setHeader(CorrelationIdSupport.CORRELATION_ID_HEADER, CorrelationIdSupport.currentOrCreate());
            filterChain.doFilter(request, response);
        } finally {
            CorrelationIdSupport.clear();
        }
    }
}
