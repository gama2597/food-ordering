# Evidencias QA - Frontend Sprint 5

Validacion UI para seguimiento de entrega integrado en dashboard.

## Precondiciones

- Frontend `http://localhost:4200`.
- Flujos Sprint 4 y Sprint 5 operativos.

## Tabla de evidencias

| Caso | Rol | Flujo UI | Objetivo | Esperado | Actual | Evidencia |
|---|---|---|---|---|---|---|
| 1. Estado ASSIGNED visible | Cliente | Dashboard -> tabla pedidos | Mostrar primera etapa de entrega | Tag `ASSIGNED` visible | OK | Captura S5-FE-01 |
| 2. Estado DELIVERING visible | Cliente | Dashboard -> polling activo | Mostrar etapa en reparto | Tag `DELIVERING` visible | OK | Captura S5-FE-02 |
| 3. Estado DELIVERED visible | Cliente | Dashboard -> fin de flujo | Mostrar entrega completada | Tag `DELIVERED` visible | OK | Captura S5-FE-03 |
| 4. Boton Ver entrega | Cliente | Dashboard -> accion por pedido | Consultar detalle de entrega | Boton habilitado desde ASSIGNED | OK | Captura S5-FE-04 |
| 5. Modal detalle entrega | Cliente | Click en `Ver entrega` | Mostrar detalle en modal | Estado y fechas visibles | OK | Captura S5-FE-05 |
| 6. Coexistencia con modal pago | Cliente | Abrir `Ver pago` y `Ver entrega` | Mantener UX limpia | Ambos modales operan correctamente | OK | Captura S5-FE-06 |
