# Mutant Detector API 🧬

API REST para detectar mutantes basándose en secuencias de ADN. Desarrollada con Spring Boot y desplegada en Render.

## 🚀 Despliegue en Render

Este proyecto está configurado para desplegarse automáticamente en Render usando Docker.

### Pasos para desplegar:

1. **Conectar repositorio en Render**
   - Ve a [Render Dashboard](https://dashboard.render.com/)
   - Click en "New +" → "Web Service"
   - Conecta tu repositorio de GitHub/GitLab

2. **Configuración automática**
   - Render detectará automáticamente el archivo `render.yaml`
   - La configuración incluye:
     - Construcción con Docker
     - Puerto configurable (8080)
     - Variables de entorno necesarias

3. **Deploy**
   - Render construirá y desplegará automáticamente
   - La URL estará disponible en el dashboard

## 📋 Endpoints

### POST /mutant
Detecta si una secuencia de ADN pertenece a un mutante.

**Request:**
```json
{
  "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}
```

**Response:**
- `200 OK` - Es mutante
- `403 Forbidden` - No es mutante

### GET /stats
Obtiene estadísticas de verificaciones de ADN.

**Response:**
```json
{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.4
}
```

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.2.0**
- **H2 Database** (en memoria)
- **Swagger/OpenAPI** (documentación)
- **Docker** (contenedorización)
- **Gradle** (gestión de dependencias)

## 📦 Ejecutar localmente

### Con Docker:
```bash
docker build -t mutant-detector-api .
docker run -p 8080:8080 mutant-detector-api
```

### Con Gradle:
```bash
./gradlew bootRun
```

## 📚 Documentación API

Una vez desplegado, accede a:
- Swagger UI: `https://tu-app.onrender.com/swagger-ui.html`
- API Docs: `https://tu-app.onrender.com/api-docs`

## 🧪 Tests

Ejecutar tests:
```bash
./gradlew test
```

Ver cobertura:
```bash
./gradlew jacocoTestReport
```

## 📝 Configuración

El proyecto usa H2 en memoria por defecto. Para producción en Render, considera migrar a PostgreSQL:

1. Agregar dependencia en `build.gradle`:
```groovy
runtimeOnly 'org.postgresql:postgresql'
```

2. Actualizar `application.properties`:
```properties
spring.datasource.url=${DATABASE_URL}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

3. Agregar base de datos en Render dashboard.

## 📄 Licencia

Este proyecto es parte de un desafío técnico.

