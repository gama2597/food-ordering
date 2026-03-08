# Evidencias QA - Sprint 5 (Delivery + seguimiento)

Este documento valida la fase de entrega posterior al pago aprobado.

## Precondiciones

- Servicios arriba: `api-gateway`, `order-service`, `payment-service`, `delivery-service`, `kafka`, `kafka-ui`.
- Pedido creado y pagado (`PAID`) mediante flujo Sprint 4.

## Tabla de evidencias

| Caso | Rol | Request/Flujo | Objetivo | Esperado | Actual | Evidencia |
|---|---|---|---|---|---|---|
| 1. Evento payment.approved | N/A | Kafka topic `payment.approved` | Inicio del flujo de entrega | Evento visible con `orderId` | OK | Captura S5-BE-01 |
| 2. Persistencia de entrega | N/A | DB `deliveries` por `orderId` | Crear seguimiento unico por pedido | Registro unico creado | OK | Captura S5-BE-02 |
| 3. Evento delivery.assigned | N/A | Kafka topic `delivery.assigned` | Marcar pedido asignado | Evento publicado | OK | Captura S5-BE-03 |
| 4. Evento delivery.started | N/A | Kafka topic `delivery.started` | Marcar pedido en reparto | Evento publicado | OK | Captura S5-BE-04 |
| 5. Evento delivery.delivered | N/A | Kafka topic `delivery.delivered` | Marcar pedido entregado | Evento publicado | OK | Captura S5-BE-05 |
| 6. Pedido en ASSIGNED | Customer | `GET /api/v1/orders/{id}` | Ver sincronizacion en order-service | `status=ASSIGNED` (transicion) | OK | Captura S5-BE-06 |
| 7. Pedido en DELIVERING | Customer | `GET /api/v1/orders/{id}` | Ver sincronizacion en order-service | `status=DELIVERING` (transicion) | OK | Captura S5-BE-07 |
| 8. Pedido en DELIVERED | Customer | `GET /api/v1/orders/{id}` | Ver estado final de entrega | `status=DELIVERED` | OK | Captura S5-BE-08 |
| 9. Consulta delivery por orderId | Customer | `GET /api/v1/deliveries/order/{id}` | Ver detalle de entrega | `200` con timestamps y estado final | OK | Captura S5-BE-09 |
| 10. Idempotencia de entrega | N/A | Reprocesar `payment.approved` mismo `orderId` | Evitar duplicados | No se crea nueva entrega | OK | Captura S5-BE-10 |
