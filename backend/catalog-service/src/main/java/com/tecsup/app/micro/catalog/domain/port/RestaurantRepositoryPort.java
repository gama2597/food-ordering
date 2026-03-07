package com.tecsup.app.micro.catalog.domain.port;

import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepositoryPort {
    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Long id);

    List<Restaurant> findAllActive();
}