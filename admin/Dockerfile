# Use the official OpenJDK base image
FROM openjdk:17-jdk-slim AS final

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY admin/target/admin-0.0.1-SNAPSHOT.jar /app/admin.jar

# Define the command to run the application
CMD ["java", "-jar", "admin.jar"]
