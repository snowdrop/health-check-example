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
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;

import java.util.concurrent.TimeUnit;

import com.jayway.restassured.RestAssured;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) // needed due to shutdown
public abstract class AbstractBoosterApplicationTest {

	@Test
	public void testGreetingEndpoint() {
		RestAssured.basePath = "/api/greeting";
		when().get()
			.then()
			.statusCode(200)
			.body("content", is("Hello, World!"));
	}

	@Test
	public void testGreetingEndpointWithNameParameter() {
		RestAssured.basePath = "/api/greeting";
		given().param("name", "John")
			.when()
			.get()
			.then()
			.statusCode(200)
			.body("content", is("Hello, John!"));
	}

	@Test
	public void testShutdown() {
		RestAssured.basePath = "/api/killme";
		when().get()
			.then()
			.statusCode(200);

		RestAssured.basePath = "/health";
		await().atMost(5, TimeUnit.MINUTES)
			.until(() -> {
				try {
					int statusCode = get().statusCode();
					return (statusCode != 200);
				}
				catch (Exception e) {
					return false;
				}
			});
	}
}
