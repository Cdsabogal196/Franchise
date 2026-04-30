
# Franchise API - Gestión Reactiva de Franquicias

Aplicación desarrollada con **Spring Boot WebFlux** y **PostgreSQL** que permite administrar franquicias, sus sucursales y productos de forma reactiva. Incluye gestión de stock, actualización de nombres y reasignación de productos entre sucursales, todo mediante una API REST.

## 📋 Requisitos previos

- Java 21 (para ejecución local sin Docker)
- Maven 3.9+ (opcional, se puede usar el wrapper)
- PostgreSQL (local o remoto)
- Docker (opcional, para contenerización)

# Gestión de una franquicia

Se creó una aplicación Java para gestionar de forma reactiva todas las sucursales y productos de las diferentes franquicias que se manejan a través de la aplicación
## ⚙️ Configuración de la aplicación

El archivo `src/main/resources/application.properties` contiene la configuración de la base de datos y del servidor. Asegúrate de ajustar los valores según tu entorno.

```properties
spring.application.name=Franchise
server.port=8081

spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/franchise_db}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=${DATABASE_USERNAME:postgres}
spring.datasource.password=${DATABASE_PASSWORD:12345}

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## Ejecución 

Es necesario crear una base de datos PostgreSQL con el siguiente nombre:
```bash
franchise_db
```
Con el siguiente comando se levantaran todos los servicios requeridos para ejecutar la aplicación y se iniciará el contenedor:
```bash
docker-compose up --build
```
La API estará disponible en:
```bash
http://localhost:8081
```

# 📡 Endpoints de la API
Todos los endpoints están bajo el prefijo /api y no requieren autenticación.

## 🔷 Franquicias (`/api/franchise`)

| Método | Endpoint                                      | Descripción                         |
|--------|-----------------------------------------------|-------------------------------------|
| POST   | `/api/franchise`                              | Crear una nueva franquicia          |
| PUT    | `/api/franchise/{franchiseId}/name`           | Actualizar el nombre de la franquicia |
| GET    | `/api/franchise/{franchiseId}/products-stock` | Obtener producto con mayor stock por sucursal |

### 1. Crear franquicia

**POST** `/api/franchise`

Body `FranchiseRequest`:

```bash
json
{
  "name": "Mi Franquicia ",
  "stock": 0
}
```

Respuesta (200 OK):
```bash
json
{
  "id": 1,
  "name": "Mu Franquicia"
}
```

### 2. Actualizar nombre de franquicia
**PUT** ` /api/franchise/{franchiseId}/name`
```bash
Body UpdateNameRequest:

json
{
  "name": "McDonalds"
}
```
Respuesta (200 OK):
```bash
json
{
  "message": "Franchise updated successfully",
  "product": {
    "id": 1,
    "name": "McDonalds"
  }
}
```
### 3. Obtener productos con mayor stock por sucursal
**GET** ` /api/franchise/{franchiseId}/products-stock`

Respuesta (200 OK) – 

```bash
ProductsFromFranchiseResponse:

json
{
  "franchiseId": 1,
  "franchiseName": "Franquicia Norte",
  "branches": [
    {
      "branchId": 1,
      "branchName": "Sucursal Principal",
      "products": [
        {
          "productId": 10,
          "productName": "Laptop Gamer",
          "stock": 25
        }
      ]
    },
    {
      "branchId": 2,
      "branchName": "Sucursal Norte",
      "products": [
        {
          "productId": 12,
          "productName": "Teclado Mecánico",
          "stock": 50
        }
      ]
    }
  ]
}
```
## 🔹 Sucursales | branch (`/api/branch`)

| Método | Endpoint                         | Descripción                       |
|--------|----------------------------------|-----------------------------------|
| POST   | `/api/branch`                    | Crear una sucursal asociada a una franquicia |
| PUT    | `/api/branch/{branchId}/name`    | Actualizar el nombre de la sucursal |

### 4. Crear sucursal

**POST** `/api/branch`

Body `BranchRequest`:

```bash
{
  "name": "Sucursal de Bogota",
  "franchiseId": 1
}

```
Respuesta (201 CREATED) – GetBranchResponse:
```bash
json
{
  "id": 1,
  "name": "Sucursal Principal",
  "franchiseId": 1
}
```

### 5. Actualizar nombre de sucursal
**PUT** `/api/branch/{branchId}/name`

Body `UpdateNameRequest`:
```json
{
  "name": "Sucursal de Paris"
}
```
Respuesta (200 OK):
```bash

