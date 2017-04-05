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

import com.alanbuttars.commons.config.ConfigurationJsonCollectionImpl;
import com.alanbuttars.commons.config.ConfigurationJsonImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Intermediate stub ultimately used to create a {@link ConfigurationJsonImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileJsonImplStub {

	protected final String sourceId;
	protected final File file;
	protected final EventBus eventBus;

	WatchFileJsonImplStub(String sourceId, File file, EventBus eventBus) {
		this.sourceId = sourceId;
		this.file = file;
		this.eventBus = eventBus;
	}

	/**
	 * Instructs the configuration object to map the contents of the JSON file to an object of the given type. For
	 * example:
	 * 
	 * <pre>
	 * ConfigurationJsonImpl&lt;User&gt; config = Watch.json("/path/to/user.json").mappedTo(User.class);
	 * User user = config.getValue();
	 * </pre>
	 * 
	 * @param clazz
	 *            Non-null type
	 * @throws IOException
	 *             On I/O parsing the JSON file
	 */
	public <T> ConfigurationJsonImpl<T> mappedTo(Class<T> clazz) throws IOException {
		verifyNonNull(clazz, "Clazz must be non-null");
		return new ConfigurationJsonImpl<>(sourceId, file, eventBus, clazz);
	}

	/**
	 * Instructs the configuration object to map the contents of the JSON file to an object of the given type reference.
	 * This method should be used when the JSON file contains a collection. For example:
	 * 
	 * <pre>
	 * ConfigurationJsonImpl&lt;List&lt;User&gt;&gt; config = Watch.json("/path/to/users.json").mappedTo(new TypeReference&lt;List&lt;User&gt;&gt;() {
	 * });
	 * List&lt;User&gt; users = config.getValue();
	 * </pre>
	 * 
	 * @param typeReference
	 *            Non-null type reference
	 * @throws IOException
	 *             On I/O parsing the JSON file
	 */
	public <C> ConfigurationJsonCollectionImpl<C> mappedTo(TypeReference<C> typeReference) throws IOException {
		verifyNonNull(typeReference, "Type reference must be non-null");
		return new ConfigurationJsonCollectionImpl<>(sourceId, file, eventBus, typeReference);
	}
}
