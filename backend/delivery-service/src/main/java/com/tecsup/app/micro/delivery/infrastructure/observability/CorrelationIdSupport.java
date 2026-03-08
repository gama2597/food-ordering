package com.tecsup.app.micro.delivery.infrastructure.observability;

import org.slf4j.MDC;

import java.util.UUID;

public final class CorrelationIdSupport {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String MDC_CORRELATION_ID_KEY = "correlationId";

    private CorrelationIdSupport() {
    }

    public static String currentOrCreate() {
        String correlationId = MDC.get(MDC_CORRELATION_ID_KEY);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
            MDC.put(MDC_CORRELATION_ID_KEY, correlationId);
        }
        return correlationId;
    }

    public static void set(String correlationId) {
        if (correlationId == null || correlationId.isBlank()) {
            MDC.put(MDC_CORRELATION_ID_KEY, UUID.randomUUID().toString());
            return;
        }
        MDC.put(MDC_CORRELATION_ID_KEY, correlationId);
    }

    public static void clear() {
        MDC.remove(MDC_CORRELATION_ID_KEY);
    }
}
