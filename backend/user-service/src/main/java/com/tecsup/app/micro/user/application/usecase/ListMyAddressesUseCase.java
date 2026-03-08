package com.tecsup.app.micro.user.application.usecase;

import com.tecsup.app.micro.user.domain.exception.UserDomainException;
import com.tecsup.app.micro.user.domain.model.Address;
import com.tecsup.app.micro.user.domain.port.AddressRepositoryPort;
import com.tecsup.app.micro.user.domain.port.UserProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListMyAddressesUseCase {

    private final AddressRepositoryPort addressRepository;
    private final UserProfileRepositoryPort userProfileRepository;

    public List<Address> execute(String authUserId) {
        var user = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new UserDomainException("Perfil de usuario no encontrado"));
        return addressRepository.findActiveByUserId(user.getId());
    }
}
