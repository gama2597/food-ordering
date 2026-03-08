package com.tecsup.app.micro.catalog.application.service.impl;

import com.tecsup.app.micro.catalog.application.service.RestaurantApplicationService;
import com.tecsup.app.micro.catalog.application.usecase.DeactivateRestaurantUseCase;
import com.tecsup.app.micro.catalog.application.usecase.CreateRestaurantUseCase;
import com.tecsup.app.micro.catalog.application.usecase.GetAllActiveRestaurantsUseCase;
import com.tecsup.app.micro.catalog.application.usecase.GetRestaurantByIdUseCase;
import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del Servicio de Aplicación de Restaurantes.
 * Actúa como fachada para los casos de uso y gestiona las transacciones.
 */
@Service
@RequiredArgsConstructor
public class RestaurantApplicationServiceImpl implements RestaurantApplicationService {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final GetRestaurantByIdUseCase getRestaurantByIdUseCase;
    private final GetAllActiveRestaurantsUseCase getAllActiveRestaurantsUseCase;
    private final DeactivateRestaurantUseCase deactivateRestaurantUseCase;

    @Override
    @Transactional
    public Restaurant createRestaurant(Restaurant restaurant) {
        return createRestaurantUseCase.execute(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public Restaurant getRestaurantById(Long id) {
        return getRestaurantByIdUseCase.execute(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurant> getAllActiveRestaurants() {
        return getAllActiveRestaurantsUseCase.execute();
    }

    @Override
    @Transactional
    public void deactivateRestaurant(Long id) {
        deactivateRestaurantUseCase.execute(id);
    }
}
