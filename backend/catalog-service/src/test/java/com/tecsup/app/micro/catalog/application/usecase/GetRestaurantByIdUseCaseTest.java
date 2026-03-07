package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.exception.CatalogDomainException;
import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetRestaurantByIdUseCaseTest {

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @InjectMocks
    private GetRestaurantByIdUseCase useCase;

    @Test
    void execute_shouldReturnRestaurantWhenExists() {
        Restaurant restaurant = Restaurant.builder().id(1L).name("R1").active(true).build();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        Restaurant result = useCase.execute(1L);

        assertEquals(1L, result.getId());
        assertEquals("R1", result.getName());
    }

    @Test
    void execute_shouldThrowWhenIdIsNull() {
        CatalogDomainException ex = assertThrows(CatalogDomainException.class, () -> useCase.execute(null));
        assertEquals("El ID del restaurante es obligatorio", ex.getMessage());
    }

    @Test
    void execute_shouldThrowWhenRestaurantNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        CatalogDomainException ex = assertThrows(CatalogDomainException.class, () -> useCase.execute(99L));
        assertEquals("Restaurante no encontrado con ID: 99", ex.getMessage());
    }
}
