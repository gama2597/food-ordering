# Evidencias QA - Frontend Sprint 3

Este documento registra la validacion funcional del frontend Angular 18 (standalone + PrimeNG) integrado con `api-gateway` y microservicios actuales.

## Precondiciones

- Servicios levantados: `keycloak`, `api-gateway`, `catalog-service`, `user-service`, `order-service`, `kafka`, `kafka-ui`.
- Frontend levantado con `npm start` en `http://localhost:4200`.

## Datos de prueba sugeridos

- Usuario cliente: `cliente1 / 123456`
- Al menos 1 restaurante y 1 producto disponible en catalogo.

## Tabla de evidencias

| Caso | Rol | Request/Flujo | Objetivo | Esperado | Actual | Evidencia |
|---|---|---|---|---|---|---|
| 1. Login con Keycloak | Cliente | Abrir `http://localhost:4200` e iniciar sesion | Validar autenticacion OIDC | Redireccion a Keycloak y retorno a app autenticada |  |  |
| 2. Navegacion protegida | Cliente | Ir a `Dashboard`, `Usuarios`, `Productos` | Validar guard + sesion activa | Navegacion correcta sin errores CORS |  |  |
| 3. Perfil usuario | Cliente | Pantalla `Usuarios` | Validar consumo `GET /api/v1/users/me` | Perfil visible en UI |  |  |
| 4. Direcciones usuario | Cliente | Pantalla `Usuarios` | Validar consumo `GET /api/v1/users/me/addresses` | Tabla de direcciones visible |  |  |
| 4.1 Agregar direccion | Cliente | Formulario `Nueva direccion` en `Usuarios` | Validar consumo `POST /api/v1/users/me/addresses` | Direccion creada y visible en tabla |  |  |
| 4.2 Editar direccion | Cliente | Boton editar en tabla `Usuarios` y guardar cambios | Validar consumo `PUT /api/v1/users/me/addresses/{id}` | Direccion actualizada y reflejada en tabla |  |  |
| 4.3 Eliminar direccion | Cliente | Boton eliminar en tabla `Usuarios` y confirmar | Validar consumo `DELETE /api/v1/users/me/addresses/{id}` | Direccion removida de la tabla activa |  |  |
| 5. Catalogo restaurantes | Cliente | Pantalla `Productos` | Validar `GET /api/v1/catalog/restaurants` | Lista de restaurantes cargada |  |  |
| 6. Catalogo productos | Cliente | Seleccionar restaurante en `Productos` | Validar `GET /api/v1/catalog/products/restaurant/{id}` | Tabla de productos cargada |  |  |
| 7. Crear pedido feliz | Cliente | Seleccionar cantidades y click en `Crear pedido` | Validar flujo UI -> order-service | Toast exito + `Ultimo pedido creado: #ID` |  |  |
| 7.1 Mis pedidos en dashboard | Cliente | Ver tabla `Mis pedidos recientes` | Validar consumo `GET /api/v1/orders/me` | Pedidos visibles con estado y fecha |  |  |
| 8. Pedido incompleto | Cliente | Click en `Crear pedido` sin cantidades > 0 | Validar regla UI de seleccion minima | Toast warning y sin POST a orders |  |  |
| 9. Publicacion Kafka | Cliente | Tras crear pedido, revisar Kafka UI | Validar evento `order.created` | Topic con mensaje nuevo |  |  |
| 10. Refresh token con actividad | Cliente | Navegar/usar app por tiempo prolongado | Validar renovacion de token por actividad | Sesion se mantiene activa |  |  |
| 11. Expiracion por inactividad | Cliente | Dejar app sin actividad (15 min) | Validar cierre de sesion inactiva | Redireccion a `/session-expired` |  |  |
| 12. Logout manual | Cliente | Click en `Salir` | Validar cierre voluntario de sesion | Logout en Keycloak y salida de app |  |  |
| 13. Responsive movil | Cliente | Abrir en viewport movil y usar menu | Validar UX responsive base | Sidebar colapsable y navegacion usable |  |  |
| 14. Navegacion por rol | Admin/Restaurant/Customer | Iniciar sesion con distintos roles | Validar visibilidad de menu por rol | Items de menu acordes al rol |  |  |

## Nota de soporte

- Para evidenciar errores CORS o auth, incluir captura de `Network` y `Console` del navegador cuando aplique.
- Para evidenciar Kafka, incluir captura del mensaje en `http://localhost:8090` topic `order.created`.
