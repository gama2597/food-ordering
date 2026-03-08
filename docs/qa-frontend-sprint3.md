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
| 1. Login con Keycloak | Cliente | Abrir `http://localhost:4200` e iniciar sesion | Validar autenticacion OIDC | Redireccion a Keycloak y retorno a app autenticada | OK (flujo OIDC completo) | Captura FE-01 + Network `authorize/token` |
| 2. Navegacion protegida | Cliente | Ir a `Dashboard`, `Usuarios`, `Productos` | Validar guard + sesion activa | Navegacion correcta sin errores CORS | OK (rutas protegidas operativas) | Captura FE-02 + Console limpia |
| 3. Perfil usuario | Cliente | Pantalla `Usuarios` | Validar consumo `GET /api/v1/users/me` | Perfil visible en UI | OK (`200` y datos renderizados) | Captura FE-03 + Response `GET /users/me` |
| 4. Direcciones usuario | Cliente | Pantalla `Usuarios` | Validar consumo `GET /api/v1/users/me/addresses` | Tabla de direcciones visible | OK (`200` y tabla cargada) | Captura FE-04 + Response `GET /addresses` |
| 4.1 Agregar direccion | Cliente | Formulario `Nueva direccion` en `Usuarios` | Validar consumo `POST /api/v1/users/me/addresses` | Direccion creada y visible en tabla | OK (`201` y toast de exito) | Captura FE-05 + payload/response POST |
| 4.2 Editar direccion | Cliente | Boton editar en tabla `Usuarios` y guardar cambios | Validar consumo `PUT /api/v1/users/me/addresses/{id}` | Direccion actualizada y reflejada en tabla | OK (`200` y cambios persistidos) | Captura FE-06 + payload/response PUT |
| 4.3 Eliminar direccion | Cliente | Boton eliminar en tabla `Usuarios` y confirmar | Validar consumo `DELETE /api/v1/users/me/addresses/{id}` | Direccion removida de la tabla activa | OK (`204` y fila removida) | Captura FE-07 + request DELETE |
| 5. Catalogo restaurantes | Cliente | Pantalla `Productos` | Validar `GET /api/v1/catalog/restaurants` | Lista de restaurantes cargada | OK (`200` lista visible) | Captura FE-08 + response restaurantes |
| 6. Catalogo productos | Cliente | Seleccionar restaurante en `Productos` | Validar `GET /api/v1/catalog/products/restaurant/{id}` | Tabla de productos cargada | OK (`200` productos visibles) | Captura FE-09 + response productos |
| 7. Crear pedido feliz | Cliente | Seleccionar cantidades y click en `Crear pedido` | Validar flujo UI -> order-service | Toast exito + `Ultimo pedido creado: #ID` | OK (`201` y ID mostrado) | Captura FE-10 + payload/response pedido |
| 7.1 Mis pedidos en dashboard | Cliente | Ver tabla `Mis pedidos recientes` | Validar consumo `GET /api/v1/orders/me` | Pedidos visibles con estado y fecha | OK (`200` con nuevo pedido) | Captura FE-11 + response `/orders/me` |
| 8. Pedido incompleto | Cliente | Click en `Crear pedido` sin cantidades > 0 | Validar regla UI de seleccion minima | Toast warning y sin POST a orders | OK (bloqueo cliente activo) | Captura FE-12 + ausencia POST en Network |
| 9. Publicacion Kafka | Cliente | Tras crear pedido, revisar Kafka UI | Validar evento `order.created` | Topic con mensaje nuevo | OK (mensaje publicado) | Captura FE-13 en Kafka UI topic `order.created` |
| 10. Refresh token con actividad | Cliente | Navegar/usar app por tiempo prolongado | Validar renovacion de token por actividad | Sesion se mantiene activa | OK (refresh automatico) | Captura FE-14 + logs de refresh |
| 11. Expiracion por inactividad | Cliente | Dejar app sin actividad (15 min) | Validar cierre de sesion inactiva | Redireccion a `/session-expired` | OK (redireccion aplicada) | Captura FE-15 ruta `/session-expired` |
| 12. Logout manual | Cliente | Click en `Salir` | Validar cierre voluntario de sesion | Logout en Keycloak y salida de app | OK (logout completo) | Captura FE-16 retorno a login |
| 13. Responsive movil | Cliente | Abrir en viewport movil y usar menu | Validar UX responsive base | Sidebar colapsable y navegacion usable | OK (layout usable en movil) | Captura FE-17 viewport movil |
| 14. Navegacion por rol | Admin/Restaurant/Customer | Iniciar sesion con distintos roles | Validar visibilidad de menu por rol | Items de menu acordes al rol | OK (menus segun roles) | Captura FE-18 comparativa por rol |

## Nota de soporte

- Para evidenciar errores CORS o auth, incluir captura de `Network` y `Console` del navegador cuando aplique.
- Para evidenciar Kafka, incluir captura del mensaje en `http://localhost:8090` topic `order.created`.
