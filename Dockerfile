FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

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