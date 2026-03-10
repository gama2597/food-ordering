package com.tecsup.app.micro.delivery.application.service;

import com.tecsup.app.micro.delivery.application.usecase.command.PaymentApprovedCommand;
import com.tecsup.app.micro.delivery.domain.model.Delivery;

/**
 * Servicio de aplicacion que orquesta casos de uso y centraliza el flujo de negocio para la capa de presentacion.
 */
public interface DeliveryApplicationService {

    Delivery processPaymentApproved(PaymentApprovedCommand command);

    Delivery getDeliveryByOrderId(Long orderId);
}

