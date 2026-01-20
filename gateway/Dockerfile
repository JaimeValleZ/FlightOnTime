# ETAPA 1: BuildFROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests -pl gateway -am

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/gateway/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]