# Kubernetes local (Docker Desktop)

Este despliegue usa Kubernetes para aplicaciones y mantiene infraestructura base (Postgres, Kafka, Keycloak) en Docker Compose local.

## Flujo recomendado (seguro): uno por uno

1. Levantar infraestructura Docker.
2. Construir imagen del servicio.
3. Desplegar ese servicio en Kubernetes.
4. Verificar logs y estado.
5. Repetir con el siguiente servicio.

Orden recomendado:

1) `catalog-service`
2) `user-service`
3) `order-service`
4) `payment-service`
5) `delivery-service`
6) `api-gateway`
7) `frontend`

## 0) Prerrequisitos

- Docker Desktop con Kubernetes habilitado.
- Contexto de `kubectl` apuntando al clúster local.
- Puertos libres: `4200`, `8000`, `8080`, `9092`, `5432-5436`.

## 1) Reinicio total (opcional, desde cero)

```powershell
kubectl delete -k k8s/overlays/local --ignore-not-found=true
kubectl delete namespace food-ordering --ignore-not-found=true
docker compose -f infra/local/docker-compose.yml down -v
docker compose -f infra/local/docker-compose.yml up -d
```

## 2) Ajuste importante de Kafka

En `infra/local/docker-compose.yml`, en servicio `kafka`, usa:

```yaml
KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka:29092,EXTERNAL://host.docker.internal:${KAFKA_PORT}
```

Aplicar cambio:

```powershell
docker compose -f infra/local/docker-compose.yml up -d --force-recreate kafka kafka-ui
```

## 3) Construir imagenes locales

Backend (PowerShell/VS Code, recomendado): usar `clean` y `--%`.

```powershell
./backend/catalog-service/mvnw -f backend/catalog-service/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/catalog-service:local
./backend/user-service/mvnw -f backend/user-service/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/user-service:local
./backend/order-service/mvnw -f backend/order-service/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/order-service:local
./backend/payment-service/mvnw -f backend/payment-service/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/payment-service:local
./backend/delivery-service/mvnw -f backend/delivery-service/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/delivery-service:local
./backend/api-gateway/mvnw -f backend/api-gateway/pom.xml clean -DskipTests spring-boot:build-image --% -Dspring-boot.build-image.imageName=food-ordering/api-gateway:local
```

Frontend:

```powershell
docker build -t food-ordering/frontend:local frontend
```

Verificar imágenes:

```powershell
docker images | findstr food-ordering
```

## 4) Crear namespace y config base

```powershell
kubectl create namespace food-ordering
kubectl apply -f k8s/base/configmap.yaml -n food-ordering
kubectl apply -f k8s/base/secret.yaml -n food-ordering
```

## 5) Despliegue servicio por servicio

### 5.1 catalog-service

```powershell
kubectl apply -f k8s/base/catalog-service/service.yaml -n food-ordering
kubectl apply -f k8s/base/catalog-service/deployment.yaml -n food-ordering
kubectl get pods -n food-ordering -l app=catalog-service -w
kubectl logs deployment/catalog-service -n food-ordering --tail=80
```

### 5.2 user-service

```powershell
kubectl apply -f k8s/base/user-service/service.yaml -n food-ordering
kubectl apply -f k8s/base/user-service/deployment.yaml -n food-ordering
kubectl get pods -n food-ordering -l app=user-service -w
kubectl logs deployment/user-service -n food-ordering --tail=80
```

### 5.3 order-service

```powershell
kubectl apply -f k8s/base/order-service/service.yaml -n food-ordering
kubectl apply -f k8s/base/order-service/deployment.yaml -n food-ordering
kubectl get pods -n food-ordering -l app=order-service -w
kubectl logs deployment/order-service -n food-ordering --tail=120
```

### 5.4 payment-service

```powershell
kubectl apply -f k8s/base/payment-service/service.yaml -n food-ordering
kubectl apply -f k8s/base/payment-service/deployment.yaml -n food-ordering
kubectl get pods -n food-ordering -l app=payment-service -w
kubectl logs deployment/payment-service -n food-ordering --tail=120
```

### 5.5 delivery-service

```powershell
kubectl apply -f k8s/base/delivery-service/service.yaml -n food-ordering
kubectl apply -f k8s/base/delivery-service/deployment.yaml -n food-ordering
kubectl get pods -n food-ordering -l app=delivery-service -w
kubectl logs deployment/delivery-service -n food-ordering --tail=120
```

### 5.6 api-gateway

```powershell
kubectl apply -f k8s/base/api-gateway/service.yaml -n food-ordering
kubectl apply -f k8s/base/api-gateway/deployment.yaml -n food-ordering
kubectl get pods -n food-ordering -l app=api-gateway -w
kubectl logs deployment/api-gateway -n food-ordering --tail=100
```

### 5.7 frontend

```powershell
kubectl apply -f k8s/base/frontend/service.yaml -n food-ordering
kubectl apply -f k8s/base/frontend/deployment.yaml -n food-ordering
kubectl get pods -n food-ordering -l app=frontend -w
kubectl logs deployment/frontend -n food-ordering --tail=80
```

## 6) Acceso desde tu maquina

Terminal A:

```powershell
kubectl port-forward svc/api-gateway 8000:8000 -n food-ordering
```

Terminal B:

```powershell
kubectl port-forward svc/frontend 4200:80 -n food-ordering
```

URLs:

- Frontend: `http://localhost:4200`
- API Gateway: `http://localhost:8000`
- Health: `http://localhost:8000/actuator/health`

## 7) Reiniciar un servicio

```powershell
kubectl rollout restart deployment/order-service -n food-ordering
kubectl rollout status deployment/order-service -n food-ordering
```

Nota: durante `rollout restart` es normal ver 2 pods temporalmente.

## 8) Bajar Kubernetes

```powershell
kubectl delete -k k8s/overlays/local
```

## 9) Troubleshooting rapido

- `ErrImagePull`: imagen local faltante o tag incorrecto.
- `localhost:9092` en logs Kafka: revisar `KAFKA_ADVERTISED_LISTENERS` y recrear Kafka.
- `401` + mensaje CORS desde frontend: suele ser validación JWT/issuer. En este repo se usa `KEYCLOAK_ISSUER_URI=http://localhost:8080/...` y `KEYCLOAK_JWK_SET_URI=http://host.docker.internal:8080/.../certs` para K8s local.
- Estado completo:

```powershell
kubectl get all -n food-ordering
```
