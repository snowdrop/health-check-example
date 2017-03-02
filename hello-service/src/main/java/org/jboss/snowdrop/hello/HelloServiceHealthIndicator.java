package org.jboss.snowdrop.hello;/*
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

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@Component
public class HelloServiceHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		if (isNameServiceUp()) {
			return Health.up()
					.build();
		}

		return Health.down()
				.withDetail("name-service", "Name service doesn't function correctly")
				.build();
	}

	private boolean isNameServiceUp() {
		RestTemplate restTemplate = new RestTemplate();
		try {
			return restTemplate.exchange(HelloServiceController.NAME_SERVICE_URL, HttpMethod.GET, null, String.class)
					.getStatusCodeValue() == 200;
		} catch (Throwable t) {
			return false;
		}
	}

}
