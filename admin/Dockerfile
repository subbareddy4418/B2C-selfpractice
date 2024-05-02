# Stage 1: Build the Docker image
FROM openjdk:17-jdk-slim AS final
WORKDIR /app
COPY --from=build /app/target/admin-0.0.1-SNAPSHOT.jar /app/admin.jar
EXPOSE 8080
CMD ["java", "-jar", "admin.jar"]
