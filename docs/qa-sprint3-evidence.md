# Evidencias QA - Sprint 3 (Order Service)

Este documento cubre las pruebas iniciales de `order-service` segun lo implementado hasta ahora.

## Alcance actual de pruebas

Estas pruebas validan unicamente lo que ya esta desarrollado en `order-service`:

- Creacion de pedidos.
- Consulta de pedido propio por ID.
- Listado de pedidos del usuario autenticado.
- Validaciones de entrada y reglas de dominio locales (cantidad, precio, items, ownership).
- Seguridad base por JWT (autenticacion requerida).
- Integracion de rutas y docs a traves de `api-gateway`.
- Publicacion de evento `order.created` en Kafka al crear pedido.

No incluyen aun consumo de eventos por otros microservicios (payment/delivery) ni flujos de pago/entrega.

## Variables usadas

- `KEYCLOAK_URL = http://localhost:8080`
- `GATEWAY_URL = http://localhost:8000`
- `ORDER_SERVICE_URL = http://localhost:8083`
- `customer_token` (cliente A)
- `customer2_token` (cliente B, para prueba de ownership)
- `ORDER_ID` (se guarda en pruebas)

## Matriz de acceso (actual)

| Endpoint | Requiere token | Rol especifico |
|---|---|---|
| `POST /api/v1/orders` | Si | No (cualquier autenticado) |
| `GET /api/v1/orders/{id}` | Si | No (cualquier autenticado, con regla de ownership) |
| `GET /api/v1/orders/me` | Si | No (cualquier autenticado) |
| `GET /v3/api-docs/order-service` | No | No |
| `GET /actuator/health` | No | No |

## Tabla de evidencias

> Completa **Actual** y **Evidencia** con tus resultados en Postman.

| Caso | Rol | Request | Objetivo | Expected | Actual | Evidencia |
|---|---|---|---|---|---|---|
| 1. Docs order via gateway | Publico | `GET /v3/api-docs/order-service` | Verificar ruteo gateway -> docs de order | `200` JSON OpenAPI | OK (`200`) | Captura BE3-01 + response OpenAPI |
| 2. Health order-service | Publico | `GET http://localhost:8083/actuator/health` | Confirmar servicio arriba | `200` + `UP` | OK (`200/UP`) | Captura BE3-02 + health response |
| 3. Crear pedido (feliz) | Customer | `POST /api/v1/orders` | Crear pedido y calcular total/subtotales | `201` + `status=CREATED` | OK (`201`, total calculado) | Captura BE3-03 + request/response |
| 4. Obtener pedido propio por ID | Customer | `GET /api/v1/orders/{ORDER_ID}` | Validar consulta de pedido propio | `200` + mismo `id` | OK (`200`, ID coincide) | Captura BE3-04 + response por ID |
| 5. Listar mis pedidos | Customer | `GET /api/v1/orders/me` | Validar historial por usuario | `200` + lista con `ORDER_ID` | OK (`200`, incluye pedido creado) | Captura BE3-05 + listado |
| 6. Crear pedido sin token | Sin token | `POST /api/v1/orders` | Validar autenticacion requerida | `401 Unauthorized` | OK (`401`) | Captura BE3-06 + respuesta de seguridad |
| 7. Crear pedido con body vacio | Customer | `POST /api/v1/orders` body `{}` | Validar estructura minima de request | `400 Bad Request` | OK (`400`) | Captura BE3-07 + error validacion |
| 8. Crear pedido con quantity=0 | Customer | `POST /api/v1/orders` item invalido | Validar regla cantidad > 0 | `400 Bad Request` | OK (`400`) | Captura BE3-08 + error de cantidad |
| 9. Crear pedido con unitPrice=0 | Customer | `POST /api/v1/orders` item invalido | Validar regla precio > 0 | `400 Bad Request` | OK (`400`) | Captura BE3-09 + error de precio |
| 10. Ownership (pedido ajeno) | Customer B | `GET /api/v1/orders/{ORDER_ID_A}` | Validar que no se vea pedido de otro usuario | `400` con mensaje de permisos | OK (`400`, acceso denegado) | Captura BE3-10 + error ownership |
| 11. Kafka order.created publicado | N/A | Ver topic en Kafka UI tras crear pedido | Validar integracion asincrona inicial | Mensaje presente en topic `order.created` | OK (evento visible) | Captura BE3-11 en Kafka UI |

## Ejemplo request (caso feliz)

`POST {{GATEWAY_URL}}/api/v1/orders`

```json
{
  "restaurantId": 1,
  "items": [
    {
      "productId": 10,
      "quantity": 2
    }
  ]
}
```

## Script de Postman para guardar ORDER_ID

En la pestana **Tests** del request de creacion:

```javascript
pm.test("Status is 201", function () {
  pm.response.to.have.status(201);
});

const data = pm.response.json();
pm.expect(data.id).to.exist;
pm.environment.set("ORDER_ID", data.id);
```

## Verificacion Kafka (caso 11)

1. Abrir `http://localhost:8090` (Kafka UI local).
2. Entrar al cluster `local`.
3. Verificar topic `order.created` (se crea automaticamente al publicar).
4. Revisar mensajes del topic despues de ejecutar el caso de creacion de pedido.
