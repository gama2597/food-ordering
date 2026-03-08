package com.tecsup.app.micro.delivery.infrastructure.persistence.mapper;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.infrastructure.persistence.entity.DeliveryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryPersistenceMapper {

    DeliveryEntity toEntity(Delivery domain);

    Delivery toDomain(DeliveryEntity entity);
}
