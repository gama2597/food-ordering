package com.tecsup.app.micro.delivery.application.service;

import com.tecsup.app.micro.delivery.application.usecase.command.PaymentApprovedCommand;
import com.tecsup.app.micro.delivery.domain.model.Delivery;

public interface DeliveryApplicationService {

    Delivery processPaymentApproved(PaymentApprovedCommand command);

    Delivery getDeliveryByOrderId(Long orderId);
}
