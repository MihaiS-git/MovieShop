# Build stage
FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

COPY gradle /app/gradle
COPY gradlew /app
COPY build.gradle /app
COPY settings.gradle /app

RUN ./gradlew build --no-daemon

COPY src /app/src
RUN ./gradlew build --no-daemon -x test

# Run stage
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
