package com.tecsup.app.micro.catalog.presentation.mapper;

import com.tecsup.app.micro.catalog.domain.model.Product;
import com.tecsup.app.micro.catalog.presentation.dto.ProductRequest;
import com.tecsup.app.micro.catalog.presentation.dto.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductPresentationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "available", ignore = true)
    Product toDomain(ProductRequest request);

    ProductResponse toResponse(Product domain);

    List<ProductResponse> toResponseList(List<Product> domains);
}
