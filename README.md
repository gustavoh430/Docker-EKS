# Docker-EKS
Project using docker to build up an image and EKS to run it publicly

![Header](https://github.com/gustavoh430/Docker-EKS/blob/main/ReadmeImage.png)




## What is it for?
We are going to create an image from an application in java already created and available on "https://github.com/gustavoh430/loginproject". Moreover, it will be necessary to fetch MySQL image from Docker Hub.
Basically, we are going to host them in EKS and make them communicate with each other.




## Building Java Image

Dockerfile has been set up as follows:

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

Then, once it is saved in the application path as "Dockerfile", it is time to create a repository in Docker Hub, which will host the image that will be created.
In order to do that, go to "https://hub.docker.com/", sign up, and click on "Create Respository". Then, choose a name and click on "Create"

![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/d9dfd1f7-65a7-4fd7-9502-359ceb435df4)


After creating this repository, we can move ahead and build up the image.

Obs. The image and repository name, which has just been created, has to be exactly the same.

```text
docker build -t gustavoh430/login_app .
```

Soon after this, it becomes avaible from

```text
docker images
```

![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/ecc190db-8750-47b2-8e07-eb51c5c20153)


## How Kubernetes works

In the following image we can check the Kubernetes Architecture:

![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/1e767268-7ec3-418e-b5b8-c5e2b4d39564)


Let's take a look at the key concepts of Kubernetes

1. Cluster: A set of node machines which are running the conternarized application (Worker Node) or the node controller (Master Node)
2. Nodes
3. Master Node
4. Worker Node
5. PODs
6. Containers
7. Services
