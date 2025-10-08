# backend/Dockerfile

# Build stage
FROM eclipse-temurin:17 AS build

WORKDIR /app

COPY pom.xml mvnw ./

COPY .mvn .mvn

COPY src src

RUN ./mvnw -B -DskipTests clean package


# Run stage
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/project-two-ecommerce-application-0.0.1-SNAPSHOT.jar app.jar

# Environment variables
ENV SPRING_PROFILES_ACTIVE=prod

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
