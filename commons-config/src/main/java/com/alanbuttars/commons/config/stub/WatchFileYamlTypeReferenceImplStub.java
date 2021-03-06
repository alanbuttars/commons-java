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

import com.alanbuttars.commons.config.Configuration;
import com.alanbuttars.commons.config.ConfigurationYamlCollectionImpl;
import com.alanbuttars.commons.config.ConfigurationYamlImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Terminal stub used to create a {@link ConfigurationYamlImpl} from a YAML file which represents some collection.
 * 
 * @author Alan Buttars
 *
 * @param <C>
 *            Collection type belonging to {@link ConfigurationYamlImpl}
 */
public class WatchFileYamlTypeReferenceImplStub<C> {

	protected final String sourceId;
	protected final File file;
	protected final TypeReference<C> typeReference;

	WatchFileYamlTypeReferenceImplStub(String sourceId, File file, TypeReference<C> typeReference) {
		this.sourceId = sourceId;
		this.file = file;
		this.typeReference = typeReference;
	}

	/**
	 * Instructs the created {@link Configuration} to subscribe to events published by the given event bus.
	 * 
	 * @param eventBus
	 *            Non-null event bus
	 * @return The configuration object
	 * @throws IOException
	 *             On I/O parsing the YAML file
	 */
	public ConfigurationYamlCollectionImpl<C> withEventBus(EventBus eventBus) throws IOException {
		verifyNonNull(eventBus, "Event bus must be non-null");
		return new ConfigurationYamlCollectionImpl<>(sourceId, file, eventBus, typeReference);
	}

}
