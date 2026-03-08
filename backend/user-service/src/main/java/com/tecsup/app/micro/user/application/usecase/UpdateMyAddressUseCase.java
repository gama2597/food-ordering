package com.tecsup.app.micro.user.application.usecase;

import com.tecsup.app.micro.user.domain.exception.UserDomainException;
import com.tecsup.app.micro.user.domain.model.Address;
import com.tecsup.app.micro.user.domain.port.AddressRepositoryPort;
import com.tecsup.app.micro.user.domain.port.UserProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateMyAddressUseCase {

    private final AddressRepositoryPort addressRepository;
    private final UserProfileRepositoryPort userProfileRepository;

    public Address execute(String authUserId, Long addressId, Address update) {
        if (addressId == null) {
            throw new UserDomainException("El ID de la direccion es obligatorio");
        }
        if (update == null) {
            throw new UserDomainException("La direccion es obligatoria");
        }
        if (update.getLine1() == null || update.getLine1().isBlank()) {
            throw new UserDomainException("La direccion principal es obligatoria");
        }
        if (update.getCity() == null || update.getCity().isBlank()) {
            throw new UserDomainException("La ciudad es obligatoria");
        }

        var user = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new UserDomainException("Perfil de usuario no encontrado"));

        Address current = addressRepository.findActiveByIdAndUserId(addressId, user.getId())
                .orElseThrow(() -> new UserDomainException("Direccion no encontrada"));

        if (update.isPrimaryAddress()) {
            addressRepository.clearPrimaryAddress(user.getId());
        }

        Address toSave = current.toBuilder()
                .label(update.getLabel())
                .line1(update.getLine1())
                .line2(update.getLine2())
                .district(update.getDistrict())
                .city(update.getCity())
                .reference(update.getReference())
                .primaryAddress(update.isPrimaryAddress())
                .build();

        return addressRepository.save(toSave);
    }
}
