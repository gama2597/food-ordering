package com.tecsup.app.micro.payment.infrastructure.messaging.consumer;

import com.tecsup.app.micro.payment.application.service.PaymentApplicationService;
import com.tecsup.app.micro.payment.application.usecase.command.OrderCreatedCommand;
import com.tecsup.app.micro.payment.infrastructure.messaging.event.PaymentRequestedEvent;
import com.tecsup.app.micro.payment.infrastructure.observability.CorrelationIdSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestedConsumer {

    private final PaymentApplicationService paymentApplicationService;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topics.payment-requested:payment.requested}")
    private String paymentRequestedTopic;

    @KafkaListener(topics = "${app.kafka.topics.payment-requested:payment.requested}")
    public void onPaymentRequested(
            String payload,
            @Header(name = CorrelationIdSupport.CORRELATION_ID_HEADER, required = false) String correlationId
    ) {
        CorrelationIdSupport.set(correlationId);
        try {
            PaymentRequestedEvent event = objectMapper.readValue(payload, PaymentRequestedEvent.class);
            paymentApplicationService.processOrderCreated(OrderCreatedCommand.builder()
                    .orderId(event.orderId())
                    .customerAuthUserId(event.customerAuthUserId())
                    .amount(event.amount())
                    .currency(event.currency())
                    .occurredAt(event.requestedAt())
                    .build());
            log.info("Evento {} procesado para orderId={}", paymentRequestedTopic, event.orderId());
        } catch (Exception ex) {
            log.warn("Fallo controlado procesando payment.requested. Se aplicara retry/DLQ. causa={}", ex.getMessage());
            throw new IllegalStateException("No se pudo procesar payment.requested", ex);
        } finally {
            CorrelationIdSupport.clear();
        }
    }
}
