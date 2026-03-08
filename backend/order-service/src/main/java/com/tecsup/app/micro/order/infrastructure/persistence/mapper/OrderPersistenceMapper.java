package com.tecsup.app.micro.order.infrastructure.persistence.mapper;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.model.OrderItem;
import com.tecsup.app.micro.order.infrastructure.persistence.entity.OrderEntity;
import com.tecsup.app.micro.order.infrastructure.persistence.entity.OrderItemEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderPersistenceMapper {

    @Mapping(target = "items", source = "items")
    OrderEntity toEntity(Order domain);

    @Mapping(target = "items", source = "items")
    Order toDomain(OrderEntity entity);

    List<Order> toDomainList(List<OrderEntity> entities);

    @Mapping(target = "order", ignore = true)
    OrderItemEntity toItemEntity(OrderItem item);

    OrderItem toItemDomain(OrderItemEntity entity);

    @AfterMapping
    default void linkItems(@MappingTarget OrderEntity orderEntity) {
        if (orderEntity.getItems() == null) {
            return;
        }
        for (OrderItemEntity item : orderEntity.getItems()) {
            item.setOrder(orderEntity);
        }
    }
}
