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
import com.alanbuttars.commons.config.ConfigurationJsonCollectionImpl;
import com.alanbuttars.commons.config.ConfigurationJsonImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Terminal stub used to create a {@link ConfigurationJsonImpl} from a JSON file which represents some collection.
 * 
 * @author Alan Buttars
 *
 * @param <C>
 *            Collection type belonging to {@link ConfigurationJsonImpl}
 */
public class WatchFileJsonTypeReferenceImplStub<C> {

	protected final File file;
	protected final TypeReference<C> typeReference;

	WatchFileJsonTypeReferenceImplStub(File file, TypeReference<C> typeReference) {
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
	 *             On I/O parsing the JSON file
	 */
	public ConfigurationJsonCollectionImpl<C> withEventBus(EventBus eventBus) throws IOException {
		verifyNonNull(eventBus, "Event bus must be non-null");
		return new ConfigurationJsonCollectionImpl<>(file, eventBus, typeReference);
	}

}
