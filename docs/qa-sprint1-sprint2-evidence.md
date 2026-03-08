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
| `PUT /api/v1/users/me/addresses/{id}` | Si | Si | Si | Si |
| `DELETE /api/v1/users/me/addresses/{id}` | Si | Si | Si | Si |
| `POST /api/v1/catalog/restaurants` | Si | No | No | No |
| `GET /api/v1/catalog/restaurants` | Si | Si | Si | Si |
| `GET /api/v1/catalog/restaurants/{id}` | Si | Si | Si | Si |
| `POST /api/v1/catalog/products` | Si | Si | No | No |
| `GET /api/v1/catalog/products/restaurant/{restaurantId}` | Si | Si | Si | Si |

## Tabla de evidencias

> Completa la columna **Actual** y **Evidencia** al ejecutar en Postman.

| Caso | Rol | Request | Expected | Actual | Evidencia |
|---|---|---|---|---|---|
| 1. Token admin | N/A | `POST /realms/food-ordering/protocol/openid-connect/token` (admin1) | `200` + `access_token` | OK (`200`) | Captura BE12-01 token admin |
| 2. Token restaurant | N/A | `POST /realms/food-ordering/protocol/openid-connect/token` (restaurante1) | `200` + `access_token` | OK (`200`) | Captura BE12-02 token restaurant |
| 3. Token customer | N/A | `POST /realms/food-ordering/protocol/openid-connect/token` (cliente1) | `200` + `access_token` | OK (`200`) | Captura BE12-03 token customer |
| 4. Health gateway | Publico | `GET /actuator/health` | `200` + `UP` | OK (`200/UP`) | Captura BE12-04 health gateway |
| 5. OpenAPI catalog via gateway | Publico | `GET /v3/api-docs/catalog-service` | `200` JSON OpenAPI | OK (`200`) | Captura BE12-05 docs catalog |
| 6. OpenAPI user via gateway | Publico | `GET /v3/api-docs/user-service` | `200` JSON OpenAPI | OK (`200`) | Captura BE12-06 docs user |
| 7. Get/Create profile | Customer | `GET /api/v1/users/me` | `200` perfil del usuario | OK (`200`) | Captura BE12-07 perfil |
| 8. Update profile | Customer | `PUT /api/v1/users/me` | `200` perfil actualizado | OK (`200`) | Captura BE12-08 update perfil |
| 9. Add address | Customer | `POST /api/v1/users/me/addresses` | `201` direccion creada | OK (`201`) | Captura BE12-09 add address |
| 10. List addresses | Customer | `GET /api/v1/users/me/addresses` | `200` lista direcciones | OK (`200`) | Captura BE12-10 list addresses |
| 10.1 Update address | Customer | `PUT /api/v1/users/me/addresses/{id}` | `200` direccion actualizada | OK (`200`) | Captura BE12-10A update address |
| 10.2 Delete address | Customer | `DELETE /api/v1/users/me/addresses/{id}` | `204` direccion inactivada | OK (`204`) | Captura BE12-10B delete address |
| 11. Users sin token | Sin token | `GET /api/v1/users/me` | `401 Unauthorized` | OK (`401`) | Captura BE12-11 users sin token |
| 12. Update profile invalido | Customer | `PUT /api/v1/users/me` body invalido | `400` + `validationErrors` | OK (`400`) | Captura BE12-12 validacion perfil |
| 13. Add address invalida | Customer | `POST /api/v1/users/me/addresses` body invalido | `400` + `validationErrors` | OK (`400`) | Captura BE12-13 validacion address |
| 14. Crear restaurante | Admin | `POST /api/v1/catalog/restaurants` | `201` + restaurante activo | OK (`201`) | Captura BE12-14 crear restaurante |
| 15. Listar restaurantes | Customer | `GET /api/v1/catalog/restaurants` | `200` + lista | OK (`200`) | Captura BE12-15 listar restaurantes |
| 16. Crear producto | Restaurant | `POST /api/v1/catalog/products` | `201` + producto disponible | OK (`201`) | Captura BE12-16 crear producto |
| 17. Listar productos por restaurante | Customer | `GET /api/v1/catalog/products/restaurant/{id}` | `200` + lista | OK (`200`) | Captura BE12-17 listar productos |
| 18. Customer crea producto (negativa) | Customer | `POST /api/v1/catalog/products` | `403 Forbidden` | OK (`403`) | Captura BE12-18 negativa rol |
| 19. Precio invalido (negativa) | Restaurant | `POST /api/v1/catalog/products` price `0` | `400 Bad Request` | OK (`400`) | Captura BE12-19 precio invalido |
| 20. Restaurante inexistente (negativa) | Restaurant | `POST /api/v1/catalog/products` id inexistente | `400 Bad Request` | OK (`400`) | Captura BE12-20 restaurante inexistente |
| 21. Regresion final users | Customer | `GET /api/v1/users/me` | `200` | OK (`200`) | Captura BE12-21 regresion users |
| 22. Regresion final catalog | Customer | `GET /api/v1/catalog/restaurants` | `200` | OK (`200`) | Captura BE12-22 regresion catalog |
| 23. Regresion docs catalog | Publico | `GET /v3/api-docs/catalog-service` | `200` | OK (`200`) | Captura BE12-23 docs catalog |
| 24. Regresion docs user | Publico | `GET /v3/api-docs/user-service` | `200` | OK (`200`) | Captura BE12-24 docs user |

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
