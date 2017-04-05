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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.alanbuttars.commons.config.stub.Watch;
import com.alanbuttars.commons.config.util.FileTestHelper;
import com.google.common.base.Joiner;

/**
 * Abstract integration test for {@link Configuration} implementations.
 * 
 * @author Alan Buttars
 * @param <T>
 *
 */
abstract class ConfigurationIntegrationTest<C extends ConfigurationAbstractImpl<T>, T> {

	protected EventBus eventBus;
	protected C config;
	protected File configFile;
	private File yamlConfig;

	public void setup(String content) throws IOException {
		this.eventBus = new EventBusSyncImpl();

		this.configFile = FileTestHelper.file();
		FileTestHelper.write(content, configFile);

		this.yamlConfig = FileTestHelper.file();
		updateConfigFile(configFile);

		Watch watch = Watch.config(yamlConfig.getAbsolutePath()).withEventBus(eventBus);

		config = config(watch, "config");
	}

	/**
	 * The configuration object to be tested.
	 */
	protected abstract C config(Watch watch, String sourceId) throws IOException;

	protected void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		}
		catch (InterruptedException e) {
			fail(e.getMessage());
		}
	}

	protected void updateMasterPollEvery(int pollEvery) throws IOException {
		writeConfigFile(configFile, pollEvery, 1);
	}

	protected void updateConfigPollEvery(int pollEvery) throws IOException {
		writeConfigFile(configFile, 1, pollEvery);
	}

	protected void updateConfigFile(File configFile) throws IOException {
		writeConfigFile(configFile, 1, 1);
	}

	private void writeConfigFile(File configFile, int masterPollEvery, int configPollEvery) throws IOException {
		String content = Joiner.on("\n").join(//
				"master:", //
				"  poll-every: " + masterPollEvery, //
				"  poll-every-unit: 'seconds'", //
				"  pool-size: 10", //
				"files:", //
				"  config:", //
				"    file: '" + configFile.getAbsolutePath() + "'", //
				"    poll-every: " + configPollEvery, //
				"    poll-every-unit: 'seconds'");
		FileTestHelper.write(content, yamlConfig);
	}

}
