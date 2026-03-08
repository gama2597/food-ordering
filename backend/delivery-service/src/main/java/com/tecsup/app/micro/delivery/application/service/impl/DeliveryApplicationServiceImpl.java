package com.tecsup.app.micro.delivery.application.service.impl;

import com.tecsup.app.micro.delivery.application.service.DeliveryApplicationService;
import com.tecsup.app.micro.delivery.application.usecase.GetDeliveryByOrderIdUseCase;
import com.tecsup.app.micro.delivery.application.usecase.ProcessPaymentApprovedUseCase;
import com.tecsup.app.micro.delivery.application.usecase.command.PaymentApprovedCommand;
import com.tecsup.app.micro.delivery.domain.model.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryApplicationServiceImpl implements DeliveryApplicationService {

    private final ProcessPaymentApprovedUseCase processPaymentApprovedUseCase;
    private final GetDeliveryByOrderIdUseCase getDeliveryByOrderIdUseCase;

    @Override
    @Transactional
    public Delivery processPaymentApproved(PaymentApprovedCommand command) {
        return processPaymentApprovedUseCase.execute(command);
    }

    @Override
    @Transactional(readOnly = true)
    public Delivery getDeliveryByOrderId(Long orderId) {
        return getDeliveryByOrderIdUseCase.execute(orderId);
    }
}
