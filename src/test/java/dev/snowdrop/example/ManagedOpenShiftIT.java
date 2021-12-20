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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import io.dekorate.testing.annotation.Inject;
import io.dekorate.testing.openshift.annotation.OpenshiftIntegrationTest;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.api.model.Route;
import io.fabric8.openshift.client.OpenShiftClient;

@DisabledIfSystemProperty(named = "unmanaged-test", matches = "true")
@OpenshiftIntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManagedOpenShiftIT extends AbstractOpenShiftIT {

    @Inject
    KubernetesClient client;

    String baseURI;

    @BeforeAll
    public void setup() {
        // TODO: In Dekorate 2.7, we can inject Routes directly, so we won't need to do this:
        Route route = client.adapt(OpenShiftClient.class).routes().withName("health-check").get();
        String protocol = route.getSpec().getTls() == null ? "http" : "https";
        int port = "http".equals(protocol) ? 80 : 443;
        baseURI = String.format("%s://%s:%s/", protocol, route.getSpec().getHost(), port, "/");
    }

    @Override
    public String baseURI() {
        return baseURI;
    }

}
