# ─────────────────────────────────────────────────────────────────────────────
# Stage 1: Build
#   Uses a full JDK image to compile and package the Spring Boot application.
#   The result is a fat JAR in the /build directory.
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

# Copy Maven wrapper and POM first (layer-cache friendly)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ─────────────────────────────────────────────────────────────────────────────
# Stage 2: Runtime
#   Uses a slim JRE-only image.  Copies only the built JAR.
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

# Security: run as non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Copy the fat JAR from the builder stage
COPY --from=builder /build/target/attendance-management.jar app.jar

# Expose application port
EXPOSE 8080

# Health check for Docker daemon (ALB has its own)
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget -qO- http://localhost:8080/attendance/status || exit 1

# Start the application
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=${SPRING_PROFILE:-default}", \
    "-jar", "app.jar"]
