# microservices-spring-k8s-gateway

| Type | Name | Description | Status |
|--- |--- |--- |--- |
| Lib | lib-common-exception | Handles all exceptions for the services it imports | Finished |
| Service | Microservice-gateway | Service that forwards incoming requests to the approproate destination | TODO |
| Service | Microservice-user|  Service to manage users | Finished |
| Service | Microservice-course | Service to manage courses | TODO |

<br/>

## Technology used in microservice

| Tecnology | Version | Site |
|--- |--- |--- |
| Java OpenJdk | 11      | [https://openjdk.org/](https://openjdk.org/)                                      |
| Spring Boot  | 2.7.3   | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)  |
| Spring Cloud | 2021.0.3| [https://spring.io/projects/spring-cloud](https://spring.io/projects/spring-cloud)|
| Spring Cloud Kubernetes| 2.1.3|[https://spring.io/projects/spring-cloud-kubernetes](https://spring.io/projects/spring-cloud-kubernetes)|
| Spring Webflux | 2.7.3 | [https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/boot-features-testing.html](https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/boot-features-testing.html)|
| OpenApi 3| 1.6.11| [https://springdoc.org/](https://springdoc.org/)|
| Hateoas| 1.6.11| [https://docs.spring.io/spring-hateoas/docs/current/reference/html/](https://docs.spring.io/spring-hateoas/docs/current/reference/html/)|
| PosrgreSQL| 13 | [https://www.postgresql.org/](https://www.postgresql.org/)|
| Flyway| 9.18.0| [https://flywaydb.org/documentation/usage/plugins/springboot](https://flywaydb.org/documentation/usage/plugins/springboot)|
| Docker | - | [https://www.docker.com/](https://www.docker.com/)|
| Docker Compose| 3.x | [https://docs.docker.com/compose/](https://docs.docker.com/compose/)|
| Google Jib|3.3.0 | [https://github.com/GoogleContainerTools/jib](https://github.com/GoogleContainerTools/jib)|
|Minikube |- |[https://minikube.sigs.k8s.io/docs/start/](https://minikube.sigs.k8s.io/docs/start/)|

<br/>

## Install + start minikube

### Install minikube [https://minikube.sigs.k8s.io/docs/start/](https://minikube.sigs.k8s.io/docs/start/)
<br/>

Start minikube
> $ minikube start --cpus 2 --memory 8192

Show status minikube
> $ minikube status

Stop minikube
> $ minikube stop

<br/>

## Kubernetes - commands

Apply configuration file (deployment.yaml, service.yaml, secret.yaml, etc)
> $ kubectl apply -f deployment-name.yaml

List (pod, service e deployment)
> $ kubectl get all

List only pods
> $ kubectl get pod

List only services
> $ kubectl get services

List only deployments
> $ kubectl get deployment

Show configmap
> $ kubectl describe configmaps [name-configmap]

Delete deployment, service, secret, etc 
> $ kubectl delete [deployment] [deployment-name]
 
Show url service
> $ minikube service [service-name] --url

Interactive pod - postgreSQL
> $ kubectl exec -it [pod-name] --  psql -h localhost -U admin --password -p 5432 postgres

<br/>

## Create account Docker Hub
[https://hub.docker.com/](https://hub.docker.com/)
<br/>

## Google Container Tools jib - commands

Create package .jar
> $ mvn clean install

Send image to docker hub (configured in pom.xml)
> $ mvn compile jib:build

<br/><br/>
## Start project local
to-do

## Start project with k8s
to-do

## Postman collections
to-do
