/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.openshift.booster.service;

import java.util.logging.Logger;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ShutdownController {
	private static Logger log = Logger.getLogger(ShutdownController.class.getName());

	private ShutdownHook shutdownHook;

	@RequestMapping("/killme")
	public void shutdown() throws Exception {
		log.info("Shutting down server ...");
		shutdownHook.shutdown();
	}

	@Bean
	public EmbeddedServletContainerCustomizer getContainerCustomizer() {
		return container -> {
			if (container instanceof TomcatEmbeddedServletContainerFactory) {
				TomcatEmbeddedServletContainerFactory tcContainer = (TomcatEmbeddedServletContainerFactory) container;
				tcContainer.addContextCustomizers((TomcatContextCustomizer) context -> ShutdownController.this.shutdownHook = new TomcatShutdownHook(context));
			} else {
				ShutdownController.this.shutdownHook = () -> {
					System.exit(0); // TODO -- add other web servers support instead, if needed
				};
			}
		};
	}
}
