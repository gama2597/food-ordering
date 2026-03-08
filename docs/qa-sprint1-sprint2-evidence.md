# Evidencias QA - Sprint 1 y Sprint 2

Este documento sirve para registrar resultados de pruebas funcionales, seguridad por rol y validaciones de `api-gateway`, `catalog-service` y `user-service`.

## Variables usadas

- `KEYCLOAK_URL = http://localhost:8080`
- `GATEWAY_URL = http://localhost:8000`
- `admin_token`, `restaurant_token`, `customer_token`
- `CATALOG_ID` (se guarda desde el caso de crear restaurante)

## Matriz de roles por endpoint

| Endpoint | Admin | Restaurant | Customer | Courier |
|---|---|---|---|---|
| `GET /api/v1/users/me` | Si | Si | Si | Si |
| `PUT /api/v1/users/me` | Si | Si | Si | Si |
| `GET /api/v1/users/me/addresses` | Si | Si | Si | Si |
| `POST /api/v1/users/me/addresses` | Si | Si | Si | Si |
| `POST /api/v1/catalog/restaurants` | Si | No | No | No |
| `GET /api/v1/catalog/restaurants` | Si | Si | Si | Si |
| `GET /api/v1/catalog/restaurants/{id}` | Si | Si | Si | Si |
| `POST /api/v1/catalog/products` | Si | Si | No | No |
| `GET /api/v1/catalog/products/restaurant/{restaurantId}` | Si | Si | Si | Si |

## Tabla de evidencias

> Completa la columna **Actual** y **Evidencia** al ejecutar en Postman.

| Caso | Rol | Request | Expected | Actual | Evidencia |
|---|---|---|---|---|---|
| 1. Token admin | N/A | `POST /realms/food-ordering/protocol/openid-connect/token` (admin1) | `200` + `access_token` |  |  |
| 2. Token restaurant | N/A | `POST /realms/food-ordering/protocol/openid-connect/token` (restaurante1) | `200` + `access_token` |  |  |
| 3. Token customer | N/A | `POST /realms/food-ordering/protocol/openid-connect/token` (cliente1) | `200` + `access_token` |  |  |
| 4. Health gateway | Publico | `GET /actuator/health` | `200` + `UP` |  |  |
| 5. OpenAPI catalog via gateway | Publico | `GET /v3/api-docs/catalog-service` | `200` JSON OpenAPI |  |  |
| 6. OpenAPI user via gateway | Publico | `GET /v3/api-docs/user-service` | `200` JSON OpenAPI |  |  |
| 7. Get/Create profile | Customer | `GET /api/v1/users/me` | `200` perfil del usuario |  |  |
| 8. Update profile | Customer | `PUT /api/v1/users/me` | `200` perfil actualizado |  |  |
| 9. Add address | Customer | `POST /api/v1/users/me/addresses` | `201` direccion creada |  |  |
| 10. List addresses | Customer | `GET /api/v1/users/me/addresses` | `200` lista direcciones |  |  |
| 11. Users sin token | Sin token | `GET /api/v1/users/me` | `401 Unauthorized` |  |  |
| 12. Update profile invalido | Customer | `PUT /api/v1/users/me` body invalido | `400` + `validationErrors` |  |  |
| 13. Add address invalida | Customer | `POST /api/v1/users/me/addresses` body invalido | `400` + `validationErrors` |  |  |
| 14. Crear restaurante | Admin | `POST /api/v1/catalog/restaurants` | `201` + restaurante activo |  |  |
| 15. Listar restaurantes | Customer | `GET /api/v1/catalog/restaurants` | `200` + lista |  |  |
| 16. Crear producto | Restaurant | `POST /api/v1/catalog/products` | `201` + producto disponible |  |  |
| 17. Listar productos por restaurante | Customer | `GET /api/v1/catalog/products/restaurant/{id}` | `200` + lista |  |  |
| 18. Customer crea producto (negativa) | Customer | `POST /api/v1/catalog/products` | `403 Forbidden` |  |  |
| 19. Precio invalido (negativa) | Restaurant | `POST /api/v1/catalog/products` price `0` | `400 Bad Request` |  |  |
| 20. Restaurante inexistente (negativa) | Restaurant | `POST /api/v1/catalog/products` id inexistente | `400 Bad Request` |  |  |
| 21. Regresion final users | Customer | `GET /api/v1/users/me` | `200` |  |  |
| 22. Regresion final catalog | Customer | `GET /api/v1/catalog/restaurants` | `200` |  |  |
| 23. Regresion docs catalog | Publico | `GET /v3/api-docs/catalog-service` | `200` |  |  |
| 24. Regresion docs user | Publico | `GET /v3/api-docs/user-service` | `200` |  |  |

## Nota para Postman (guardar `CATALOG_ID`)

En el request de crear restaurante, en la pestaña **Tests** usa:

```javascript
pm.test("Status is 201", function () {
  pm.response.to.have.status(201);
});

const data = pm.response.json();

pm.test("Response has id", function () {
  pm.expect(data.id).to.exist;
});

pm.environment.set("CATALOG_ID", data.id);
```
