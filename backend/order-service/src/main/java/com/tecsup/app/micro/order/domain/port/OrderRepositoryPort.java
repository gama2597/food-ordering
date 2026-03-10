package com.tecsup.app.micro.order.domain.port;

import com.tecsup.app.micro.order.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de Salida: Base de Datos.
 * El dominio dice "Necesito poder guardar y buscar pedidos". 
 * No le importa si usamos JPA, MongoDB o un archivo de texto.
 */
public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findByCustomerAuthUserId(String customerAuthUserId);
}
