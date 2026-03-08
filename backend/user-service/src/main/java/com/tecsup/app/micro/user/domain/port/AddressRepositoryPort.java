package com.tecsup.app.micro.user.domain.port;

import com.tecsup.app.micro.user.domain.model.Address;

import java.util.List;

public interface AddressRepositoryPort {

    List<Address> findActiveByUserId(Long userId);

    void clearPrimaryAddress(Long userId);

    Address save(Address address);
}
