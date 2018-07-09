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
package io.openshift.booster;

import static io.restassured.RestAssured.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;

import io.openshift.booster.service.GreetingProperties;
import io.openshift.booster.service.TomcatShutdown;
import org.apache.catalina.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocalTest extends AbstractBoosterApplicationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private GreetingProperties properties;

    @MockBean
    private TomcatShutdown tomcatShutdown;

    @Test
    public void testStopServiceEndpoint() {
        given()
            .baseUri(baseURI())
            .get("api/stop")
            .then()
            .statusCode(200);

        InOrder inOrder = inOrder(tomcatShutdown);
        inOrder.verify(tomcatShutdown)
                .setContext(any(Context.class));
        inOrder.verify(tomcatShutdown)
                .shutdown();
    }

    @Override
    protected GreetingProperties getProperties() {
        return properties;
    }

    @Override
    protected String baseURI() {
        return String.format("http://localhost:%d/", port);
    }

}
