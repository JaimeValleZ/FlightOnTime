FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY config-server/pom.xml ./config-server/
COPY eureka/pom.xml ./eureka/
COPY gateway/pom.xml ./gateway/
COPY flight-prediction/pom.xml ./flight-prediction/
RUN mvn dependency:go-offline

COPY config-server/src ./config-server/src
RUN mvn -pl config-server clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/config-server/target/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
