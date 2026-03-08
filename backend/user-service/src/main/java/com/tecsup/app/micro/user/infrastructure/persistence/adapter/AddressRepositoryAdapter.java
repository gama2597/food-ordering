package com.tecsup.app.micro.user.infrastructure.persistence.adapter;

import com.tecsup.app.micro.user.domain.model.Address;
import com.tecsup.app.micro.user.domain.port.AddressRepositoryPort;
import com.tecsup.app.micro.user.infrastructure.persistence.mapper.AddressPersistenceMapper;
import com.tecsup.app.micro.user.infrastructure.persistence.repository.AddressJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryAdapter implements AddressRepositoryPort {

    private final AddressJpaRepository addressJpaRepository;
    private final AddressPersistenceMapper mapper;

    @Override
    public List<Address> findActiveByUserId(Long userId) {
        return mapper.toDomainList(addressJpaRepository.findByUserIdAndActiveTrue(userId));
    }

    @Override
    public Optional<Address> findActiveByIdAndUserId(Long addressId, Long userId) {
        return addressJpaRepository.findByIdAndUserIdAndActiveTrue(addressId, userId)
                .map(mapper::toDomain);
    }

    @Override
    public void clearPrimaryAddress(Long userId) {
        addressJpaRepository.clearPrimaryAddressByUserId(userId);
    }

    @Override
    public void deactivateById(Long addressId) {
        addressJpaRepository.deactivateById(addressId);
    }

    @Override
    public Address save(Address address) {
        return mapper.toDomain(addressJpaRepository.save(mapper.toEntity(address)));
    }
}
