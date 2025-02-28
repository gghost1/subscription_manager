#FROM maven:3.8.8-eclipse-temurin-17 AS builder
#
#WORKDIR /app
#
#COPY pom.xml .
#COPY ./src ./src
#
#RUN mvn clean package -DskipTests
#
## Финальный образ
#FROM eclipse-temurin:17-jdk-alpine-3.21
#
#WORKDIR /app
#COPY --from=builder /app/target/*.jar app.jar
#
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Вариант с локальной сборкой

FROM eclipse-temurin:17-jdk-alpine-3.21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
