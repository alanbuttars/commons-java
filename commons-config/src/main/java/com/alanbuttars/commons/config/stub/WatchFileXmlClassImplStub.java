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

import javax.xml.bind.JAXBException;

import com.alanbuttars.commons.config.Configuration;
import com.alanbuttars.commons.config.ConfigurationXmlImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;

/**
 * Terminal stub used to create a {@link ConfigurationXmlImpl} from a XML file which represents some object.
 * 
 * @author Alan Buttars
 *
 * @param <T>
 *            Type belonging to {@link ConfigurationXmlImpl}
 */
public class WatchFileXmlClassImplStub<T> {

	protected final File file;
	protected final Class<T> clazz;

	WatchFileXmlClassImplStub(File file, Class<T> clazz) {
		this.file = file;
		this.clazz = clazz;
	}

	/**
	 * Instructs the created {@link Configuration} to subscribe to events published by the given event bus.
	 * 
	 * @param eventBus
	 *            Non-null event bus
	 * @return The configuration object
	 * @throws IOException
	 *             On I/O parsing the XML file
	 */
	public ConfigurationXmlImpl<T> withEventBus(EventBus eventBus) throws IOException, JAXBException {
		verifyNonNull(eventBus, "Event bus must be non-null");
		return new ConfigurationXmlImpl<>(file, eventBus, clazz);
	}

}
