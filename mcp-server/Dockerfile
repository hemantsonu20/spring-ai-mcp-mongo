FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
