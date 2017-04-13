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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.alanbuttars.commons.config.ConfigurationClientsImpl.Client;
import com.alanbuttars.commons.config.eventbus.EventBus;

public class ConfigurationClientsImpl extends ConfigurationAbstractImpl<List<Client>> {

	public ConfigurationClientsImpl(String sourceId, File configFile, EventBus eventBus) throws IOException {
		super(sourceId, eventBus);
		init(configFile);
	}

	@Override
	public List<Client> load(File configFile) throws IOException {
		List<Client> clients = new ArrayList<>();
		try (Scanner scanner = new Scanner(configFile)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] lineParts = line.split(",");
				Client client = new Client(lineParts[0], lineParts[1], lineParts[2]);
				clients.add(client);
			}
		}
		return clients;
	}

	static class Client {
		public Client(String a, String b, String c) {

		}
	}
}
