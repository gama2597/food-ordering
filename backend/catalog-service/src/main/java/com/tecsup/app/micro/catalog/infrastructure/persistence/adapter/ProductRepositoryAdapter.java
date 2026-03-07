package com.tecsup.app.micro.catalog.infrastructure.persistence.adapter;

import com.tecsup.app.micro.catalog.domain.model.Product;
import com.tecsup.app.micro.catalog.domain.port.ProductRepositoryPort;
import com.tecsup.app.micro.catalog.infrastructure.persistence.entity.ProductEntity;
import com.tecsup.app.micro.catalog.infrastructure.persistence.mapper.ProductPersistenceMapper;
import com.tecsup.app.micro.catalog.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository jpaRepository;
    private final ProductPersistenceMapper mapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Product> findByRestaurantId(Long restaurantId) {
        List<ProductEntity> entities = jpaRepository.findByRestaurantId(restaurantId);
        return mapper.toDomainList(entities);
    }
}