package com.tecsup.app.micro.payment.presentation.mapper;

import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.presentation.dto.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentPresentationMapper {

    @Mapping(target = "status", expression = "java(domain.getStatus().name())")
    PaymentResponse toResponse(Payment domain);
}
