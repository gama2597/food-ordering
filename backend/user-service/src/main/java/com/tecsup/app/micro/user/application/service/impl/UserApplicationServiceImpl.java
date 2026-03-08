package com.tecsup.app.micro.user.application.service.impl;

import com.tecsup.app.micro.user.application.service.UserApplicationService;
import com.tecsup.app.micro.user.application.usecase.AddAddressToMyProfileUseCase;
import com.tecsup.app.micro.user.application.usecase.DeleteMyAddressUseCase;
import com.tecsup.app.micro.user.application.usecase.GetOrCreateUserProfileUseCase;
import com.tecsup.app.micro.user.application.usecase.ListMyAddressesUseCase;
import com.tecsup.app.micro.user.application.usecase.UpdateMyAddressUseCase;
import com.tecsup.app.micro.user.application.usecase.UpdateMyProfileUseCase;
import com.tecsup.app.micro.user.domain.model.Address;
import com.tecsup.app.micro.user.domain.model.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {

    private final GetOrCreateUserProfileUseCase getOrCreateUserProfileUseCase;
    private final UpdateMyProfileUseCase updateMyProfileUseCase;
    private final ListMyAddressesUseCase listMyAddressesUseCase;
    private final AddAddressToMyProfileUseCase addAddressToMyProfileUseCase;
    private final UpdateMyAddressUseCase updateMyAddressUseCase;
    private final DeleteMyAddressUseCase deleteMyAddressUseCase;

    @Override
    @Transactional
    public UserProfile getOrCreateProfile(String authUserId, String email, String firstName, String lastName) {
        return getOrCreateUserProfileUseCase.execute(authUserId, email, firstName, lastName);
    }

    @Override
    @Transactional
    public UserProfile updateMyProfile(String authUserId, UserProfile profile) {
        return updateMyProfileUseCase.execute(authUserId, profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> listMyAddresses(String authUserId) {
        return listMyAddressesUseCase.execute(authUserId);
    }

    @Override
    @Transactional
    public Address addAddress(String authUserId, Address address) {
        return addAddressToMyProfileUseCase.execute(authUserId, address);
    }

    @Override
    @Transactional
    public Address updateAddress(String authUserId, Long addressId, Address address) {
        return updateMyAddressUseCase.execute(authUserId, addressId, address);
    }

    @Override
    @Transactional
    public void deleteAddress(String authUserId, Long addressId) {
        deleteMyAddressUseCase.execute(authUserId, addressId);
    }
}
