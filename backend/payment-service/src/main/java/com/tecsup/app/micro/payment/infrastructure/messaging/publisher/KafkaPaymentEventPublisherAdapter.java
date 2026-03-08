package com.tecsup.app.micro.payment.infrastructure.messaging.publisher;

import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.domain.port.PaymentEventPublisherPort;
import com.tecsup.app.micro.payment.infrastructure.messaging.mapper.PaymentEventMapper;
import com.tecsup.app.micro.payment.infrastructure.observability.CorrelationIdSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
@ConditionalOnBean(KafkaTemplate.class)
public class KafkaPaymentEventPublisherAdapter implements PaymentEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentEventMapper mapper;

    @Value("${app.kafka.topics.payment-approved:payment.approved}")
    private String paymentApprovedTopic;

    @Value("${app.kafka.topics.payment-rejected:payment.rejected}")
    private String paymentRejectedTopic;

    @Override
    public void publishApproved(Payment payment) {
        try {
            var event = mapper.toApprovedEvent(payment);
            kafkaTemplate.send(MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, paymentApprovedTopic)
                    .setHeader(KafkaHeaders.KEY, String.valueOf(payment.getOrderId()))
                    .setHeader(CorrelationIdSupport.CORRELATION_ID_HEADER, CorrelationIdSupport.currentOrCreate())
                    .build());
            log.info("Evento payment.approved publicado para orderId={}", payment.getOrderId());
        } catch (Exception ex) {
            log.error("No se pudo publicar payment.approved para orderId={}", payment.getOrderId(), ex);
        }
    }

    @Override
    public void publishRejected(Payment payment) {
        try {
            var event = mapper.toRejectedEvent(payment);
            kafkaTemplate.send(MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, paymentRejectedTopic)
                    .setHeader(KafkaHeaders.KEY, String.valueOf(payment.getOrderId()))
                    .setHeader(CorrelationIdSupport.CORRELATION_ID_HEADER, CorrelationIdSupport.currentOrCreate())
                    .build());
            log.info("Evento payment.rejected publicado para orderId={}", payment.getOrderId());
        } catch (Exception ex) {
            log.error("No se pudo publicar payment.rejected para orderId={}", payment.getOrderId(), ex);
        }
    }
}
