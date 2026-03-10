package com.tecsup.app.micro.user.application.service;

import com.tecsup.app.micro.user.domain.model.Address;
import com.tecsup.app.micro.user.domain.model.UserProfile;

import java.util.List;

/**
 * Servicio de aplicacion que orquesta casos de uso y centraliza el flujo de negocio para la capa de presentacion.
 */
public interface UserApplicationService {

    UserProfile getOrCreateProfile(String authUserId, String email, String firstName, String lastName);

    UserProfile updateMyProfile(String authUserId, UserProfile profile);

    List<Address> listMyAddresses(String authUserId);

    Address addAddress(String authUserId, Address address);

    Address updateAddress(String authUserId, Long addressId, Address address);

    void deleteAddress(String authUserId, Long addressId);
}

