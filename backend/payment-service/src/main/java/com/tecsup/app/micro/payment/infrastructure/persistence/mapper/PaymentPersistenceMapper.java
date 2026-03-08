package com.tecsup.app.micro.payment.infrastructure.persistence.mapper;

import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.infrastructure.persistence.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentPersistenceMapper {

    PaymentEntity toEntity(Payment domain);

    Payment toDomain(PaymentEntity entity);
}
