# Use an official OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

#  Copy the built JAR into the container.
COPY target/smart-health-care-appointment-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (default 8080)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
