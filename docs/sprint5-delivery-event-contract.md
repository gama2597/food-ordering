# Sprint 5 - Contrato de eventos (Delivery)

Este documento define el contrato minimo para integrar `payment-service`, `delivery-service` y `order-service`.

## Topicos Kafka

- Entrada de `delivery-service`: `payment.approved`
- Salida de `delivery-service`:
  - `delivery.assigned`
  - `delivery.started`
  - `delivery.delivered`

## Convenciones

- Key de mensaje: `orderId` como string.
- Formato: JSON.
- Timestamps: ISO-8601 UTC (`Instant`).
- Versionado inicial de contrato: `eventVersion = 1`.

## Evento de entrada: `payment.approved`

Payload minimo requerido por `delivery-service`:

```json
{
  "orderId": 101,
  "customerAuthUserId": "f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d",
  "occurredAt": "2026-03-08T12:20:35Z"
}
```

Campos requeridos:

- `orderId`
- `customerAuthUserId`

## Evento de salida: `delivery.assigned`

```json
{
  "eventId": "58f3739e-7ec4-47f4-b3e4-d2ca17f53cb8",
  "eventVersion": 1,
  "eventType": "DELIVERY_ASSIGNED",
  "occurredAt": "2026-03-08T12:21:00Z",
  "orderId": 101,
  "deliveryId": 9001,
  "status": "ASSIGNED"
}
```

## Evento de salida: `delivery.started`

```json
{
  "eventId": "9c53dc5c-b438-4767-8f0f-19a45fc24f2f",
  "eventVersion": 1,
  "eventType": "DELIVERY_STARTED",
  "occurredAt": "2026-03-08T12:24:00Z",
  "orderId": 101,
  "deliveryId": 9001,
  "status": "DELIVERING"
}
```

## Evento de salida: `delivery.delivered`

```json
{
  "eventId": "7d2ef6c4-7f7f-4545-96ff-404f1f04158d",
  "eventVersion": 1,
  "eventType": "DELIVERY_DELIVERED",
  "occurredAt": "2026-03-08T12:35:00Z",
  "orderId": 101,
  "deliveryId": 9001,
  "status": "DELIVERED"
}
```

## Reglas de idempotencia

- `delivery-service` debe procesar una sola vez por `orderId`.
- Restriccion recomendada en DB: `unique(order_id)`.
- Si llega duplicado de `payment.approved` para un `orderId` ya procesado:
  - no crear nueva entrega,
  - no duplicar pipeline de eventos.

## Reglas de actualizacion en `order-service`

- `delivery.assigned` -> `ASSIGNED`
- `delivery.started` -> `DELIVERING`
- `delivery.delivered` -> `DELIVERED`

## Criterios de aceptacion Sprint 5

- Un pedido en `PAID` dispara pipeline de delivery.
- `delivery-service` persiste entrega y emite eventos de progreso.
- `order-service` refleja estados de entrega hasta `DELIVERED`.
- Frontend muestra seguimiento de estado final del pedido.
