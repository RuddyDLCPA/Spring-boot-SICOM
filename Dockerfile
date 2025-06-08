# Etapa de compilación - Usar una imagen con Java 17
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución - Usar Java 17 también para la ejecución
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
# Corregir el nombre del archivo JAR aquí
COPY --from=build /app/target/globaline_logistic_api-1.0.0.jar app.jar

# Exponer el puerto (ajusta según tu aplicación)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]