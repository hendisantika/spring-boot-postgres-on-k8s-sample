# Use a base image with Java 21
FROM bellsoft/liberica-openjdk-debian:21
LABEL authors="hendisantika"

# Copy the JAR package into the image
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the application port
EXPOSE 8080

# Run the App
#ENTRYPOINT ["java", "-jar", "/app.jar"]
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=k8s","-jar","/app.jar"]