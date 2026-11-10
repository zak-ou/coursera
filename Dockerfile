# Multi-stage Dockerfile for Smart Clinic Management System

# Stage 1: Build stage
# Uses Maven image to compile the Spring Boot application
FROM maven:3.9-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (leverages Docker cache)
COPY app/pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY app/src ./src

# Compile and package the application
# Skips tests for faster builds (run tests separately in CI/CD)
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
# Uses lightweight JRE image to run the application
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the compiled JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
# Spring Boot default port is 8080
EXPOSE 8080

# Set environment variables (can be overridden at runtime)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Health check to ensure container is running properly
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Entry point to run the application
# Uses exec form to ensure proper signal handling
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Alternative ENTRYPOINT with explicit parameters
# ENTRYPOINT ["java", "-jar", "app.jar"]

# Labels for metadata
LABEL maintainer="clinic-admin@example.com"
LABEL version="1.0"
LABEL description="Smart Clinic Management System Backend"
