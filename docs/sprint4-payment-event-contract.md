# Sprint 4 - Contrato de eventos (Payment)

Este documento define el contrato minimo para integrar `order-service` y `payment-service` en Sprint 4.

## Topicos Kafka

- Entrada de `payment-service`: `order.created`
- Salida de `payment-service` (aprobado): `payment.approved`
- Salida de `payment-service` (rechazado): `payment.rejected`

## Convenciones generales

- Key de mensaje: `orderId` (como string) para mantener orden por pedido.
- Formato: JSON.
- Timestamps: ISO-8601 UTC (`Instant`).
- Versionado inicial de contrato: `eventVersion = 1`.

## Evento de entrada: `order.created`

Payload minimo esperado por `payment-service`:

```json
{
  "eventId": "58f3739e-7ec4-47f4-b3e4-d2ca17f53cb8",
  "eventVersion": 1,
  "eventType": "ORDER_CREATED",
  "occurredAt": "2026-03-08T12:10:35Z",
  "orderId": 101,
  "customerAuthUserId": "f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d",
  "amount": 45.50,
  "currency": "PEN"
}
```

Campos requeridos:

- `eventId`
- `orderId`
- `customerAuthUserId`
- `amount`
- `occurredAt`

## Evento de salida: `payment.approved`

```json
{
  "eventId": "f17f5906-f63c-4e40-b8e6-1f1a53bd4b8e",
  "eventVersion": 1,
  "eventType": "PAYMENT_APPROVED",
  "occurredAt": "2026-03-08T12:10:37Z",
  "orderId": 101,
  "paymentId": 5001,
  "customerAuthUserId": "f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d",
  "amount": 45.50,
  "currency": "PEN",
  "status": "APPROVED",
  "reason": "Pago aprobado"
}
```

## Evento de salida: `payment.rejected`

```json
{
  "eventId": "d476cd1d-4af9-4976-a318-fce0f5518ad4",
  "eventVersion": 1,
  "eventType": "PAYMENT_REJECTED",
  "occurredAt": "2026-03-08T12:10:37Z",
  "orderId": 101,
  "paymentId": 5002,
  "customerAuthUserId": "f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d",
  "amount": 45.50,
  "currency": "PEN",
  "status": "REJECTED",
  "reason": "Pago rechazado por simulacion"
}
```

## Reglas de idempotencia

- `payment-service` debe procesar una sola vez por `orderId`.
- Se recomienda restriccion unica en base de datos: `unique(order_id)`.
- Si llega duplicado de `order.created` para un `orderId` ya procesado:
  - no crear un nuevo pago,
  - no publicar un nuevo evento de salida.

## Reglas de actualizacion de `order-service`

- Al recibir `payment.approved`: actualizar pedido a `PAID`.
- Al recibir `payment.rejected`: actualizar pedido a `CANCELLED` (o `PAYMENT_FAILED` si se agrega ese estado).

## Criterios de aceptacion Sprint 4

- Creando un pedido en `order-service`, se publica `order.created`.
- `payment-service` consume el evento y persiste el pago con estado final.
- `payment-service` publica `payment.approved` o `payment.rejected`.
- `order-service` consume resultado y actualiza estado del pedido.
- Hay trazabilidad en Kafka UI y evidencia QA del flujo completo.
