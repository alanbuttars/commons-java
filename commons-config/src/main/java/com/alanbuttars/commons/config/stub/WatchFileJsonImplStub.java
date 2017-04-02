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

import com.alanbuttars.commons.config.ConfigurationJsonImpl;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Intermediate stub ultimately used to create a {@link ConfigurationJsonImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileJsonImplStub {

	protected final File file;

	WatchFileJsonImplStub(File file) {
		this.file = file;
	}

	/**
	 * Instructs the configuration object to map the contents of the JSON file to an object of the given type. For
	 * example:
	 * 
	 * <pre>
	 * ConfigurationJsonImpl&lt;User&gt; config = Watch.json("/path/to/user.json").mappedTo(User.class).withEventBus();
	 * User user = config.getValue();
	 * </pre>
	 * 
	 * @param clazz
	 *            Non-null type
	 */
	public <T> WatchFileJsonClassImplStub<T> mappedTo(Class<T> clazz) {
		verifyNonNull(clazz, "Clazz must be non-null");
		return new WatchFileJsonClassImplStub<>(file, clazz);
	}

	/**
	 * Instructs the configuration object to map the contents of the JSON file to an object of the given type reference.
	 * This method should be used when the JSON file contains a collection. For example:
	 * 
	 * <pre>
	 * ConfigurationJsonImpl&lt;List&lt;User&gt;&gt; config = Watch.json("/path/to/users.json").mappedTo(new TypeReference&lt;List&lt;User&gt;&gt;() {
	 * }).withEventBus();
	 * List&lt;User&gt; users = config.getValue();
	 * </pre>
	 * 
	 * @param typeReference
	 *            Non-null type reference
	 */
	public <C> WatchFileJsonTypeReferenceImplStub<C> mappedTo(TypeReference<C> typeReference) {
		verifyNonNull(typeReference, "Type reference must be non-null");
		return new WatchFileJsonTypeReferenceImplStub<>(file, typeReference);
	}
}
