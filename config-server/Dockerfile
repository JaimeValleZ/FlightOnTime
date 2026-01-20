
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests -pl config-server -am

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/config-server/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]