FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8082

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
