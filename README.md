# 🏢 Franchise API

API RESTful reactiva para gestión de franquicias, sucursales y productos. Desarrollada con **Spring Boot 3 + WebFlux + MongoDB Reactivo**.

---

## 🏗️ Arquitectura

El proyecto sigue los principios de **Clean Architecture**:

```
franchise-api/
├── src/main/java/com/accenture/franchise/
│   ├── domain/                    # Núcleo del negocio (sin dependencias externas)
│   │   ├── model/                 # Entidades del dominio
│   │   ├── repository/            # Puertos (interfaces)
│   │   └── exception/             # Excepciones de dominio
│   ├── application/               # Casos de uso
│   │   ├── usecase/               # Lógica de negocio
│   │   └── dto/                   # Objetos de transferencia de datos
│   └── infrastructure/            # Adaptadores (detalles técnicos)
│       ├── adapter/
│       │   ├── web/               # Controllers REST
│       │   └── persistence/       # Adaptadores MongoDB
│       ├── entity/                # Documentos MongoDB
│       ├── mapper/                # Mappers dominio <-> infraestructura
│       └── config/                # Configuración y error handler
├── terraform/                     # Infrastructure as Code (AWS)
├── Dockerfile
└── docker-compose.yml
```

### Tecnologías
| Componente | Tecnología |
|---|---|
| Framework | Spring Boot 3.2 + WebFlux |
| Programación | Reactiva (Project Reactor - Mono/Flux) |
| Base de datos | MongoDB (Reactivo) |
| Contenerización | Docker + Docker Compose |
| IaC (Cloud) | Terraform → AWS (ECS Fargate + DocumentDB) |
| Tests | JUnit 5 + Mockito + StepVerifier + WebTestClient |

---

## 🚀 Ejecución Local

### Prerrequisitos
- Java 17+
- Maven 3.9+
- Docker & Docker Compose

### Opción 1 — Docker Compose (Recomendado)

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/franchise-api.git
cd franchise-api

# Levantar MongoDB + API en un solo comando
docker compose up --build

# La API estará disponible en: http://localhost:8080
```

### Opción 2 — Maven local

```bash
# 1. Levantar solo MongoDB
docker compose up mongodb -d

# 2. Compilar y ejecutar
mvn clean package -DskipTests
java -jar target/franchise-api-1.0.0.jar
```

---

## 🧪 Ejecutar Tests

```bash
mvn test
```

Los tests incluyen:
- **Unit tests** del caso de uso (`FranchiseUseCaseTest`) con Mockito + `StepVerifier`
- **Integration tests** del controller (`FranchiseControllerTest`) con `@WebFluxTest` + `WebTestClient`

---

## 📡 Endpoints

Base URL: `http://localhost:8080/api/v1`

### Franquicias
| Método | URL | Descripción |
|--------|-----|-------------|
| `POST` | `/franchises` | Crear franquicia |
| `PATCH` | `/franchises/{id}/name` | Actualizar nombre de franquicia |

### Sucursales
| Método | URL | Descripción |
|--------|-----|-------------|
| `POST` | `/franchises/{id}/branches` | Agregar sucursal |
| `PATCH` | `/franchises/{franchiseId}/branches/{branchId}/name` | Actualizar nombre de sucursal |

### Productos
| Método | URL | Descripción |
|--------|-----|-------------|
| `POST` | `/franchises/{franchiseId}/branches/{branchId}/products` | Agregar producto |
| `DELETE` | `/franchises/{franchiseId}/branches/{branchId}/products/{productId}` | Eliminar producto |
| `PATCH` | `/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock` | Modificar stock |
| `PATCH` | `/franchises/{franchiseId}/branches/{branchId}/products/{productId}/name` | Actualizar nombre de producto |
| `GET` | `/franchises/{franchiseId}/top-stock-products` | Producto con más stock por sucursal |

### Ejemplos de uso con curl

```bash
# 1. Crear franquicia
curl -X POST http://localhost:8080/api/v1/franchises \
  -H "Content-Type: application/json" \
  -d '{"name": "McDonald'"'"'s Colombia"}'

# 2. Agregar sucursal (reemplaza {franchiseId} con el id obtenido)
curl -X POST http://localhost:8080/api/v1/franchises/{franchiseId}/branches \
  -H "Content-Type: application/json" \
  -d '{"name": "Sucursal Bogotá"}'

# 3. Agregar producto (reemplaza {franchiseId} y {branchId})
curl -X POST http://localhost:8080/api/v1/franchises/{franchiseId}/branches/{branchId}/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Big Mac", "stock": 150}'

# 4. Producto con mayor stock por sucursal
curl http://localhost:8080/api/v1/franchises/{franchiseId}/top-stock-products
```

---

## ☁️ Despliegue en AWS con Terraform

### Prerrequisitos
- Terraform >= 1.5
- AWS CLI configurado (`aws configure`)

```bash
cd terraform

# 1. Inicializar Terraform
terraform init

# 2. Crear archivo de variables (NO commitear a git)
cp example.tfvars terraform.tfvars
# Editar terraform.tfvars con tus valores reales

# 3. Planificar cambios
terraform plan -var-file="terraform.tfvars"

# 4. Aplicar infraestructura
terraform apply -var-file="terraform.tfvars"

# 5. Obtener URL del repositorio ECR
terraform output ecr_repository_url

# 6. Construir y subir imagen Docker a ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <ecr_url>
docker build -t franchise-api .
docker tag franchise-api:latest <ecr_url>:latest
docker push <ecr_url>:latest
```

### Recursos AWS creados
- **VPC** con subnets públicas y privadas
- **AWS DocumentDB** (compatible con MongoDB)
- **ECR** — Repositorio de imágenes Docker
- **ECS Fargate** — Servicio de contenedores serverless
- **CloudWatch** — Logs centralizados

---

## 📦 Estructura de respuestas

### Franquicia
```json
{
  "id": "uuid",
  "name": "McDonald's Colombia",
  "branches": [
    {
      "id": "uuid",
      "name": "Sucursal Bogotá",
      "products": [
        { "id": "uuid", "name": "Big Mac", "stock": 150 }
      ]
    }
  ]
}
```

### Top productos por sucursal
```json
[
  {
    "branchId": "uuid",
    "branchName": "Sucursal Bogotá",
    "product": { "id": "uuid", "name": "Big Mac", "stock": 150 }
  }
]
```

---

## 🔑 Variables de entorno

| Variable | Descripción | Default |
|----------|-------------|---------|
| `MONGO_URI` | URI de conexión MongoDB | `mongodb://localhost:27017/franchisedb` |
| `MONGO_DB` | Nombre de la base de datos | `franchisedb` |
| `PORT` | Puerto del servidor | `8080` |
