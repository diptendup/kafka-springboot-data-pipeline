FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app
COPY target/spring-boot-producer-docker.jar /app/spring-boot-producer-docker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boot-producer-docker.jar"]