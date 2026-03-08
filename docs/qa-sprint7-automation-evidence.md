# Evidencias QA - Sprint 7 (Calidad y pruebas automatizadas)

Este documento registra la validacion automatizada de calidad con pruebas unitarias e integracion reproducibles.

## Alcance automatizado

- Unit tests en casos de uso criticos de `order-service`, `payment-service` y `delivery-service`.
- Integration tests con Testcontainers (Postgres + Kafka) en `order-service` y `payment-service`.
- Contract tests de eventos Kafka para compatibilidad productor/consumidor entre `payment`, `delivery` y `order`.
- Escenarios E2E previamente validados y mantenidos (flujo feliz, pago rechazado, restaurante inactivo).

## Ejecucion de pruebas

| Servicio | Comando | Resultado |
|---|---|---|
| order-service | `./mvnw test` | OK (unit + integration, integration con Docker condicional) |
| payment-service | `./mvnw test` | OK (unit + integration, integration con Docker condicional) |
| delivery-service | `mvn -f backend/delivery-service/pom.xml test` | OK (unit + context) |

### Nota de ejecucion local

- Si aparece error de compilacion en tests despues de cambios estructurales, ejecutar una vez `clean test` por servicio para regenerar clases y metadatos antes de volver a `test`.

## Cobertura por casos de uso (unit)

### Order service

- `CreateOrderUseCaseTest` (existente)
- `GetMyOrderByIdUseCaseTest` (existente)
- `RequestMyOrderPaymentUseCaseTest` (nuevo)
- `ApplyPaymentResultUseCaseTest` (nuevo)
- `ApplyDeliveryProgressUseCaseTest` (nuevo)

### Payment service

- `ProcessOrderCreatedUseCaseTest` (nuevo)
- `GetPaymentByOrderIdUseCaseTest` (nuevo)

### Delivery service

- `ProcessPaymentApprovedUseCaseTest` (nuevo)
- `GetDeliveryByOrderIdUseCaseTest` (nuevo)

## Integration con Testcontainers

- `OrderServiceContainersIntegrationTest`:
  - levanta Postgres + Kafka con Testcontainers,
  - valida persistencia real de pedido e items en DB del contenedor.
- `PaymentServiceContainersIntegrationTest`:
  - levanta Postgres + Kafka con Testcontainers,
  - valida persistencia real de pago y consulta por `orderId`.

### Nota de entorno

- Los tests de contenedor estan marcados con `@Testcontainers(disabledWithoutDocker = true)`.
- Si Docker no esta disponible, esos tests se reportan como `Skipped` sin romper pipeline local.

## Contract tests Kafka

- `OrderConsumerContractCompatibilityTest`:
  - valida que `order-service` deserializa eventos extendidos de `payment` y `delivery` sin romper compatibilidad.
- `PaymentEventContractTest`:
  - valida contrato de entrada `payment.requested` y forma de salida para `payment.approved` y `payment.rejected`.
- `DeliveryEventContractTest`:
  - valida contrato de entrada `payment.approved` y forma de salida para `delivery.assigned`, `delivery.started`, `delivery.delivered`.

## Escenarios E2E referenciados (regresion)

- Flujo feliz completo: `CREATED -> PAYMENT_PENDING -> PAID -> ASSIGNED -> DELIVERING -> DELIVERED`.
- Error 1: pago rechazado (estado final de pedido en rama de rechazo).
- Error 2: restaurante inactivo/no disponible al crear pedido.

## Evidencia recomendada

- Captura S7-01: salida `order-service` tests OK.
- Captura S7-02: salida `payment-service` tests OK.
- Captura S7-03: salida `delivery-service` tests OK.
- Captura S7-04: log de testcontainers (running o skipped por Docker no disponible).
