### Description

This example demonstrates an OpenShift health check feature with two Spring Boot microservices.

Example consists of two microservices: name-service and hello-service.

name-service exposes a GET endpoint returning a name as a String, which is configured in its application.properties file.

hello-service exposes a GET endpoint returning a hello message. It uses the name-service to get a name to whom the hello message is addressed. Because of this relationship, hello-service implements a [health indicator](https://github.com/gytis/spring-boot-health-check-example/blob/master/hello-service/src/main/java/org/jboss/snowdrop/hello/HelloServiceHealthIndicator.java#L30) to verify that name-service can be used. In case of the name-service being unavailable, hello-service will become unhealthy. OpenShift probe will notice that and will make it unavailable and outside users will get an HTTP status 503 (service unavailable). hello-service will become available again as soon as a name-service becomes available.

### Usage

1. Deploy microservices with Fabric8 maven plugin:

    mvn clean fabric8:deploy

2. Open OpenShift console and navigate to your project's overview page.

3. Wait until both services are running.

4. Scale down name-service to 0 pods. hello-service probes will start failing and OpenShift will make this service unavailable from outside.

5. If you'd try to open a hello-service route you should get an HTTP error 503 service unavailable.

6. Scale up name-service to 1 pod. hello-service probes will start passing and OpenShift will make this service available again.