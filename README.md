# Docker-EKS
Project using docker to build up an image and EKS to run it publicly

![Header](https://github.com/gustavoh430/Docker-EKS/blob/main/ReadmeImage.png)




## What is it for?
We are going to create an image from an application in java already created and available on "https://github.com/gustavoh430/loginproject". Moreover, it will be necessary to fetch MySQL image from Docker Hub.
Basically, we are going to host them in EKS and make them communicate with each other.




## Building Java Image

Dockerfile has been configured as follows:

```text
FROM maven:3.8.5-openjdk-17-slim as build
COPY /src /app/src
COPY /pom.xml /app
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip

FROM openjdk:17-alpine
EXPOSE 8080
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
```

Then, once it is saved in the application path as "Dockerfile", we can run it through terminal:

```text
docker build -t gustavoh430/login_app .
```

