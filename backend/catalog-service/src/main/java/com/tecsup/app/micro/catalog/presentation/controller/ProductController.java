package com.tecsup.app.micro.catalog.presentation.controller;

import com.tecsup.app.micro.catalog.application.service.ProductApplicationService;
import com.tecsup.app.micro.catalog.domain.model.Product;
import com.tecsup.app.micro.catalog.presentation.dto.ProductRequest;
import com.tecsup.app.micro.catalog.presentation.dto.ProductResponse;
import com.tecsup.app.micro.catalog.presentation.mapper.ProductPresentationMapper;
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
@RequestMapping("/api/v1/catalog/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "API para gestionar los platos/productos de los restaurantes")
public class ProductController {

    private final ProductApplicationService productService;
    private final ProductPresentationMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Agregar un nuevo producto a un restaurante")
    public ResponseEntity<ProductResponse> addProductToRestaurant(@Valid @RequestBody ProductRequest request) {
        Product domainRequest = mapper.toDomain(request);
        Product savedDomain = productService.addProductToRestaurant(domainRequest);
        return new ResponseEntity<>(mapper.toResponse(savedDomain), HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Obtener todos los productos de un restaurante específico")
    public ResponseEntity<List<ProductResponse>> getProductsByRestaurant(@PathVariable Long restaurantId) {
        // Cualquier usuario autenticado puede ver el menú
        List<Product> domains = productService.getProductsByRestaurant(restaurantId);
        return ResponseEntity.ok(mapper.toResponseList(domains));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Desactivar producto por ID")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return ResponseEntity.noContent().build();
    }
}
