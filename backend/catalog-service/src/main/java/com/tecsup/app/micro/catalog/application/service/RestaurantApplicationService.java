package com.tecsup.app.micro.catalog.application.service;

import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import java.util.List;

/**
 * Servicio de aplicacion que orquesta casos de uso y centraliza el flujo de negocio para la capa de presentacion.
 */
public interface RestaurantApplicationService {
    Restaurant createRestaurant(Restaurant restaurant);

    Restaurant getRestaurantById(Long id);

    List<Restaurant> getAllActiveRestaurants();

    void deactivateRestaurant(Long id);
}

