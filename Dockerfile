FROM openjdk:17-jdk-slim


WORKDIR /app


COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src


RUN chmod +x gradlew


RUN ./gradlew build -x test --no-daemon


EXPOSE 8080


CMD ["java", "-jar", "build/libs/playground-0.0.1-SNAPSHOT.jar"]
