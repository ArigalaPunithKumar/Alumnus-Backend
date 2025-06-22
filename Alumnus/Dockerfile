# --- Build Stage ---
# Use a valid Maven image to build the application JAR
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application, skipping tests for a faster build
RUN mvn package -DskipTests

# --- Final Stage ---
# Use a slim OpenJDK image for the final, smaller application image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory
WORKDIR /app

# Copy the executable JAR from the build stage
# The path is target/Alumnus-1.0.0.jar based on your pom.xml
COPY --from=builder /app/target/Alumnus-1.0.0.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]