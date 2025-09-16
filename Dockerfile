# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Pre-build dependencies (optional, speeds up spring-boot:run)
RUN mvn dependency:resolve

# ---------- Run stage ----------
FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app

# Copy source code and pom.xml again (spring-boot:run needs it)
COPY pom.xml .
COPY src ./src

# Expose Spring Boot port
EXPOSE 8080

# Run directly with spring-boot:run and your JVM argument
ENTRYPOINT ["mvn", "spring-boot:run", "-DskipTests", "-Dspring-boot.run.jvmArguments=--add-opens java.base/sun.net.www.protocol.https=ALL-UNNAMED"]
