FROM openjdk:17-jdk-slim

WORKDIR /app

# Gradle 및 설정 파일 복사
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

# Gradle 실행 권한 부여
RUN chmod +x gradlew

# 의존성 사전 다운로드
RUN ./gradlew clean assemble --no-daemon

# 애플리케이션 실행 포트 노출
EXPOSE 8080

# 컨테이너 실행 시 애플리케이션 구동
CMD ["./gradlew", "bootRun", "--no-daemon"]
