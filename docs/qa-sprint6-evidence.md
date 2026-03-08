# Evidencias QA - Sprint 6 (Resiliencia y consistencia)

Este documento registra la validacion de resiliencia para eventos Kafka, llamadas REST internas y trazabilidad por correlacion.

## Alcance validado

- Reintentos y backoff en consumidores Kafka (`order`, `payment`, `delivery`).
- Envio a DLQ por topico critico tras agotamiento de reintentos.
- Timeouts y circuit breaker en llamada REST interna `order-service -> catalog-service`.
- Correlacion de logs con `traceId` y `correlationId`.

## Tabla de evidencias

| Caso | Flujo | Objetivo | Esperado | Actual | Evidencia |
|---|---|---|---|---|---|
| A1. Correlacion en flujo feliz | `POST /orders` + `request-payment` con `X-Correlation-Id=qa-s6-001` | Trazabilidad extremo a extremo | `correlationId` visible en logs de servicios involucrados | OK | Captura S6-01 logs correlacion |
| A2. Flujo funcional intacto | Crear pedido y completar pipeline pago/entrega | Confirmar que resiliencia no rompe negocio | Pedido finaliza en `DELIVERED` | OK | Captura S6-02 respuesta orders/deliveries |
| B1. Retry payment.requested | Publicar payload invalido en `payment.requested` | Ver reintentos con backoff en `payment-service` | Logs `[RETRY][payment-service] intento=1..3` | OK | Captura S6-03 logs retry payment |
| B2. DLQ payment.requested | Mismo caso B1 | Confirmar descarte controlado | Mensaje en `payment.requested.dlq` | OK | Captura S6-04 Kafka UI DLQ |
| C1. Retry payment.approved | Publicar payload invalido en `payment.approved` | Ver reintentos en `delivery-service` | Logs `[RETRY][delivery-service] intento=1..3` | OK | Captura S6-05 logs retry delivery |
| C2. DLQ payment.approved | Mismo caso C1 | Confirmar DLQ de delivery input | Mensaje en `payment.approved.dlq` | OK | Captura S6-06 Kafka UI DLQ |
| D1. Retry delivery.assigned | Publicar payload invalido en `delivery.assigned` | Ver reintentos en `order-service` | Logs `[RETRY][order-service] intento=1..3` | OK | Captura S6-07 logs retry order |
| D2. DLQ delivery.assigned | Mismo caso D1 | Confirmar DLQ de order input | Mensaje en `delivery.assigned.dlq` | OK | Captura S6-08 Kafka UI DLQ |
| E1. Circuit breaker abierto | Detener `catalog-service` y crear pedidos repetidos | Fallo rapido y controlado | Error de dominio por catalogo no disponible | OK | Captura S6-09 responses con catalog down |
| E2. Recuperacion de circuito | Levantar `catalog-service` y reintentar tras ventana | Ver retorno a operacion normal | Creacion de pedido vuelve a `201` | OK | Captura S6-10 response recovery |

## Notas de ejecucion

- Backoff configurado: `1s`.
- Reintentos configurados: `3` por mensaje.
- Patron de log de retry: `[RETRY][<service>] intento=n/3 ...`.
- Correlation header usado en pruebas: `X-Correlation-Id: qa-s6-001`.
