/*
 * Copyright (C) Alan Buttars
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
package com.alanbuttars.commons.config.stub;

import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import java.io.File;
import java.io.IOException;

import com.alanbuttars.commons.config.ConfigurationXmlImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;

/**
 * Intermediate stub ultimately used to create a {@link ConfigurationXmlImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileXmlImplStub {

	protected final String sourceId;
	protected final File file;
	protected final EventBus eventBus;

	WatchFileXmlImplStub(String sourceId, File file, EventBus eventBus) {
		this.sourceId = sourceId;
		this.file = file;
		this.eventBus = eventBus;
	}

	/**
	 * Instructs the configuration object to map the contents of the XML file to an object of the given type. For
	 * example:
	 * 
	 * <pre>
	 * ConfigurationXmlImpl&lt;User&gt; config = Watch.xml("/path/to/user.xml").mappedTo(User.class);
	 * User user = config.getValue();
	 * </pre>
	 * 
	 * @param clazz
	 *            Non-null type
	 * @throws IOException
	 *             On I/O parsing the XML file
	 */
	public <T> ConfigurationXmlImpl<T> mappedTo(Class<T> clazz) throws IOException {
		verifyNonNull(clazz, "Clazz must be non-null");
		return new ConfigurationXmlImpl<>(sourceId, file, eventBus, clazz);
	}

}
