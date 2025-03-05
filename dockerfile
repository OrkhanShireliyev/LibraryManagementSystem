# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the local build files into the container
COPY build/libs/LibraryManagementSystem-0.0.1-SNAPSHOT.jar /app/library-management.jar

# Expose the port your app is running on
EXPOSE 9090

# Run the JAR file
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/app/library-management.jar"]

