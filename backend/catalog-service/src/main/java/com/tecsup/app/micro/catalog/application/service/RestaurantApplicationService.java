package com.tecsup.app.micro.catalog.application.service;

import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import java.util.List;

public interface RestaurantApplicationService {
    Restaurant createRestaurant(Restaurant restaurant);

    Restaurant getRestaurantById(Long id);

    List<Restaurant> getAllActiveRestaurants();

    void deactivateRestaurant(Long id);
}
