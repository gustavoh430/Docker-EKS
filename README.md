# Docker-EKS
Project using docker to build up an image and EKS to run it publicly

![Header](https://github.com/gustavoh430/Docker-EKS/blob/main/ReadmeImage.png)




## What is it for?
We are going build an image from a java application already created and available on "https://github.com/gustavoh430/loginproject". Moreover, it will be necessary to fetch MySQL image from Docker Hub.
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

## Pushing it into Docker Hub

Use the following command:

```text
docker push gustavoh430/login_app
```

![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/231cab95-2041-4868-8c20-c13830c1b6a5)


## How Kubernetes works

In the following image we can check the Kubernetes Architecture:

![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/d3db25fc-d332-4caf-ade7-49d52ca66d3c)



Let's take a look at the key concepts of Kubernetes

**1. Cluster:** A set of node machines which are running the conternarized application (Worker Node) or the node controller (Master Node)

**2. Nodes:** Physical or virtual machines with a certain hardware capacity which host one or multiple Pods + resources below:
   
   **Kubelet**
   
   Description: Kubelet is an agent that runs on each node and is responsible for making sure that the containers are running in a Pod.

   **Kube Proxy**

   Description: Kube Proxy is a network proxy that maintains network rules on nodes and helps with network communication to/from the pods.

   **Container Runtime**

   Description: The container runtime (e.g., Docker, containerd) is responsible for running containers in a pod.
   
**3. Master Node:** Cluster Control Pane for managing Pods across the Worker Nodes. It contains the following components:
   
   **API Server**
   
   Description: The Kubernetes API server is the front-end for the Kubernetes control plane. It exposes the Kubernetes API and is used to communicate with the workder node.
   
   **etcd**
   
   Description: etcd is a distributed key-value store that stores the configuration data of the Kubernetes cluster. It holds every API object, including config values and sensitive        data you store in ConfigMaps and
   
   **Controller Manager**
   
   Description: The Controller Manager manages controller processes responsible for maintaining the desired state of the cluster. It includes controllers for nodes, replication,          endpoints, and more. It is a loop that continually monitors your cluster and performs actions when certain events occur, like creating a pod when a new deployment is deployed.
   
   **Scheduler**
   
   Description: The Scheduler assigns work (pods) to worker nodes based on resource availability, policies, and other constraints.
   
**4.POD:**

   Description: The smallest deployable unit in Kubernetes. A pod can contain one or more containers. It hosts the container + their required resources to run it.
   
**5.Service:**

   Description: A Kubernetes service is an abstraction that defines a set of pods and provides a network connection to those pods. It is important to make our PODs reachable.
   
**6.Volume:**

   Description: Volumes are used to store and share data between containers in a pod and persist data beyond the lifetime of a pod.
   
**7.Namespace:**

   Description: Namespaces are virtual clusters within a physical cluster, allowing for resource isolation and management. It's commonly used to group resources.


## Getting deeper into Kubernetes Concepts

**1. Replication Controllers:**

   Description: Replication Controllers (RCs) are responsible for maintaining a specified number of pod replicas running at all times. If a pod fails, the RC automatically replaces it with a new one. RCs ensure high availability, load balancing, and fault tolerance for your applications.

**2. Deployments:**

   Description: Deployments provide a higher-level abstraction for managing pod replicas. They allow you to describe an application's lifecycle, including rolling updates and             rollbacks. Deployments are essential for making changes to your application without causing downtime.

**3. StatefulSets:**

   Description: StatefulSets are used to manage stateful applications and pods that require stable network identities and stable storage. Examples include databases and distributed       systems. StatefulSets assign unique network identifiers and storage volumes to pods, making it suitable for applications with stateful data.

**4. ConfigMaps and Secrets:**

   Description: ConfigMaps are used to store configuration data as key-value pairs that can be injected into pods as environment variables or volume mounts. Secrets, on the other         hand, are used to store sensitive information, such as passwords and tokens. Both ConfigMaps and Secrets allow for the separation of configuration data from application code.

**5. Labels and Selectors:**

   Description: Labels are key-value pairs associated with Kubernetes objects, allowing for arbitrary metadata to be assigned to objects. Selectors are used to filter and organize        objects based on their labels. This concept is crucial for grouping and categorizing resources, and for defining how objects are discovered and manipulated.

**6. Resource Quotas:**

   Description: Resource Quotas enable administrators to set constraints on resource usage within namespaces. This helps in controlling and limiting the resource consumption of pods      and ensures fairness and isolation between different workloads in a multi-tenant cluster.
   
**7. Taints and Tolerations:**

   Description: Taints and tolerations are used to influence pod scheduling decisions. Taints are applied to nodes, indicating that certain conditions must be met for pods to be          scheduled on those nodes. Tolerations are added to pods, specifying which taints they can tolerate, allowing them to be scheduled on tainted nodes.

**8. Affinity and Anti-Affinity:**
   Description: Node affinity and anti-affinity rules are used to influence pod scheduling based on node characteristics. Node affinity ensures that pods are scheduled on nodes with       specific labels, while node anti-affinity prevents pods from being scheduled on nodes with certain characteristics. This is used for better workload distribution and high             availability.

**9. ReplicaSets:**
   Description: A ReplicaSet in Kubernetes is a resource object that ensures a specified number of pod replicas are running at all times within a cluster. It is one of the fundamental    building blocks for achieving high availability, load balancing, and fault tolerance in Kubernetes. ReplicaSets are often used to maintain and manage the desired number of pod         instances of a particular application or microservice.


**10. Job and CronJobs:**
   Description: Creates one or more pods to perform a specific task or job and then ensures that the task completes successfully. Kubernetes jobs and cronjobs are Kubernetes objects      that are primarily meant for short-lived and batch workloads.



## Resource definition setting up

Java Resource Definition. Here, we are creating a deployment (login-deployment) with only one POD (login) from the image that were built up before and hosted at DockerHub. Furthermore, it has an environment variable (SPRING.DATASOURCE.URL) which contains the service host and port (mysql-service.default:3306) and other instructions.
Then, we set up the Service component (login-service) pointing to 8080 port and externally available (type: LoadBalancer).

```text
apiVersion: apps/v1
kind: Deployment
metadata:
  name: login-deployment
spec:
  replicas: 1
  selector: 
    matchLabels:
      app: login
  template:
    metadata:
      labels:
        app: login
    spec:
      containers:
        - name: login
          image: gustavoh430/login_app
          env:
            - name: SPRING.DATASOURCE.URL
              value: "jdbc:mysql://mysql-service.default:3306/UserDatabase?allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&useSSL=false&useUnicode=yes&characterEncoding=UTF-8&useLegacyDatetimeCode=true&createDatabaseIfNotExist=true&useTimezone=true&serverTimezone=UTC"

---
apiVersion: v1
kind: Service
metadata:
  name: login-service
spec:
  selector:
    app: login
  type: LoadBalancer
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
```


We also must create the MySQL components. We also create a Deployment (mysql) with one POD (mysql) from an image already available on Docker Hub (mysql:5.6). It has got two environment variables (MYSQL_ROOT_PASSWORD and MYSQL_ROOT_USERNAME), points to 3306 port and storages data into a CSI volume type. We must have a Persistent Volume (PV) which represents the volume itself. Then, it is also necessary to create a Persistent Volume Claim, to attach our pod to the PV. Finally, to make this a dynamical communication, we create a StorageClass, so that we do not need to create a PV for every POD.



```text
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
spec:
  selector:
    matchLabels:
      app: mysql
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - image: mysql:5.6
        name: mysql
        env:
        - name: MYSQL_ROOT_PASSWORD
          value: root
        - name: MYSQL_ROOT_USERNAME
          value: root
        ports:
        - containerPort: 3306
          name: mysql
        volumeMounts:
        - name: efs-vol
          mountPath: /app/users
      volumes:
      - name: efs-vol
        persistentVolumeClaim:
          claimName: efs-pvc
---
kind: StorageClass
apiVersion: storage.k8s.io/v1
metadata:
  name: efs-sc
provisioner: efs.csi.aws.com
mountOptions:
  - iam
parameters:
  provisioningMode: efs-ap
  fileSystemId: fs-03db8f74bf9706627
  directoryPerms: "700"
---
apiVersion: v1
kind: PersistentVolume
metadata:
  name: efs-pv
spec:
  storageClassName: efs-sc
  capacity:
    storage: 5Gi
  volumeMode: Filesystem
  accessModes:
    - ReadWriteMany
  csi:
    driver: efs.csi.aws.com
    volumeHandle: fs-03db8f74bf9706627
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: efs-pvc
spec:
  storageClassName: efs-sc
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 5Gi
---
apiVersion: v1
kind: Service
metadata:
  name: mysql-service
spec:
  ports:
  - port: 3306
  selector:
    app: mysql
  clusterIP: None

```


## Working with AWS

1. Firstly, we will Create an EKS (Elastic Kubernetes Service) in us-east1 region.

![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/d41f7769-1bd4-4225-a9e2-da9c80de855c)

2. Give a name to your cluster (Name chosen: EKsCluster)

3. Then, create a Role as showed in "https://docs.aws.amazon.com/eks/latest/userguide/service_IAM_role.html#create-service-role".

4. Choose the Role just created.

5. Create a VPC using a template from CloudFormation.

      a. Create a Stack, clicking on:

      ![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/cdd4dc48-e0ad-4bff-a45e-8a2f94467e9d)

      Then, in the specification template, use the following url: "https://s3.us-west-2.amazonaws.com/amazon-eks/cloudformation/2020-10-29/amazon-eks-vpc-private-subnets.yaml"

      After this, click "Next" and give a name to your VPC

6 . Select the VPC you've created.

7. Select "Public and Private" in Cluster Endpoint Access.

   ![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/606ecfcd-6787-4ff3-9c5b-a4cf82e2a6e7)

8. Click on "Next" until we finally create the cluster.

9. After this, open the CLI and use the following command to update your kubeconfig:

   ```text
      aws eks update-kubeconfig --name EKsCluster
      ```

10. In "Compute" inside your EKS cluster, click on "Add Node Group"

   ![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/8a1619cc-dd4f-42e7-a6e3-46051090e9df)

11. Create another IAM role. This time we will use EC2 and the following permissions:

        AmazonEKSWorkerNodePolicy

        AmazonEKS_CNI_Policy

        AmazonEC2ContainerRegistryReadOnly

12. Select only the private subnets and click "Next" until node creation.

    ![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/d142d973-7ade-4b00-9653-a8d2a803bb2f)


13. Now we add a volume to persist our data even if the pod gets restarted. We will achieve that through EFS and CSI volume type (check it out on "https://github.com/kubernetes-sigs/aws-efs-csi-driver").

14. Create another Security Group. This time, we select the VPC we created before for our EKS cluster. This will ensure that our EKS will be accessible from this SG.

15. Fill out "Inbound Rules" as follows:
   Type: "NFS"
   Source: "Custom"
   CIDR Blocks: Our VPC IPV4 CIDR

![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/fae14195-79ad-4c17-99c9-4246bfab8975)

16. Create a Elastic File System (EFS) using the EKS VPC, through clicking on "Customize" (not Create). It is necessary because we are selecting the Security Group just created in the last step. 

![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/2903ba2e-c9f6-410a-af52-824f630f56e3)


17. Edit the MySQL.yml to add the EFS id to the "volumeHandle" in PersistentVolume compostion. Then, apply the new version to EKS.
   

![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/23f66093-94f2-478d-aee2-32cd2272dce9)



18. Use the following command to install CSI driver in our environment:

    ```text
kubectl apply -k "github.com/kubernetes-sigs/aws-efs-csi-driver/deploy/kubernetes/overlays/stable/?ref=release-1.7"
```

19. After this, we apply our resource definition through "CloudShell" or CLI.
   In order to do that, we upload our yml file to CloudShell, then we just apply it using the code below

   ```text
   kubectl apply -f Java.yml
   kubectl apply -f MySql.yml
   ```

20. Once all these steps were followed properly, we must get a working application on cloud. In order to make calls and test if it is working, we use the code below which will list our external IP.

```text
   kubectl get services
   ```

![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/06d95cc4-25bc-424d-b9f7-41f5ef4a4eb1)


With that, we can call the login-service, which is the only one that is exposed on the internet (the other is only accessible through the cluster).

21. Use Postman to test our application:
      
      Sign up (/login/signup)

    ![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/8c27de38-3cd4-4ea7-b6b1-92ba82924319)

      Login in (/login/signin)

   ![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/2601cddb-be63-4e72-8146-207d99a8484a)

      Gets user's information
      
   ![image](https://github.com/gustavoh430/Docker-EKS/assets/41215245/b6302eb9-1ca9-4d27-a8b3-17b50d1927dc)


# Author

Hi, I'm Gustavo!

I have been working as an SRE & Observability Analyst for two years already. As most of SREs, I work analyzing infraestructure and services in a massive Cloud based ecosystem, using traces, logs and metrics.

Contacts:
Email: gustavoh430@gmail.com

Linkedin: https://www.linkedin.com/in/gustavohgodinho/
