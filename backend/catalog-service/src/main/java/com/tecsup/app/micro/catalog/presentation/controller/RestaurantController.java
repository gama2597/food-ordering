package com.tecsup.app.micro.catalog.presentation.controller;

import com.tecsup.app.micro.catalog.application.service.RestaurantApplicationService;
import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.presentation.dto.RestaurantRequest;
import com.tecsup.app.micro.catalog.presentation.dto.RestaurantResponse;
import com.tecsup.app.micro.catalog.presentation.mapper.RestaurantPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurants", description = "API para gestionar restaurantes")
public class RestaurantController {

    private final RestaurantApplicationService restaurantService;
    private final RestaurantPresentationMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Crear un nuevo restaurante")
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantRequest request) {
        // 1. Mapear DTO a Dominio
        Restaurant domainRequest = mapper.toDomain(request);
        // 2. Ejecutar lógica de negocio
        Restaurant savedDomain = restaurantService.createRestaurant(domainRequest);
        // 3. Mapear Dominio a DTO de respuesta
        return new ResponseEntity<>(mapper.toResponse(savedDomain), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los restaurantes activos")
    public ResponseEntity<List<RestaurantResponse>> getAllActiveRestaurants() {
        List<Restaurant> domains = restaurantService.getAllActiveRestaurants();
        return ResponseEntity.ok(mapper.toResponseList(domains));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener restaurante por ID")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) {
        Restaurant domain = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(mapper.toResponse(domain));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Desactivar restaurante por ID")
    public ResponseEntity<Void> deactivateRestaurant(@PathVariable Long id) {
        restaurantService.deactivateRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
