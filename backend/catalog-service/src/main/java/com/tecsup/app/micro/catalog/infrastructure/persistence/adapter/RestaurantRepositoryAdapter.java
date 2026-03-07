package com.tecsup.app.micro.catalog.infrastructure.persistence.adapter;

import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import com.tecsup.app.micro.catalog.infrastructure.persistence.entity.RestaurantEntity;
import com.tecsup.app.micro.catalog.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.tecsup.app.micro.catalog.infrastructure.persistence.repository.RestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryAdapter implements RestaurantRepositoryPort {

    private final RestaurantJpaRepository jpaRepository;
    private final RestaurantPersistenceMapper mapper;

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity entity = mapper.toEntity(restaurant);
        RestaurantEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Restaurant> findAllActive() {
        List<RestaurantEntity> activeEntities = jpaRepository.findByActiveTrue();
        return mapper.toDomainList(activeEntities);
    }
}