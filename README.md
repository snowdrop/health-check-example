### Description

This QuickStart demonstrates the health check feature using two Spring Boot microservices.
The `name-service` exposes a REST endpoint which returns a name as a String when it is called while the `hello-service` exposes
another REST endpoint which returns a hello message to the user.

Before to reply to the user it will call the `name-service` in order to get the name to be returned within the Hello World Message.

To control if the `name-service` is still alive, the `hello-service` implements the Health Check pattern to verify the status of the service

```
	private boolean isNameServiceUp() {
		RestTemplate restTemplate = new RestTemplate();
		try {
			return restTemplate.exchange(HelloServiceController.NAME_SERVICE_URL, HttpMethod.GET, null, String.class)
					.getStatusCodeValue() == 200;
		} catch (Throwable t) {
			return false;
		}
	}
```

If `name-service` is alive, it will get as HTTP response the status `200` and the Health Status reported by the Spring Boot actuator will be `Up`
  
```
return Health.up()
```
  
If now the `name-service` is down and unreachable, then the `hello-service` will be informed about this situation due to the heartbit session managed by Spring Boot
and will become unhealthy as the Health Status reported is `down`.
 
As Openshift probes the service to control its Health status, it will discover that the health status of the `hello-service` is now `Down`
and will make it unavailable.
So, every call issued to the `hello-service` through the Openshift service will receive a HTTP status 503 (service unavailable).

When the `name-service` will be restored (example : new pod created) and that `hello-service` will discover that it is alive again, then its health check status
will be changed to Up and OpenShift will allow to access it again.

### Usage

1. Deploy microservices with Fabric8 maven plugin:

    mvn clean fabric8:deploy -Popenshift

2. Open OpenShift console and navigate to your project's overview page.

3. Wait until both services are running.

4. Scale down `name-service` to `0` pod. Then the `hello-service` probe will start to fail and OpenShift will make this service unavailable from outside.

5. If you'd try to call the `hello-service` route, you should get an HTTP error 503 service unavailable.

6. Scale up `name-service` to 1 pod. Soon, you will see that the `hello-service` probe will start again and OpenShift will make this service available again.