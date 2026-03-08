package com.tecsup.app.micro.user.infrastructure.persistence.mapper;

import com.tecsup.app.micro.user.domain.model.Address;
import com.tecsup.app.micro.user.infrastructure.persistence.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressPersistenceMapper {

    AddressEntity toEntity(Address domain);

    Address toDomain(AddressEntity entity);

    List<Address> toDomainList(List<AddressEntity> entities);
}
