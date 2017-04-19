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

import static com.jayway.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

import io.openshift.booster.test.OpenShiftTestAssistant;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class OpenShiftIT extends AbstractBoosterApplicationTest {

    private static OpenShiftTestAssistant assistant = new OpenShiftTestAssistant();

    @BeforeClass
    public static void prepare() throws Exception {
        assistant.deployApplication();
        assistant.awaitApplicationReadinessOrFail();

        await().atMost(5, TimeUnit.MINUTES)
                .until(AbstractBoosterApplicationTest::isAlive);
    }

    @AfterClass
    public static void cleanup() {
        assistant.cleanup();
    }

    @Test
    @Override
    public void testKillMeEndpoint() {
        super.testKillMeEndpoint();
        // OpenShift should restart the application
        await("Await for the application to restart").atMost(5, TimeUnit.MINUTES)
                .until(AbstractBoosterApplicationTest::isAlive);
    }

}