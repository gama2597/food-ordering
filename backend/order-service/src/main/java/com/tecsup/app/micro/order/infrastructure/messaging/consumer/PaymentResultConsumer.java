package com.tecsup.app.micro.order.infrastructure.messaging.consumer;

import com.tecsup.app.micro.order.application.service.OrderApplicationService;
import com.tecsup.app.micro.order.infrastructure.messaging.event.PaymentApprovedEvent;
import com.tecsup.app.micro.order.infrastructure.messaging.event.PaymentRejectedEvent;
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
public class PaymentResultConsumer {

    private final OrderApplicationService orderApplicationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.payment-approved:payment.approved}")
    public void onPaymentApproved(
            String payload,
            @Header(name = CorrelationIdSupport.CORRELATION_ID_HEADER, required = false) String correlationId
    ) {
        CorrelationIdSupport.set(correlationId);
        try {
            PaymentApprovedEvent event = objectMapper.readValue(payload, PaymentApprovedEvent.class);
            orderApplicationService.applyPaymentApproved(event.orderId());
            log.info("Evento payment.approved procesado para orderId={}", event.orderId());
        } catch (Exception ex) {
            log.warn("Fallo controlado procesando payment.approved. Se aplicara retry/DLQ. causa={}", ex.getMessage());
            throw new IllegalStateException("No se pudo procesar payment.approved", ex);
        } finally {
            CorrelationIdSupport.clear();
        }
    }

    @KafkaListener(topics = "${app.kafka.topics.payment-rejected:payment.rejected}")
    public void onPaymentRejected(
            String payload,
            @Header(name = CorrelationIdSupport.CORRELATION_ID_HEADER, required = false) String correlationId
    ) {
        CorrelationIdSupport.set(correlationId);
        try {
            PaymentRejectedEvent event = objectMapper.readValue(payload, PaymentRejectedEvent.class);
            orderApplicationService.applyPaymentRejected(event.orderId());
            log.info("Evento payment.rejected procesado para orderId={}", event.orderId());
        } catch (Exception ex) {
            log.warn("Fallo controlado procesando payment.rejected. Se aplicara retry/DLQ. causa={}", ex.getMessage());
            throw new IllegalStateException("No se pudo procesar payment.rejected", ex);
        } finally {
            CorrelationIdSupport.clear();
        }
    }
}
