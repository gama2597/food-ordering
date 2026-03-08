package com.tecsup.app.micro.order.infrastructure.messaging.consumer;

import com.tecsup.app.micro.order.application.service.OrderApplicationService;
import com.tecsup.app.micro.order.infrastructure.messaging.event.DeliveryAssignedEvent;
import com.tecsup.app.micro.order.infrastructure.messaging.event.DeliveryDeliveredEvent;
import com.tecsup.app.micro.order.infrastructure.messaging.event.DeliveryStartedEvent;
import com.tecsup.app.micro.order.infrastructure.observability.CorrelationIdSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryProgressConsumer {

    private final OrderApplicationService orderApplicationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.delivery-assigned:delivery.assigned}")
    public void onDeliveryAssigned(
            String payload,
            @Header(name = CorrelationIdSupport.CORRELATION_ID_HEADER, required = false) String correlationId
    ) {
        CorrelationIdSupport.set(correlationId);
        try {
            DeliveryAssignedEvent event = objectMapper.readValue(payload, DeliveryAssignedEvent.class);
            orderApplicationService.applyDeliveryAssigned(event.orderId());
            log.info("Evento delivery.assigned procesado para orderId={}", event.orderId());
        } catch (Exception ex) {
            log.warn("Fallo controlado procesando delivery.assigned. Se aplicara retry/DLQ. causa={}", ex.getMessage());
            throw new IllegalStateException("No se pudo procesar delivery.assigned", ex);
        } finally {
            CorrelationIdSupport.clear();
        }
    }

    @KafkaListener(topics = "${app.kafka.topics.delivery-started:delivery.started}")
    public void onDeliveryStarted(
            String payload,
            @Header(name = CorrelationIdSupport.CORRELATION_ID_HEADER, required = false) String correlationId
    ) {
        CorrelationIdSupport.set(correlationId);
        try {
            DeliveryStartedEvent event = objectMapper.readValue(payload, DeliveryStartedEvent.class);
            orderApplicationService.applyDeliveryStarted(event.orderId());
            log.info("Evento delivery.started procesado para orderId={}", event.orderId());
        } catch (Exception ex) {
            log.warn("Fallo controlado procesando delivery.started. Se aplicara retry/DLQ. causa={}", ex.getMessage());
            throw new IllegalStateException("No se pudo procesar delivery.started", ex);
        } finally {
            CorrelationIdSupport.clear();
        }
    }

    @KafkaListener(topics = "${app.kafka.topics.delivery-delivered:delivery.delivered}")
    public void onDeliveryDelivered(
            String payload,
            @Header(name = CorrelationIdSupport.CORRELATION_ID_HEADER, required = false) String correlationId
    ) {
        CorrelationIdSupport.set(correlationId);
        try {
            DeliveryDeliveredEvent event = objectMapper.readValue(payload, DeliveryDeliveredEvent.class);
            orderApplicationService.applyDeliveryDelivered(event.orderId());
            log.info("Evento delivery.delivered procesado para orderId={}", event.orderId());
        } catch (Exception ex) {
            log.warn("Fallo controlado procesando delivery.delivered. Se aplicara retry/DLQ. causa={}", ex.getMessage());
            throw new IllegalStateException("No se pudo procesar delivery.delivered", ex);
        } finally {
            CorrelationIdSupport.clear();
        }
    }
}
