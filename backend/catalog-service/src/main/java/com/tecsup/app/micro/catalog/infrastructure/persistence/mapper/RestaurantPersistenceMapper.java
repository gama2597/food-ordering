package com.tecsup.app.micro.catalog.infrastructure.persistence.mapper;

import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.infrastructure.persistence.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantPersistenceMapper {

    RestaurantEntity toEntity(Restaurant domain);

    Restaurant toDomain(RestaurantEntity entity);

    List<Restaurant> toDomainList(List<RestaurantEntity> entities);
}