FROM maven:3.9.9-amazoncorretto-17 as builder
LABEL mantainer="root@cydeo.com"
WORKDIR usr/app
COPY pom.xml .
COPY src ./src

RUN mvn package -Dmaven.test.skip

FROM openjdk:17-jdk-slim-buster

COPY --from=builder usr/app/target/practice-project-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

