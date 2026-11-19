FROM eclipse-temurin:8-jre
WORKDIR /app
COPY target/consumer-service-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
