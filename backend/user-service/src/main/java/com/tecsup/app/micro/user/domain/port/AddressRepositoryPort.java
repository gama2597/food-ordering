package com.tecsup.app.micro.user.domain.port;

import com.tecsup.app.micro.user.domain.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepositoryPort {

    List<Address> findActiveByUserId(Long userId);

    Optional<Address> findActiveByIdAndUserId(Long addressId, Long userId);

    void clearPrimaryAddress(Long userId);

    void deactivateById(Long addressId);

    Address save(Address address);
}
