FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=builder build/libs/fasting-date-counter-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=deploy", "/app.jar"]
