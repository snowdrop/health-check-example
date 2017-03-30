### Description

This quickstart demonstrates the health check feature using Spring Boot microservices.

We have two REST endpoints, a greeting endpoint and a shutdown endpoint. The first one returns a simple hello string, while the second one shuts down JVM (System.exit(0)). 
To demonstrate how health check functions, simply invoke shutdown endpoint, and OpenShift should, based on health check, re-create new pod with your app.


### OpenShift Online

1. Go to [OpenShift Online](https://console.dev-preview-int.openshift.com/console/command-line) to get the token used by the oc client for authentication and project access. 

2. On the oc client, execute the following command to replace MYTOKEN with the one from the Web Console:

    ```
    oc login https://api.dev-preview-int.openshift.com --token=MYTOKEN
    ```
3. Use the Fabric8 Maven Plugin to launch the S2I process on the OpenShift Online machine & start the pod.

    ```
    mvn clean fabric8:deploy -Popenshift  -DskipTests
    ```
    
4. Get the route url.

    ```
    oc get route/health-check-springboot
    NAME              HOST/PORT                                          PATH      SERVICE                TERMINATION   LABELS
    health-check-springboot   <HOST_PORT_ADDRESS>             health-check-springboot:8080
    ```

5. Use the Host or Port address to access the greeting REST endpoint.
    ```
    http http://<HOST_PORT_ADDRESS>/api/greeting
    http http://<HOST_PORT_ADDRESS>/api/greeting name==John

    or 

    curl http://<HOST_PORT_ADDRESS>/api/greeting
    curl http://<HOST_PORT_ADDRESS>/api/greeting name==John
    ```

6. Use the Host or Port address to access the shutdown REST endpoint.
    ```
    http http://<HOST_PORT_ADDRESS>/api/killme

    or 

    curl http://<HOST_PORT_ADDRESS>/api/killme
    ```
    This should shutdown current process / JVM. 
    Since health check service wouldn't provide any proper response, OpenShift will re-create the pod.
    Wait a few seconds, and you should again be able to invoke greeting endpoint.