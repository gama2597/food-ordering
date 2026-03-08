package com.tecsup.app.micro.order.presentation.mapper;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.model.OrderItem;
import com.tecsup.app.micro.order.presentation.dto.CreateOrderItemRequest;
import com.tecsup.app.micro.order.presentation.dto.CreateOrderRequest;
import com.tecsup.app.micro.order.presentation.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderPresentationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerAuthUserId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toDomain(CreateOrderRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    OrderItem toItemDomain(CreateOrderItemRequest request);

    OrderResponse toResponse(Order domain);

    List<OrderResponse> toResponseList(List<Order> orders);
}
