# Evidencias QA - Frontend Sprint 4

Este documento registra la validacion funcional de frontend para el flujo de pago bajo demanda y mejoras UX del Sprint 4.

## Precondiciones

- Frontend en `http://localhost:4200`.
- Gateway y microservicios activos (`catalog`, `user`, `order`, `payment`).
- Usuario `cliente1` autenticado.

## Tabla de evidencias

| Caso | Rol | Flujo UI | Objetivo | Esperado | Actual | Evidencia |
|---|---|---|---|---|---|---|
| 1. Pedido creado en CREATED | Cliente | Productos -> Crear pedido | Validar nuevo flujo sin pago automatico | Toast de exito y pedido en `CREATED` | OK | Captura S4-FE-01 |
| 2. Solicitar pago desde dashboard | Cliente | Dashboard -> boton `Pagar` | Disparar pago manual | Pedido cambia a `PAYMENT_PENDING` | OK | Captura S4-FE-02 |
| 3. Polling de estado | Cliente | Esperar en dashboard tras solicitar pago | Actualizar estado sin recargar pagina | Pedido pasa a `PAID` o `CANCELLED` | OK | Captura S4-FE-03 |
| 4. Columna de pago en tabla | Cliente | Ver tabla `Mis pedidos recientes` | Mostrar estado de pago resumido | `Procesando`/`APPROVED`/`REJECTED` visible | OK | Captura S4-FE-04 |
| 5. Modal detalle de pago | Cliente | Dashboard -> boton `Ver pago` | Mostrar detalle en modal (no inline) | Modal con estado, monto, motivo y fecha | OK | Captura S4-FE-05 |
| 6. Paginacion de pedidos | Cliente | Dashboard con muchos pedidos | Evitar lista extensa y mejorar navegacion | Tabla paginada y cambio de pagina correcto | OK | Captura S4-FE-06 |
| 7. Paginacion de restaurantes | Cliente/Admin | Productos -> tabla restaurantes | Escalabilidad visual con muchos registros | Tabla paginada y seleccion funcional | OK | Captura S4-FE-07 |
| 8. Paginacion de productos | Cliente/Admin | Productos -> tabla productos | Escalabilidad visual del menu | Tabla paginada y cantidades correctas | OK | Captura S4-FE-08 |
| 9. Eliminar restaurante (admin) | Admin | Productos -> boton `Eliminar` restaurante | Desactivar restaurante desde UI | `204`, toast y refresco de lista | OK | Captura S4-FE-09 |
| 10. Eliminar producto (admin/restaurant) | Admin/Restaurant | Productos -> boton `Eliminar` producto | Desactivar producto desde UI | `204`, toast y producto removido de vista | OK | Captura S4-FE-10 |
