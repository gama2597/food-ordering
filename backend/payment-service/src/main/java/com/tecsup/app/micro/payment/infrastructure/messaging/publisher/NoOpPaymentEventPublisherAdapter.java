package com.tecsup.app.micro.payment.infrastructure.messaging.publisher;

import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.domain.port.PaymentEventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnMissingBean(PaymentEventPublisherPort.class)
public class NoOpPaymentEventPublisherAdapter implements PaymentEventPublisherPort {

    @Override
    public void publishApproved(Payment payment) {
        log.warn("KafkaTemplate no disponible. No se publica payment.approved para orderId={}", payment.getOrderId());
    }

    @Override
    public void publishRejected(Payment payment) {
        log.warn("KafkaTemplate no disponible. No se publica payment.rejected para orderId={}", payment.getOrderId());
    }
}
