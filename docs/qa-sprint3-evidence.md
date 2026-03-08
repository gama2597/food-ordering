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

No incluyen aun validaciones externas por OpenFeign (catalog/user), eventos Kafka ni flujos de pago/entrega.

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
| 1. Docs order via gateway | Publico | `GET /v3/api-docs/order-service` | Verificar ruteo gateway -> docs de order | `200` JSON OpenAPI |  |  |
| 2. Health order-service | Publico | `GET http://localhost:8083/actuator/health` | Confirmar servicio arriba | `200` + `UP` |  |  |
| 3. Crear pedido (feliz) | Customer | `POST /api/v1/orders` | Crear pedido y calcular total/subtotales | `201` + `status=CREATED` |  |  |
| 4. Obtener pedido propio por ID | Customer | `GET /api/v1/orders/{ORDER_ID}` | Validar consulta de pedido propio | `200` + mismo `id` |  |  |
| 5. Listar mis pedidos | Customer | `GET /api/v1/orders/me` | Validar historial por usuario | `200` + lista con `ORDER_ID` |  |  |
| 6. Crear pedido sin token | Sin token | `POST /api/v1/orders` | Validar autenticacion requerida | `401 Unauthorized` |  |  |
| 7. Crear pedido con body vacio | Customer | `POST /api/v1/orders` body `{}` | Validar estructura minima de request | `400 Bad Request` |  |  |
| 8. Crear pedido con quantity=0 | Customer | `POST /api/v1/orders` item invalido | Validar regla cantidad > 0 | `400 Bad Request` |  |  |
| 9. Crear pedido con unitPrice=0 | Customer | `POST /api/v1/orders` item invalido | Validar regla precio > 0 | `400 Bad Request` |  |  |
| 10. Ownership (pedido ajeno) | Customer B | `GET /api/v1/orders/{ORDER_ID_A}` | Validar que no se vea pedido de otro usuario | `400` con mensaje de permisos |  |  |

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
