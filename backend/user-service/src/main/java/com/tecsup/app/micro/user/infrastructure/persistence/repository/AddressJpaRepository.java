package com.tecsup.app.micro.user.infrastructure.persistence.repository;

import com.tecsup.app.micro.user.infrastructure.persistence.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressJpaRepository extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findByUserIdAndActiveTrue(Long userId);

    Optional<AddressEntity> findByIdAndUserIdAndActiveTrue(Long id, Long userId);

    @Modifying
    @Query("update AddressEntity a set a.primaryAddress = false where a.userId = :userId")
    void clearPrimaryAddressByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("update AddressEntity a set a.active = false, a.primaryAddress = false where a.id = :addressId")
    void deactivateById(@Param("addressId") Long addressId);
}
