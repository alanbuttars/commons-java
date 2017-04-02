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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Test;

import com.alanbuttars.commons.config.stub.User;
import com.alanbuttars.commons.config.stub.Watch;
import com.alanbuttars.commons.config.stub.WatchTestHelper;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Integration test class for {@link ConfigurationYamlImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationYamlImplIntegrationTest extends AbstractIntegrationTest {

	@Test
	public void testObject() throws IOException {
		ConfigurationYamlImpl<User> config = Watch.config(WatchTestHelper.getYaml()).yaml("user-yaml", User.class);
		verifyHarry(config.getValue());
	}

	@Test
	public void testList() throws IOException {
		ConfigurationYamlImpl<List<User>> config = Watch.config(WatchTestHelper.getYaml()).yaml("users-yaml", new TypeReference<List<User>>() {
		});
		verifyHarry(config.getValue().get(0));
		verifySherman(config.getValue().get(1));
	}

	@Test
	public void testSet() throws IOException {
		ConfigurationYamlImpl<LinkedHashSet<User>> config = Watch.config(WatchTestHelper.getYaml()).yaml("users-yaml", new TypeReference<LinkedHashSet<User>>() {
		});
		Iterator<User> iterator = config.getValue().iterator();
		verifyHarry(iterator.next());
		verifySherman(iterator.next());
	}

}
