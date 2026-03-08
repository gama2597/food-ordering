package com.tecsup.app.micro.delivery.infrastructure.messaging.consumer;

import com.tecsup.app.micro.delivery.application.service.DeliveryApplicationService;
import com.tecsup.app.micro.delivery.application.usecase.command.PaymentApprovedCommand;
import com.tecsup.app.micro.delivery.infrastructure.messaging.event.PaymentApprovedEvent;
import com.tecsup.app.micro.delivery.infrastructure.observability.CorrelationIdSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentApprovedConsumer {

    private final DeliveryApplicationService deliveryApplicationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.payment-approved:payment.approved}")
    public void onPaymentApproved(
            String payload,
            @Header(name = CorrelationIdSupport.CORRELATION_ID_HEADER, required = false) String correlationId
    ) {
        CorrelationIdSupport.set(correlationId);
        try {
            PaymentApprovedEvent event = objectMapper.readValue(payload, PaymentApprovedEvent.class);
            deliveryApplicationService.processPaymentApproved(PaymentApprovedCommand.builder()
                    .orderId(event.orderId())
                    .customerAuthUserId(event.customerAuthUserId())
                    .build());
            log.info("Evento payment.approved procesado en delivery para orderId={}", event.orderId());
        } catch (Exception ex) {
            log.warn("Fallo controlado procesando payment.approved en delivery. Se aplicara retry/DLQ. causa={}", ex.getMessage());
            throw new IllegalStateException("No se pudo procesar payment.approved en delivery", ex);
        } finally {
            CorrelationIdSupport.clear();
        }
    }
}
