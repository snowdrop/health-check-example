/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.snowdrop.example;

import dev.snowdrop.example.service.GreetingProperties;
import org.arquillian.cube.openshift.impl.enricher.AwaitRoute;
import org.arquillian.cube.openshift.impl.enricher.RouteURL;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

@RunWith(Arquillian.class)
public class OpenShiftIT extends AbstractExampleApplicationTest {

    @AwaitRoute(path = "/actuator/health")
    @RouteURL("${app.name}")
    private URL baseURL;

    @Test
    public void testStopServiceEndpoint() {
        given()
           .baseUri(baseURI())
           .get("api/stop")
           .then()
           .statusCode(200);

        await("Await for the application to die").atMost(5, TimeUnit.MINUTES)
                .until(() -> !isAlive());

        await("Await for the application to restart").atMost(5, TimeUnit.MINUTES)
                .until(this::isAlive);

        await("Await for the application to be ready").atMost(5, TimeUnit.MINUTES)
                .until(this::isReady);
    }

    @Override
    protected GreetingProperties getProperties() {
        return new GreetingProperties();
    }

    @Override
    protected String baseURI() {
        return baseURL.toString();
    }

    private boolean isAlive() {
        try {
            return given().baseUri(baseURI()).get("/actuator/health/liveness").getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isReady() {
        try {
            return given().baseUri(baseURI()).get("/actuator/health/readiness").getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

}
