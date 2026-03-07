package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllActiveRestaurantsUseCase {

    private final RestaurantRepositoryPort restaurantRepository;

    public List<Restaurant> execute() {
        return restaurantRepository.findAllActive();
    }
}
