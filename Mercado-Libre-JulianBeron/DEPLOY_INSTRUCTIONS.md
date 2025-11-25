ll# 🚀 Instrucciones para Desplegar en Render

## ✅ Estado del Proyecto

Tu proyecto está **LISTO** para ser desplegado en Render. Todos los archivos de configuración necesarios han sido creados y actualizados:

### Archivos Configurados:
- ✅ `render.yaml` - Configuración de Render con Docker
- ✅ `Dockerfile` - Multi-stage build optimizado
- ✅ `application.properties` - Puerto configurable con variable PORT
- ✅ `.dockerignore` - Optimización de build
- ✅ `.gitignore` - Archivos ignorados
- ✅ `build.gradle` - Dependencias actualizadas (springdoc 2.3.0)

### JAR Generado:
- ✅ `build/libs/Mercado-Libre-JulianBeron-1.0-SNAPSHOT.jar` (53 MB)

---

## 📋 Pasos para Desplegar

### 1. Commitear los Cambios

```bash
# Añadir todos los cambios
git add .

# Commitear con un mensaje descriptivo
git commit -m "Configure project for Render deployment with Docker"

# Push al repositorio (ajusta la rama si es necesario)
git push origin main
```

### 2. Desplegar en Render

#### Opción A: Usando el Dashboard de Render

1. Ve a [Render Dashboard](https://dashboard.render.com/)
2. Click en **"New +"** → **"Web Service"**
3. Conecta tu repositorio de GitHub/GitLab
4. Render detectará automáticamente el archivo `render.yaml`
5. Click en **"Apply"** o **"Create Web Service"**
6. ¡Listo! Render construirá y desplegará automáticamente

#### Opción B: Usando Render CLI (Opcional)

```bash
# Instalar Render CLI (si no lo tienes)
npm install -g @render-com/cli

# Iniciar sesión
render login

# Desplegar
render deploy
```

---

## 🔧 Configuración de Render

El archivo `render.yaml` contiene:

```yaml
services:
  - type: web
    name: mutant-detector-api
    env: docker
    dockerfilePath: ./Dockerfile
    envVars:
      - key: PORT
        value: "8080"
```

### Variables de Entorno Adicionales (Opcional)

Si necesitas agregar más variables:

1. En el Dashboard de Render, ve a tu servicio
2. Click en **"Environment"**
3. Agrega las variables que necesites:
   - `SPRING_PROFILES_ACTIVE=prod` (para perfil de producción)
   - `DATABASE_URL=...` (si migras a PostgreSQL)

---

## 🧪 Verificar el Despliegue

Una vez desplegado, tu API estará disponible en:
```
https://mutant-detector-api.onrender.com
```

### Endpoints Disponibles:

1. **Health Check** (Spring Boot Actuator - si lo agregas):
   ```
   GET https://tu-app.onrender.com/actuator/health
   ```

2. **Detectar Mutante**:
   ```bash
   POST https://tu-app.onrender.com/mutant
   Content-Type: application/json
   
   {
     "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
   }
   ```

3. **Estadísticas**:
   ```
   GET https://tu-app.onrender.com/stats
   ```

4. **Swagger UI**:
   ```
   https://tu-app.onrender.com/swagger-ui.html
   ```

---

## 🐛 Troubleshooting

### Si el build falla en Render:

1. **Verificar logs**: En el Dashboard → Tu servicio → "Logs"
2. **Común**: Asegúrate de que el `Dockerfile` esté en la raíz
3. **Memoria**: Render Free Tier tiene 512 MB RAM. Si falla, reduce memoria JVM:
   ```yaml
   envVars:
     - key: JAVA_TOOL_OPTIONS
       value: "-Xmx400m -Xms256m"
   ```

### Si los tests fallan localmente:

```bash
# Cerrar IntelliJ IDEA (libera archivos bloqueados)
# Luego ejecutar:
.\gradlew.bat clean test --no-daemon
```

### Si quieres probar Docker localmente:

```bash
# Build
docker build -t mutant-detector-api .

# Run
docker run -p 8080:8080 -e PORT=8080 mutant-detector-api

# Probar
curl http://localhost:8080/stats
```

---

## 🔄 Actualizar el Despliegue

Cada vez que hagas push a tu repositorio, Render:
1. Detectará automáticamente el cambio
2. Construirá una nueva imagen Docker
3. Desplegará la nueva versión
4. ¡Sin downtime significativo!

---

## 📊 Monitoreo

En el Dashboard de Render verás:
- 📈 **Métricas**: CPU, Memoria, Requests
- 📋 **Logs**: En tiempo real
- 🔄 **Deploys**: Historial de despliegues
- ⚙️ **Settings**: Configuración del servicio

---

## 💾 Migrar a PostgreSQL (Recomendado para Producción)

Actualmente usas H2 (en memoria). Para datos persistentes:

### 1. Crear PostgreSQL en Render:
- Dashboard → "New +" → "PostgreSQL"
- Copia la **Internal Database URL**

### 2. Actualizar `build.gradle`:
```groovy
dependencies {
    // ... otras dependencias ...
    runtimeOnly 'org.postgresql:postgresql'
}
```

### 3. Actualizar `application.properties`:
```properties
# Usar DATABASE_URL de Render
spring.datasource.url=${DATABASE_URL}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### 4. Agregar variable en Render:
- Environment → Add Environment Variable
- Key: `DATABASE_URL`
- Value: (la URL de tu PostgreSQL)

---

## 📚 Recursos Útiles

- [Render Docs - Spring Boot](https://render.com/docs/deploy-spring-boot)
- [Render Docs - Docker](https://render.com/docs/docker)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)

---

## ✨ ¡Listo!

Tu proyecto está completamente configurado. Solo necesitas:
1. ✅ Hacer `git commit` y `git push`
2. ✅ Conectar el repo en Render
3. ✅ ¡Disfrutar de tu API en producción!

**Nota**: El primer despliegue puede tomar 5-10 minutos. Los siguientes serán más rápidos gracias al caché de Docker.

