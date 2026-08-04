# syntax=docker/dockerfile:1

# ---- Build stage -------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy only the POM first so dependency resolution is cached in its own
# layer and doesn't re-run on every source change.
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Now copy the actual source and build the jar.
COPY src src
RUN mvn -B -q package -DskipTests

# ---- Runtime stage -------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runtime

# Run as non-root.
RUN groupadd --system spring && useradd --system --gid spring spring
USER spring:spring

WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

# Basic container-level healthcheck against Spring Boot Actuator.
# Requires spring-boot-starter-actuator + management.endpoint.health.enabled=true.
HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]