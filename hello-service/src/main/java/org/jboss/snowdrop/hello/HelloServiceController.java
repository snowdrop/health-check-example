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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@Controller
public class HelloServiceController {

	static final String NAME_SERVICE_URL = "http://name-service";

	@GetMapping
	@ResponseBody
	public String getGreeting() {
		return String.format("Hello %s!", getName());
	}

	private String getName() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(NAME_SERVICE_URL, String.class);
	}

}
