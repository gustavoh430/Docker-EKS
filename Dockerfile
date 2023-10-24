FROM maven:3.8.5-openjdk-17-slim as build
COPY /src /app/src
COPY /pom.xml /app
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip

FROM openjdk:17-alpine
EXPOSE 8080
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]