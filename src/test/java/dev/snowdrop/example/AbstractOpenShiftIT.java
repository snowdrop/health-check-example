/*
 * Copyright 2021 Red Hat, Inc, and individual contributors.
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

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import dev.snowdrop.example.service.GreetingProperties;

public abstract class AbstractOpenShiftIT extends AbstractExampleApplicationTest {

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

    private boolean isAlive() {
        try {
            return given().baseUri(baseURI()).get("actuator/health/liveness").getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isReady() {
        try {
            return given().baseUri(baseURI()).get("actuator/health/readiness").getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

}
