# Etapa de compilación
FROM gradle:8.5-jdk17-alpine AS build

WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY src ./src

# Compilar sin tests
RUN gradle build -x test --no-daemon

# Etapa de ejecución: JRE ligero
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Puerto expuesto (la aplicación usará la variable de entorno PORT en Render)
EXPOSE 8080

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/build/libs/*.jar ./app.jar

# Comando de arranque con PORT configurable
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar ./app.jar"]