package com.tecsup.app.micro.user.application.usecase;

import com.tecsup.app.micro.user.domain.exception.UserDomainException;
import com.tecsup.app.micro.user.domain.port.AddressRepositoryPort;
import com.tecsup.app.micro.user.domain.port.UserProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteMyAddressUseCase {

    private final AddressRepositoryPort addressRepository;
    private final UserProfileRepositoryPort userProfileRepository;

    public void execute(String authUserId, Long addressId) {
        if (addressId == null) {
            throw new UserDomainException("El ID de la direccion es obligatorio");
        }

        var user = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new UserDomainException("Perfil de usuario no encontrado"));

        addressRepository.findActiveByIdAndUserId(addressId, user.getId())
                .orElseThrow(() -> new UserDomainException("Direccion no encontrada"));

        addressRepository.deactivateById(addressId);
    }
}
