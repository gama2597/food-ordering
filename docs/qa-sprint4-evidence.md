# Evidencias QA - Sprint 4 (Payment + eventos)

Este documento registra la validacion del flujo asincrono de pago bajo demanda entre `order-service` y `payment-service`.

## Precondiciones

- Servicios levantados: `api-gateway`, `order-service`, `payment-service`, `catalog-service`, `user-service`, `kafka`, `kafka-ui`, `keycloak`.
- Topics disponibles: `payment.requested`, `payment.approved`, `payment.rejected`.
- Usuario cliente autenticado con token valido.

## Alcance validado

- Creacion de pedido en estado `CREATED`.
- Solicitud de pago manual via endpoint `request-payment`.
- Cambio a `PAYMENT_PENDING`.
- Procesamiento de pago simulado (aprobado/rechazado) en `payment-service`.
- Publicacion de eventos de salida.
- Actualizacion final de estado de pedido en `order-service`.
- Consulta de pago por `orderId`.

## Tabla de evidencias

| Caso | Rol | Request/Flujo | Objetivo | Esperado | Actual | Evidencia |
|---|---|---|---|---|---|---|
| 1. Crear pedido inicial | Customer | `POST /api/v1/orders` | Registrar pedido sin pago automatico | `201` con `status=CREATED` | OK (`201`, `CREATED`) | Captura S4-BE-01 request/response |
| 2. Solicitar pago manual | Customer | `POST /api/v1/orders/{id}/request-payment` | Disparar pago bajo demanda | `200` con `status=PAYMENT_PENDING` | OK (`200`, `PAYMENT_PENDING`) | Captura S4-BE-02 request/response |
| 3. Evento payment.requested | N/A | Kafka UI topic `payment.requested` | Verificar publicacion del evento de solicitud | Mensaje visible con `orderId` | OK (mensaje visible) | Captura S4-BE-03 Kafka UI |
| 4. Pago aprobado (simulado) | N/A | `payment-service` consume evento de monto bajo | Validar aprobacion automatica simulada | Evento `payment.approved` publicado | OK (evento publicado) | Captura S4-BE-04 Kafka UI |
| 5. Pedido actualizado a PAID | Customer | `GET /api/v1/orders/{id}` | Confirmar impacto en order-service | `200` con `status=PAID` | OK (`200`, `PAID`) | Captura S4-BE-05 response |
| 6. Consulta de pago aprobado | Customer | `GET /api/v1/payments/order/{id}` | Ver detalle del pago asociado | `200` con `status=APPROVED` | OK (`200`, `APPROVED`) | Captura S4-BE-06 response |
| 7. Pago rechazado (simulado) | N/A | Repetir flujo con monto alto | Validar rechazo automatizado simulado | Evento `payment.rejected` publicado | OK (evento publicado) | Captura S4-BE-07 Kafka UI |
| 8. Pedido actualizado a CANCELLED | Customer | `GET /api/v1/orders/{id_rechazado}` | Confirmar estado final tras rechazo | `200` con `status=CANCELLED` | OK (`200`, `CANCELLED`) | Captura S4-BE-08 response |
| 9. Consulta de pago rechazado | Customer | `GET /api/v1/payments/order/{id_rechazado}` | Ver detalle del rechazo | `200` con `status=REJECTED` | OK (`200`, `REJECTED`) | Captura S4-BE-09 response |
| 10. Idempotencia por orderId | N/A | Reintento de solicitud/evento para mismo pedido | Evitar doble pago | No se crea nuevo pago para mismo `orderId` | OK (sin duplicados) | Captura S4-BE-10 DB/Kafka |
