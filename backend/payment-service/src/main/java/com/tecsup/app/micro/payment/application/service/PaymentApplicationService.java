package com.tecsup.app.micro.payment.application.service;

import com.tecsup.app.micro.payment.application.usecase.command.OrderCreatedCommand;
import com.tecsup.app.micro.payment.domain.model.Payment;

public interface PaymentApplicationService {

    Payment processOrderCreated(OrderCreatedCommand command);

    Payment getPaymentByOrderId(Long orderId);
}
