# Food Ordering Microservices

[![CI](https://github.com/gama2597/food-ordering/actions/workflows/ci-backend.yml/badge.svg)](https://github.com/gama2597/food-ordering/actions/workflows/ci-backend.yml)

Proyecto de arquitectura de software basado en microservicios para pedidos de comida.

## Servicios backend

- `backend/api-gateway`
- `backend/catalog-service`
- `backend/order-service`
- `backend/payment-service`
- `backend/delivery-service`
- `backend/user-profile-service`

## Calidad

- Pipeline automatizado en GitHub Actions para pruebas backend en `master` y pull requests.
- Evidencia Sprint 7 en `docs/qa-sprint7-automation-evidence.md`.

## Documentacion

- Evidencias QA en `docs/`.
- Contratos de eventos en `docs/sprint4-payment-event-contract.md` y `docs/sprint5-delivery-event-contract.md`.
- Guia Kubernetes local en `k8s/README.md`.
- Documentacion final de arquitectura y operacion en `docs/final-project-documentation.md`.
- Coleccion Postman local en `docs/postman/food-ordering-local.postman_collection.json`.

## Levantamiento local (Docker + Kubernetes)

Este es el flujo validado para levantar todo en local de forma estable.

### 1) Levantar infraestructura base

```powershell
docker compose -f infra/local/docker-compose.yml up -d
docker compose -f infra/local/docker-compose.yml ps
```

Observabilidad lista para usar:

- Grafana provisiona automaticamente datasource (`Prometheus`) y dashboard (`Food Ordering - Basic Observability`) desde `config/grafana/provisioning` y `config/grafana/dashboards`.
- Si no aparece el dashboard tras cambios locales, recrea Grafana:

```powershell
docker compose -f infra/local/docker-compose.yml up -d --force-recreate grafana
```

Importante para Kafka en `infra/local/docker-compose.yml`:

```yaml
KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka:29092,EXTERNAL://host.docker.internal:${KAFKA_PORT}
```

Si cambiaste esa linea, recrea Kafka:

```powershell
docker compose -f infra/local/docker-compose.yml up -d --force-recreate kafka kafka-ui
```

### 2) Construir imagenes de microservicios y frontend

```powershell
./backend/catalog-service/mvnw -f backend/catalog-service/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/catalog-service:local
./backend/user-service/mvnw -f backend/user-service/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/user-service:local
./backend/order-service/mvnw -f backend/order-service/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/order-service:local
./backend/payment-service/mvnw -f backend/payment-service/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/payment-service:local
./backend/delivery-service/mvnw -f backend/delivery-service/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/delivery-service:local
./backend/api-gateway/mvnw -f backend/api-gateway/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/api-gateway:local
docker build -t food-ordering/frontend:local frontend
docker images | findstr food-ordering
```

### 3) Preparar Kubernetes

```powershell
kubectl create namespace food-ordering
kubectl apply -f k8s/base/configmap.yaml -n food-ordering
kubectl apply -f k8s/base/secret.yaml -n food-ordering
```

### 4) Desplegar uno por uno (orden recomendado)

1. `catalog-service`
2. `user-service`
3. `order-service`
4. `payment-service`
5. `delivery-service`
6. `api-gateway`
7. `frontend`

Patron por servicio:

```powershell
kubectl apply -f k8s/base/<servicio>/service.yaml -n food-ordering
kubectl apply -f k8s/base/<servicio>/deployment.yaml -n food-ordering
kubectl rollout status deployment/<servicio> -n food-ordering --timeout=240s
kubectl logs deployment/<servicio> -n food-ordering --tail=120
```

### 5) Acceso desde tu maquina

En dos terminales separadas:

```powershell
kubectl port-forward svc/api-gateway 8000:8000 -n food-ordering
kubectl port-forward svc/frontend 4200:80 -n food-ordering
```

URLs:

- Frontend: `http://localhost:4200`
- API Gateway: `http://localhost:8000`
- Health API: `http://localhost:8000/actuator/health`

### 6) Reset rapido

```powershell
kubectl delete -k k8s/overlays/local --ignore-not-found=true
kubectl delete namespace food-ordering --ignore-not-found=true
docker compose -f infra/local/docker-compose.yml down -v
```

Para la guia completa de troubleshooting y comandos detallados, revisar `k8s/README.md`.

## Pruebas funcionales (sin frontend y con frontend)

Puedes validar el sistema de dos formas:

- Solo API (Postman): recomendado para validar flujo de negocio y depurar errores por servicio.
- Frontend + API: recomendado para demo funcional de experiencia de usuario.

### Opcion A: Postman (solo backend)

1. Importar `docs/postman/food-ordering-local.postman_collection.json`.
2. Ejecutar carpeta `00 Auth` para generar tokens (`admin1`, `cliente1`, `restaurante1`).
3. Ejecutar en orden: `01 Catalog` -> `02 User` -> `03 Orders and Flow`.
4. Revisar `99 Observabilidad` para validar `health` y `prometheus` en gateway.

### Opcion B: Frontend

1. Port-forward activo:

```powershell
kubectl port-forward svc/api-gateway 8000:8000 -n food-ordering
kubectl port-forward svc/frontend 4200:80 -n food-ordering
```

2. Ingresar a `http://localhost:4200`.
3. Probar flujo: productos -> crear pedido -> dashboard -> solicitar pago -> ver estado.

## Verificacion final de observabilidad

1. Mantener `port-forward` de servicios backend para que Prometheus scrapee endpoints.
2. Revisar targets en `http://localhost:9090/targets` (servicios desplegados en estado `UP`).
3. Abrir Grafana en `http://localhost:3000` y dashboard `Food Ordering - Basic Observability`.
