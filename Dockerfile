FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy Maven descriptor files first to leverage Docker layer caching for dependencies
COPY pom.xml .
COPY core-crypto/pom.xml core-crypto/pom.xml
COPY demo-spring-boot-backend/pom.xml demo-spring-boot-backend/pom.xml

# Pre-fetch dependencies for the demo-spring-boot-backend module and its required modules
RUN mvn -pl demo-spring-boot-backend -am dependency:go-offline

# Copy the full source tree and build only the backend module
COPY . .

RUN mvn -pl demo-spring-boot-backend -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/demo-spring-boot-backend/target/*.jar app.jar

ENV JAVA_OPTS=""

# Render injects PORT; Spring Boot reads it via server.port=${PORT:48080}
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

