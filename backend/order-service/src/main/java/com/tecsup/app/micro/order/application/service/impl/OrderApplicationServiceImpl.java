package com.tecsup.app.micro.order.application.service.impl;

import com.tecsup.app.micro.order.application.service.OrderApplicationService;
import com.tecsup.app.micro.order.application.usecase.CreateOrderUseCase;
import com.tecsup.app.micro.order.application.usecase.GetMyOrderByIdUseCase;
import com.tecsup.app.micro.order.application.usecase.ListMyOrdersUseCase;
import com.tecsup.app.micro.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderApplicationServiceImpl implements OrderApplicationService {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetMyOrderByIdUseCase getMyOrderByIdUseCase;
    private final ListMyOrdersUseCase listMyOrdersUseCase;

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
}
