package com.tecsup.app.micro.payment.domain.port;

import com.tecsup.app.micro.payment.domain.model.Payment;

public interface PaymentEventPublisherPort {

    void publishApproved(Payment payment);

    void publishRejected(Payment payment);
}
