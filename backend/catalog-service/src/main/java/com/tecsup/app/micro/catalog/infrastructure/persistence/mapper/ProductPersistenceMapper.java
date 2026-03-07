package com.tecsup.app.micro.catalog.infrastructure.persistence.mapper;

import com.tecsup.app.micro.catalog.domain.model.Product;
import com.tecsup.app.micro.catalog.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductPersistenceMapper {

    ProductEntity toEntity(Product domain);

    Product toDomain(ProductEntity entity);

    List<Product> toDomainList(List<ProductEntity> entities);
}