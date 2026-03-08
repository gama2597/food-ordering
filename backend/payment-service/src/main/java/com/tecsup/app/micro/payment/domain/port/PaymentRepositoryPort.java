package com.tecsup.app.micro.payment.domain.port;

import com.tecsup.app.micro.payment.domain.model.Payment;

import java.util.Optional;

public interface PaymentRepositoryPort {

    Optional<Payment> findByOrderId(Long orderId);

    Payment save(Payment payment);
}
