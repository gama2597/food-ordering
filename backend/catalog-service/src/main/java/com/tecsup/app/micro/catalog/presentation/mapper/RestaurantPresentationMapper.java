package com.tecsup.app.micro.catalog.presentation.mapper;

import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.presentation.dto.RestaurantRequest;
import com.tecsup.app.micro.catalog.presentation.dto.RestaurantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantPresentationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Restaurant toDomain(RestaurantRequest request);

    // De Dominio a Response (DTO)
    RestaurantResponse toResponse(Restaurant domain);

    List<RestaurantResponse> toResponseList(List<Restaurant> domains);
}
