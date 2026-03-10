package com.tecsup.app.micro.order.application.service;

import com.tecsup.app.micro.order.domain.model.Order;

import java.util.List;

/**
 * Servicio de aplicacion que orquesta casos de uso y centraliza el flujo de negocio para la capa de presentacion.
 */
public interface OrderApplicationService {

    Order createOrder(String customerAuthUserId, Order order);

    Order getMyOrderById(String customerAuthUserId, Long orderId);

    List<Order> listMyOrders(String customerAuthUserId);

    Order requestMyOrderPayment(String customerAuthUserId, Long orderId);

    void applyPaymentApproved(Long orderId);

    void applyPaymentRejected(Long orderId);

    void applyDeliveryAssigned(Long orderId);

    void applyDeliveryStarted(Long orderId);

    void applyDeliveryDelivered(Long orderId);
}

