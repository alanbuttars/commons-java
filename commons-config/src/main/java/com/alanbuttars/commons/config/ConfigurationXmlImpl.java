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
package com.alanbuttars.commons.config;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.alanbuttars.commons.config.eventbus.EventBus;

/**
 * {@link Configuration} implementation used for XML files. Instances of this class accept a single XML file which is
 * mapped to a given class type.
 * 
 * @author Alan Buttars
 *
 * @param <T>
 *            Class type to which the given XML file will be mapped
 */
public class ConfigurationXmlImpl<T> extends ConfigurationAbstractImpl<T> {

	private final Class<T> clazz;

	public ConfigurationXmlImpl(File configFile, EventBus eventBus, Class<T> clazz) throws IOException, JAXBException {
		super(configFile);
		this.clazz = clazz;
		initEventBus(eventBus);
	}

	@Override
	public T load(File configFile) throws IOException {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (T) unmarshaller.unmarshal(configFile);
		}
		catch (JAXBException e) {
			throw new IOException(e);
		}
	}

}
