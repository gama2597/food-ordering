package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllActiveRestaurantsUseCaseTest {

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @InjectMocks
    private GetAllActiveRestaurantsUseCase useCase;

    @Test
    void execute_shouldReturnAllActiveRestaurants() {
        List<Restaurant> restaurants = List.of(
                Restaurant.builder().id(1L).name("R1").active(true).build(),
                Restaurant.builder().id(2L).name("R2").active(true).build()
        );

        when(restaurantRepository.findAllActive()).thenReturn(restaurants);

        List<Restaurant> result = useCase.execute();

        assertEquals(2, result.size());
        verify(restaurantRepository).findAllActive();
    }
}
