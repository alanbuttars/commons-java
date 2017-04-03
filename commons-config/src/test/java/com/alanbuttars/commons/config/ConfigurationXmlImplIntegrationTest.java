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

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.alanbuttars.commons.config.stub.User;
import com.alanbuttars.commons.config.stub.Watch;
import com.alanbuttars.commons.config.stub.WatchTestHelper;

/**
 * Test class for {@link ConfigurationXmlImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationXmlImplIntegrationTest extends ConfigurationAbstractIntegrationTest {

	private EventBus eventBus;
	private Watch watch;

	@Before
	public void setup() throws IOException {
		this.eventBus = new EventBusSyncImpl();
		this.watch = Watch.config(WatchTestHelper.getYaml()).withEventBus(eventBus);
	}

	@Test
	public void testObject() throws IOException, JAXBException {
		ConfigurationXmlImpl<User> config = watch.xml("user-xml").mappedTo(User.class).withEventBus(eventBus);
		verifyHarry(config.getValue());
	}

}