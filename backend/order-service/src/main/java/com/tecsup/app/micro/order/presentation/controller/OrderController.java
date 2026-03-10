package com.tecsup.app.micro.order.presentation.controller;

import com.tecsup.app.micro.order.application.service.OrderApplicationService;
import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.presentation.dto.CreateOrderRequest;
import com.tecsup.app.micro.order.presentation.dto.OrderResponse;
import com.tecsup.app.micro.order.presentation.mapper.OrderPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST que expone endpoints HTTP del microservicio y delega la logica al servicio de aplicacion.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "API para gestion de pedidos del usuario autenticado")
public class OrderController {

    private final OrderApplicationService orderApplicationService;
    private final OrderPresentationMapper mapper;

    @PostMapping
    @Operation(summary = "Crea un pedido")
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        Order created = orderApplicationService.createOrder(jwt.getSubject(), mapper.toDomain(request));
        return new ResponseEntity<>(mapper.toResponse(created), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un pedido propio por ID")
    public ResponseEntity<OrderResponse> getMyOrderById(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        Order order = orderApplicationService.getMyOrderById(jwt.getSubject(), id);
        return ResponseEntity.ok(mapper.toResponse(order));
    }

    @GetMapping("/me")
    @Operation(summary = "Lista pedidos del usuario autenticado")
    public ResponseEntity<List<OrderResponse>> listMyOrders(@AuthenticationPrincipal Jwt jwt) {
        List<Order> orders = orderApplicationService.listMyOrders(jwt.getSubject());
        return ResponseEntity.ok(mapper.toResponseList(orders));
    }

    @PostMapping("/{id}/request-payment")
    @Operation(summary = "Solicita el pago de un pedido propio")
    public ResponseEntity<OrderResponse> requestMyOrderPayment(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        Order updated = orderApplicationService.requestMyOrderPayment(jwt.getSubject(), id);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }
}

