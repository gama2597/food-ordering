package com.tecsup.app.micro.payment.infrastructure.messaging.consumer;

import com.tecsup.app.micro.payment.application.service.PaymentApplicationService;
import com.tecsup.app.micro.payment.application.usecase.command.OrderCreatedCommand;
import com.tecsup.app.micro.payment.infrastructure.messaging.event.PaymentRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
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
    public void onPaymentRequested(String payload) {
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
            log.error("Fallo procesando evento payment.requested payload={}", payload, ex);
        }
    }
}
