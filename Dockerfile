FROM openjdk:17.0
LABEL mantainer="root@cydeo.com"
WORKDIR usr/app
COPY target/practice-project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
