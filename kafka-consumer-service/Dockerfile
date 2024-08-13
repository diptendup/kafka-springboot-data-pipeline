FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app
COPY target/spring-boot-consumer-docker.jar /app/spring-boot-consumer-docker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boot-consumer-docker.jar"]