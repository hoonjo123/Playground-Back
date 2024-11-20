FROM openjdk:17-jdk-slim

WORKDIR /app

COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

RUN chmod +x gradlew

EXPOSE 8080


CMD ["./gradlew", "bootRun"]
