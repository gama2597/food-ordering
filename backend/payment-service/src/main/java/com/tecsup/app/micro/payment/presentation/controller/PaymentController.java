package com.tecsup.app.micro.payment.presentation.controller;

import com.tecsup.app.micro.payment.application.service.PaymentApplicationService;
import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.presentation.dto.PaymentResponse;
import com.tecsup.app.micro.payment.presentation.mapper.PaymentPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "API para consultas de pagos")
public class PaymentController {

    private final PaymentApplicationService paymentApplicationService;
    private final PaymentPresentationMapper mapper;

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Obtiene el pago por orderId")
    public ResponseEntity<PaymentResponse> getByOrderId(@PathVariable Long orderId) {
        Payment payment = paymentApplicationService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(mapper.toResponse(payment));
    }
}
