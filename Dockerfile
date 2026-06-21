# ── Stage 1: Build with Maven + JDK 21 ──
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml first to cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the JAR (skip tests — to be added later)
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Stage 2: Runtime with JRE 21 only (lightweight) ──
FROM eclipse-temurin:21-jre-alpine

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run as non-root user
USER appuser

# Document the port (actual mapping is in docker-compose.yml)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
