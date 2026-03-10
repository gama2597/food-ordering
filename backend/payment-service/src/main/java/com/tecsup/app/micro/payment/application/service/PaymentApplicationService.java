package com.tecsup.app.micro.payment.application.service;

import com.tecsup.app.micro.payment.application.usecase.command.OrderCreatedCommand;
import com.tecsup.app.micro.payment.domain.model.Payment;

/**
 * Servicio de aplicacion que orquesta casos de uso y centraliza el flujo de negocio para la capa de presentacion.
 */
public interface PaymentApplicationService {

    Payment processOrderCreated(OrderCreatedCommand command);

    Payment getPaymentByOrderId(Long orderId);
}

