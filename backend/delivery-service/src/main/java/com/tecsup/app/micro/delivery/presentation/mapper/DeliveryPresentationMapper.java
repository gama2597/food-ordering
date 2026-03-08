package com.tecsup.app.micro.delivery.presentation.mapper;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.presentation.dto.DeliveryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryPresentationMapper {

    @Mapping(target = "status", expression = "java(domain.getStatus().name())")
    DeliveryResponse toResponse(Delivery domain);
}
