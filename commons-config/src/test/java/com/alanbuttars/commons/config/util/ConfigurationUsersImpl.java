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
package com.alanbuttars.commons.config.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.alanbuttars.commons.config.Configuration;
import com.alanbuttars.commons.config.ConfigurationAbstractImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.stub.User;

/**
 * A custom {@link Configuration} for testing purposes.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationUsersImpl extends ConfigurationAbstractImpl<List<User>> {

	public ConfigurationUsersImpl(String sourceId, File configFile, EventBus eventBus) throws IOException {
		super(sourceId, eventBus);
		init(configFile);
	}

	@Override
	public List<User> load(File configFile) throws IOException {
		List<User> users = new ArrayList<>();
		try (Scanner scanner = new Scanner(configFile)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] lineParts = line.split(",");
				User user = new User();
				user.setFirstName(lineParts[0]);
				user.setLastName(lineParts[1]);
				users.add(user);
			}
		}
		return users;
	}
}
