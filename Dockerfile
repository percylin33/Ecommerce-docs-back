# Usa una imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Define el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR al contenedor
COPY target/*.jar app.jar

# Expone el puerto en el que tu aplicación escucha
EXPOSE 8080

# Comando para ejecutar tu aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]