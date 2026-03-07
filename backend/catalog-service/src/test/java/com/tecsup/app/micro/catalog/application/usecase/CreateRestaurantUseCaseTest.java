package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.exception.CatalogDomainException;
import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @InjectMocks
    private CreateRestaurantUseCase useCase;

    @Test
    void execute_shouldSetRestaurantAsActiveAndPersist() {
        Restaurant input = Restaurant.builder()
                .name("La Trattoria")
                .description("Pasta")
                .address("Av. Principal 123")
                .active(false)
                .build();

        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> {
            Restaurant toSave = invocation.getArgument(0);
            return toSave.toBuilder().id(1L).build();
        });

        Restaurant result = useCase.execute(input);

        ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository).save(captor.capture());
        Restaurant persisted = captor.getValue();

        assertTrue(persisted.isActive());
        assertEquals(1L, result.getId());
        assertTrue(result.isActive());
    }

    @Test
    void execute_shouldThrowWhenRestaurantIsNull() {
        CatalogDomainException ex = assertThrows(CatalogDomainException.class, () -> useCase.execute(null));
        assertEquals("El restaurante es obligatorio", ex.getMessage());
    }

    @Test
    void execute_shouldThrowWhenRestaurantNameIsBlank() {
        Restaurant input = Restaurant.builder().name("  ").build();

        CatalogDomainException ex = assertThrows(CatalogDomainException.class, () -> useCase.execute(input));
        assertEquals("El nombre del restaurante es obligatorio", ex.getMessage());
    }
}
