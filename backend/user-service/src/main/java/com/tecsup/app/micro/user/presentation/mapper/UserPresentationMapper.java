package com.tecsup.app.micro.user.presentation.mapper;

import com.tecsup.app.micro.user.domain.model.Address;
import com.tecsup.app.micro.user.domain.model.UserProfile;
import com.tecsup.app.micro.user.presentation.dto.AddressRequest;
import com.tecsup.app.micro.user.presentation.dto.AddressResponse;
import com.tecsup.app.micro.user.presentation.dto.UpdateMyProfileRequest;
import com.tecsup.app.micro.user.presentation.dto.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserPresentationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authUserId", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserProfile toDomain(UpdateMyProfileRequest request);

    UserProfileResponse toResponse(UserProfile domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "active", ignore = true)
    Address toDomain(AddressRequest request);

    AddressResponse toResponse(Address domain);

    List<AddressResponse> toAddressResponseList(List<Address> addresses);
}
