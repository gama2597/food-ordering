package com.tecsup.app.micro.order.application.service.impl;

import com.tecsup.app.micro.order.application.service.OrderApplicationService;
import com.tecsup.app.micro.order.application.usecase.CreateOrderUseCase;
import com.tecsup.app.micro.order.application.usecase.ApplyDeliveryProgressUseCase;
import com.tecsup.app.micro.order.application.usecase.ApplyPaymentResultUseCase;
import com.tecsup.app.micro.order.application.usecase.GetMyOrderByIdUseCase;
import com.tecsup.app.micro.order.application.usecase.ListMyOrdersUseCase;
import com.tecsup.app.micro.order.application.usecase.RequestMyOrderPaymentUseCase;
import com.tecsup.app.micro.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de Fachada.
 * Actúa como la puerta principal para interactuar con todos los Casos de Uso.
 * Gestiona el control transaccional de Spring (@Transactional).
 */
@Service
@RequiredArgsConstructor
public class OrderApplicationServiceImpl implements OrderApplicationService {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetMyOrderByIdUseCase getMyOrderByIdUseCase;
    private final ListMyOrdersUseCase listMyOrdersUseCase;
    private final RequestMyOrderPaymentUseCase requestMyOrderPaymentUseCase;
    private final ApplyPaymentResultUseCase applyPaymentResultUseCase;
    private final ApplyDeliveryProgressUseCase applyDeliveryProgressUseCase;

    @Override
    @Transactional
    public Order createOrder(String customerAuthUserId, Order order) {
        return createOrderUseCase.execute(customerAuthUserId, order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getMyOrderById(String customerAuthUserId, Long orderId) {
        return getMyOrderByIdUseCase.execute(customerAuthUserId, orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> listMyOrders(String customerAuthUserId) {
        return listMyOrdersUseCase.execute(customerAuthUserId);
    }

    @Override
    @Transactional
    public Order requestMyOrderPayment(String customerAuthUserId, Long orderId) {
        return requestMyOrderPaymentUseCase.execute(customerAuthUserId, orderId);
    }

    @Override
    @Transactional
    public void applyPaymentApproved(Long orderId) {
        applyPaymentResultUseCase.applyApproved(orderId);
    }

    @Override
    @Transactional
    public void applyPaymentRejected(Long orderId) {
        applyPaymentResultUseCase.applyRejected(orderId);
    }

    @Override
    @Transactional
    public void applyDeliveryAssigned(Long orderId) {
        applyDeliveryProgressUseCase.applyAssigned(orderId);
    }

    @Override
    @Transactional
    public void applyDeliveryStarted(Long orderId) {
        applyDeliveryProgressUseCase.applyStarted(orderId);
    }

    @Override
    @Transactional
    public void applyDeliveryDelivered(Long orderId) {
        applyDeliveryProgressUseCase.applyDelivered(orderId);
    }
}

