package com.tecsup.app.micro.payment.application.service.impl;

import com.tecsup.app.micro.payment.application.service.PaymentApplicationService;
import com.tecsup.app.micro.payment.application.usecase.GetPaymentByOrderIdUseCase;
import com.tecsup.app.micro.payment.application.usecase.ProcessOrderCreatedUseCase;
import com.tecsup.app.micro.payment.application.usecase.command.OrderCreatedCommand;
import com.tecsup.app.micro.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion que orquesta casos de uso y centraliza el flujo de negocio para la capa de presentacion.
 */
@Service
@RequiredArgsConstructor
public class PaymentApplicationServiceImpl implements PaymentApplicationService {

    private final ProcessOrderCreatedUseCase processOrderCreatedUseCase;
    private final GetPaymentByOrderIdUseCase getPaymentByOrderIdUseCase;

    @Override
    @Transactional
    public Payment processOrderCreated(OrderCreatedCommand command) {
        return processOrderCreatedUseCase.execute(command);
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentByOrderId(Long orderId) {
        return getPaymentByOrderIdUseCase.execute(orderId);
    }
}

