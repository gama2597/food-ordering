package com.tecsup.app.micro.user.presentation.controller;

import com.tecsup.app.micro.user.application.service.UserApplicationService;
import com.tecsup.app.micro.user.domain.model.Address;
import com.tecsup.app.micro.user.domain.model.UserProfile;
import com.tecsup.app.micro.user.presentation.dto.AddressRequest;
import com.tecsup.app.micro.user.presentation.dto.AddressResponse;
import com.tecsup.app.micro.user.presentation.dto.UpdateMyProfileRequest;
import com.tecsup.app.micro.user.presentation.dto.UserProfileResponse;
import com.tecsup.app.micro.user.presentation.mapper.UserPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST que expone endpoints HTTP del microservicio y delega la logica al servicio de aplicacion.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API para perfil y direcciones del usuario autenticado")
public class UserController {

    private final UserApplicationService userApplicationService;
    private final UserPresentationMapper mapper;

    @GetMapping("/me")
    @Operation(summary = "Obtiene o crea el perfil del usuario autenticado")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        UserProfile profile = userApplicationService.getOrCreateProfile(
                jwt.getSubject(),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name")
        );
        return ResponseEntity.ok(mapper.toResponse(profile));
    }

    @PutMapping("/me")
    @Operation(summary = "Actualiza el perfil del usuario autenticado")
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateMyProfileRequest request
    ) {
        UserProfile updated = userApplicationService.updateMyProfile(jwt.getSubject(), mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @GetMapping("/me/addresses")
    @Operation(summary = "Lista direcciones activas del usuario autenticado")
    public ResponseEntity<List<AddressResponse>> listMyAddresses(@AuthenticationPrincipal Jwt jwt) {
        List<Address> addresses = userApplicationService.listMyAddresses(jwt.getSubject());
        return ResponseEntity.ok(mapper.toAddressResponseList(addresses));
    }

    @PostMapping("/me/addresses")
    @Operation(summary = "Agrega una direccion para el usuario autenticado")
    public ResponseEntity<AddressResponse> addAddress(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AddressRequest request
    ) {
        Address saved = userApplicationService.addAddress(jwt.getSubject(), mapper.toDomain(request));
        return new ResponseEntity<>(mapper.toResponse(saved), HttpStatus.CREATED);
    }

    @PutMapping("/me/addresses/{addressId}")
    @Operation(summary = "Actualiza una direccion del usuario autenticado")
    public ResponseEntity<AddressResponse> updateAddress(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request
    ) {
        Address updated = userApplicationService.updateAddress(jwt.getSubject(), addressId, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/me/addresses/{addressId}")
    @Operation(summary = "Elimina (inactiva) una direccion del usuario autenticado")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long addressId
    ) {
        userApplicationService.deleteAddress(jwt.getSubject(), addressId);
        return ResponseEntity.noContent().build();
    }
}

