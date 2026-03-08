package com.tecsup.app.micro.delivery.presentation.controller;

import com.tecsup.app.micro.delivery.application.service.DeliveryApplicationService;
import com.tecsup.app.micro.delivery.presentation.dto.DeliveryResponse;
import com.tecsup.app.micro.delivery.presentation.mapper.DeliveryPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
@Tag(name = "Deliveries", description = "API para consultas de entrega")
public class DeliveryController {

    private final DeliveryApplicationService deliveryApplicationService;
    private final DeliveryPresentationMapper mapper;

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Obtiene la entrega por orderId")
    public ResponseEntity<DeliveryResponse> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(mapper.toResponse(deliveryApplicationService.getDeliveryByOrderId(orderId)));
    }
}
