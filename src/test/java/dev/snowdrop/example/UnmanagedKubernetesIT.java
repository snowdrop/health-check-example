/*
 * Copyright 2016-2021 Red Hat, Inc, and individual contributors.
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

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import dev.snowdrop.example.service.GreetingProperties;
import io.dekorate.testing.annotation.Inject;
import io.dekorate.testing.annotation.KubernetesIntegrationTest;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.LocalPortForward;

@EnabledIfSystemProperty(named = "unmanaged-test", matches = "true")
@KubernetesIntegrationTest(deployEnabled = false, buildEnabled = false)
public class UnmanagedKubernetesIT extends AbstractExampleApplicationTest {

    @Inject
    KubernetesClient client;

    LocalPortForward appPort;

    @BeforeEach
    public void setup() {
        appPort = client.services().inNamespace(System.getProperty("kubernetes.namespace"))
                .withName("health-check").portForward(8080);
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (appPort != null) {
            appPort.close();
        }
    }

    @Override
    protected GreetingProperties getProperties() {
        return new GreetingProperties();
    }

    @Override
    public String baseURI() {
        return "http://localhost:" + appPort.getLocalPort() + "/";
    }
}
