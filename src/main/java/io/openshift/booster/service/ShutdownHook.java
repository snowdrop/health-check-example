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

import org.apache.catalina.Context;

interface ShutdownHook {
	void shutdown() throws Exception;
}

class TomcatShutdownHook implements ShutdownHook {
	private Context context;

	TomcatShutdownHook(Context context) {
		this.context = context;
	}

	public void shutdown() throws Exception {
		new Thread(() -> {
			try {
				context.stop();
			}
			catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}).start();
	}
}

