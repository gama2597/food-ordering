package com.tecsup.app.micro.user.application.usecase;

import com.tecsup.app.micro.user.domain.exception.UserDomainException;
import com.tecsup.app.micro.user.domain.model.Address;
import com.tecsup.app.micro.user.domain.port.AddressRepositoryPort;
import com.tecsup.app.micro.user.domain.port.UserProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddAddressToMyProfileUseCase {

    private final AddressRepositoryPort addressRepository;
    private final UserProfileRepositoryPort userProfileRepository;

    public Address execute(String authUserId, Address address) {
        if (address == null) {
            throw new UserDomainException("La direccion es obligatoria");
        }
        if (address.getLine1() == null || address.getLine1().isBlank()) {
            throw new UserDomainException("La direccion principal es obligatoria");
        }
        if (address.getCity() == null || address.getCity().isBlank()) {
            throw new UserDomainException("La ciudad es obligatoria");
        }

        var user = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new UserDomainException("Perfil de usuario no encontrado"));

        if (address.isPrimaryAddress()) {
            addressRepository.clearPrimaryAddress(user.getId());
        }

        Address addressToSave = address.toBuilder()
                .userId(user.getId())
                .active(true)
                .build();
        return addressRepository.save(addressToSave);
    }
}
