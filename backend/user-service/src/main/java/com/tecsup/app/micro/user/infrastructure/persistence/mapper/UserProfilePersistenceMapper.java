package com.tecsup.app.micro.user.infrastructure.persistence.mapper;

import com.tecsup.app.micro.user.domain.model.UserProfile;
import com.tecsup.app.micro.user.infrastructure.persistence.entity.UserProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfilePersistenceMapper {

    UserProfileEntity toEntity(UserProfile domain);

    UserProfile toDomain(UserProfileEntity entity);
}
