# spring-boot-postgres-on-k8s

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-postgres-on-k8s-sample/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-postgres-on-k8s-sample/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.x-6DB33F?logo=springboot)
![Dependabot](https://img.shields.io/badge/Dependabot-enabled-025E8C?logo=dependabot)

This demo deploys a simple Spring Boot web application that connects to Postgres onto a Kubernetes cluster.

You can watch this demo along with an introduction to Kubernetes
concepts [here](https://www.youtube.com/watch?v=OsWXtVbTnv0).

## Tech Stack

- Java 25 (LTS)
- Spring Boot 4.1.x (Web, Data JPA, Thymeleaf)
- PostgreSQL
- Maven (via the bundled Maven wrapper `./mvnw`)
- Testcontainers for integration tests
- Docker image based on `bellsoft/liberica-openjdk-debian:25`

## Prerequisites

- JDK 25 installed (`java -version` should report 25)
- Docker running locally (required by the Testcontainers-based tests)
- Kubernetes cluster with kubectl installed and configured to use your cluster
- docker cli installed, you must be signed into your Docker Hub account

## Build and Test Locally

1. Run the build including tests (Testcontainers starts a throwaway PostgreSQL container)
   ```
   ./mvnw clean package
   ```

1. Run the app against a local PostgreSQL (see `src/main/resources/application.properties` for the connection settings)
   ```
   ./mvnw spring-boot:run
   ```
   The app is then available at `http://localhost:8080`.

## Continuous Integration

- [GitHub Actions](.github/workflows/maven.yml) builds and tests every push and pull request to `main` on Temurin JDK 25.
- On pushes to `main`, the Maven dependency graph is submitted to GitHub to improve Dependabot alerts.

## Dependency Updates

[Dependabot](.github/dependabot.yml) keeps dependencies up to date:

| Ecosystem      | Schedule              | Grouping                                                        |
|----------------|-----------------------|-----------------------------------------------------------------|
| Maven          | Daily, 05:00 WIB      | Spring Boot, Testcontainers, and other minor/patch updates      |
| GitHub Actions | Daily, 05:00 WIB      | All actions in a single PR                                      |
| Docker         | Weekly, Monday 05:00  | Base image; major Java version bumps are ignored (stays on 25) |

Pull requests use conventional commit prefixes (`build(deps)`, `ci(deps)`, `build(docker)`) and are labelled automatically.

## Deploy Spring Boot app and Postgres on Kubernetes

1. Deploy postgres with a persistent volume claim
   ```
   kubectl create -f k8s/postgres.yml
   ```

1. Create a config map with the hostname of Postgres
   ```
   kubectl create configmap hostname-config --from-literal=postgres_host=$(kubectl get svc postgres -o jsonpath="{.spec.clusterIP}")
   ```

1. Build the Spring Boot app

   ```
   ./mvnw -DskipTests package
   ```

1. Build a Docker image and push the image to Docker Hub
   ```
   docker build -t <your Docker Hub account>/spring-boot-postgres-on-k8s:v1 .
   docker push <your Docker Hub account>/spring-boot-postgres-on-k8s:v1
   ```

1. Replace `<your Docker Hub account>` with your account name in `k8s/app.yml`, then deploy the app
   ```
   kubectl create -f k8s/app.yml
   ```

1. Create an external load balancer for your app
   ```
   kubectl expose deployment spring-boot-postgres-sample --type=LoadBalancer --port=8080
   ```

1. Get the External IP address of Service, then the app will be accessible at `http://<External IP Address>:8080`
   ```
   kubectl get svc spring-boot-postgres-sample
   ```
   > **Note:** It may take a few minutes for the load balancer to be created

1. Scale your application
   ```
   kubectl scale deployment spring-boot-postgres-sample --replicas=3
   ```

## Updating your application

1. Update the image that the containers in your deployment are using
   ```
   kubectl set image deployment/spring-boot-postgres-sample spring-boot-postgres-sample=<your Docker Hub account>/spring-boot-postgres-on-k8s:v2
   ```

## Deleting the Resources

1. Delete the Spring Boot app deployment
   ```
   kubectl delete -f k8s/app.yml
   ```

1. Delete the service for the app
   ```
   kubectl delete svc spring-boot-postgres-sample
   ```

1. Delete the hostname config map
   ```
   kubectl delete cm hostname-config
   ```

1. Delete Postgres
   ```
   kubectl delete -f k8s/postgres.yml
   ```