json
{
  "message": "Product name updated successfully",
  "name": "Sucursal de Paris"
}
```
## 🔹 Productos (`/api/product`)

| Método   | Endpoint                                 | Descripción                                      |
|----------|------------------------------------------|--------------------------------------------------|
| POST     | `/api/product`                           | Crear un producto y asignarlo a una sucursal     |
| PUT      | `/api/product/{productId}/stock`         | Actualizar el stock de un producto               |
| PUT      | `/api/product/{productId}/name`          | Actualizar el nombre de un producto              |
| DELETE   | `/api/product/{productId}/branch`        | Eliminar la relación producto-sucursal (dejar el producto sin sucursal) |

### 6. Crear producto

**POST** `/api/product`

Body `ProductRequest`:

```bash

json
{
  "name": "Laptop Gamer",
  "stock": 15,
  "branchId": 1
}```

Respuesta (201 CREATED) – GetProductResponse:
```bash
json
{
  "id": 100,
  "name": "Laptop Gamer",
  "stock": 15,
  "branchId": 1
}
```
7. Actualizar stock del producto

**PUT** `/api/product/{productId}/stock`/

Body `/UpdateStockRequest`/:
```bash

json
{
  "stock": 30
}```

Respuesta (200 OK) – GetProductResponse actualizado:
```bash

json
{
  "id": 100,
  "name": "Laptop Gamer",
  "stock": 30,
  "branchId": 1
}
```

8. Actualizar nombre del producto
**PUT** ` /api/product/{productId}/name`

Body `UpdateNameRequest`:

```bash
json
{
  "name": "Laptop Ultrabook"
}
```

Respuesta (200 OK):

```bash

json
{
  "message": "Product updated successfully",
  "product": {
    "id": 100,
    "name": "Laptop Ultrabook",
    "stock": 30,
    "branchId": 1
  }
}

```

### 9. Eliminar relación producto-sucursal
**DELETE**  `/api/product/{productId}/branch `

Respuesta (200 OK):

```bash

json
{
  "message": "Product branch relation deleted successfully",
  "name": "Laptop Ultrabook",
  "stock": 30
}

```
**Posibles errores**:
- `404` si el producto no existe.
- `400` si el producto ya no tiene sucursal asignada.

## 🧪 Orden recomendado para probar la API

Sigue esta secuencia para validar el buen funcionamiento del:

| Paso | Endpoint                                     | Propósito                                | Dependencias                     |
|------|----------------------------------------------|------------------------------------------|----------------------------------|
| 1    | `POST /api/franchise`                        | Crear franquicia → obtener `franchiseId` |                                  |
| 2    | `POST /api/branch`                           | Crear sucursal → obtener `branchId`      | `franchiseId` (paso 1)           |
| 3    | `POST /api/product`                          | Crear producto → obtener `productId`     | `branchId` (paso 2)              |
| 4    | `PUT /api/product/{productId}/stock`         | Actualizar stock del producto            | `productId` (paso 3)             |
| 5    | `PUT /api/product/{productId}/name`          | Cambiar nombre del producto              | `productId` (paso 3)             |
| 6    | `PUT /api/branch/{branchId}/name`            | Cambiar nombre de la sucursal            | `branchId` (paso 2)              |
| 7    | `PUT /api/franchise/{franchiseId}/name`      | Cambiar nombre de la franquicia          | `franchiseId` (paso 1)           |
| 8    | `DELETE /api/product/{productId}/branch`     | Desvincular producto de la sucursal      | `productId` (paso 3)             |
| 9    | `GET /api/franchise/{franchiseId}/products-stock` | Ver producto con mayor stock por sucursal | `franchiseId` (paso 1)         |

## 🧪 Pruebas

El proyecto incluye pruebas unitarias para los controladores usando `WebFluxTest` y `Mockito`. Se cubren los escenarios de éxito y errores comunes (404, 500, etc.).

### Estructura de pruebas

- **BranchControllerTest**: prueba la creación de sucursal, actualización de nombre, y sus errores (franquicia no encontrada, error interno).
- **FranchiseControllerTest**: prueba la creación de franquicia, actualización de nombre, consulta de productos con stock, y sus errores.
- **ProductControllerTest**: prueba la creación de producto, actualización de stock, actualización de nombre, eliminación de relación producto‑sucursal, y sus errores.


### 🛠️ Tecnologías utilizadas

- Java 21

- Spring Boot 3 + WebFlux (reactivo)

- Spring Data JPA + Hibernate

- PostgreSQL

- Maven

- Docker