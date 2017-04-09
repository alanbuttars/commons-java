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
package com.alanbuttars.commons.config.master;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.config.stub.WatchTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Test class for {@link YamlConfigValidator}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(File.class)
public class YamlConfigValidatorTest {

	@Test
	public void testFilesEmpty() {
		validateOk("files-empty");
	}

	@Test
	public void testFilesNull() {
		validateOk("files-null");
	}

	@Test
	public void testFileDirectory() {
		validate("files-user-properties-file-directory",
				"files.user-properties.file '.+commons-java/commons-config/src/test/resources/com/alanbuttars/commons/config/stub' is a directory; it must be a file");
	}

	@Test
	public void testFileEmpty() {
		validate("files-user-properties-file-empty", "files.user-properties.file must be non-empty");
	}

	@Test
	public void testFileNonexistent() {
		validate("files-user-properties-file-nonexistent", "files.user-properties.file '.+commons-java/commons-config/src/test/resources/com/alanbuttars/commons/config/stub/whatever' does not exist");
	}

	@Test
	public void testFileNull() {
		validate("files-user-properties-file-null", "files.user-properties.file must be non-empty");
	}

	@Test
	public void testFileUnreadable() throws Exception {
		String unreadablePath = WatchTest.class.getResource("unreadable.properties").getFile();
		new File(unreadablePath).setReadable(false);
		validate("files-user-properties-file-unreadable",
				"files.user-properties.file '.+commons-java/commons-config/target/test-classes/com/alanbuttars/commons/config/stub/unreadable.properties' is unreadable");
	}

	@Test
	public void testFilePollEveryNull() {
		validate("files-user-properties-poll-every-null", "files.user-properties.poll-every must be greater than 0");
	}

	@Test
	public void testFilePollEveryUnitEmpty() {
		validate("files-user-properties-poll-every-unit-empty", "files.user-properties.poll-every-unit '' is invalid; use one of [milliseconds, seconds, minutes, hours]");
	}

	@Test
	public void testFilePollEveryUnitInvalid() {
		validate("files-user-properties-poll-every-unit-invalid", "files.user-properties.poll-every-unit 'years' is invalid; use one of [milliseconds, seconds, minutes, hours]");
	}

	@Test
	public void testFilePollEveryUnitNull() {
		validate("files-user-properties-poll-every-unit-null", "files.user-properties.poll-every-unit 'null' is invalid; use one of [milliseconds, seconds, minutes, hours]");
	}

	@Test
	public void testMasterEmpty() {
		validate("master-empty", "master must be non-null");
	}

	@Test
	public void testMasterNull() {
		validate("master-null", "master must be non-null");
	}

	@Test
	public void testMasterPollEveryNull() {
		validate("master-poll-every-null", "master.poll-every must be greater than 0");
	}

	@Test
	public void testMasterPollEveryUnitEmpty() {
		validate("master-poll-every-unit-empty", "master.poll-every-unit '' is invalid; use one of [milliseconds, seconds, minutes, hours]");
	}

	@Test
	public void testMasterPollEveryUnitInvalid() {
		validate("master-poll-every-unit-invalid", "master.poll-every-unit 'years' is invalid; use one of [milliseconds, seconds, minutes, hours]");
	}

	@Test
	public void testMasterPollEveryUnitNull() {
		validate("master-poll-every-unit-null", "master.poll-every-unit 'null' is invalid; use one of [milliseconds, seconds, minutes, hours]");
	}

	@Test
	public void testMasterPoolSizeNegative() {
		validate("master-pool-size-negative", "master.pool-size must be greater than 0");
	}

	@Test
	public void testMasterPoolSizeNull() {
		validate("master-pool-size-null", "master.pool-size must be greater than 0");
	}

	@Test
	public void testValid() {
		validateOk("valid");
	}

	@Test
	public void testMultipleErrors() {
		validate("multiple-errors", //
				"master.poll-every-unit 'years' is invalid; use one of [milliseconds, seconds, minutes, hours]\n" + //
						"master.pool-size must be greater than 0\n" + //
						"files.user-properties.file must be non-empty\n" + //
						"files.users-json.poll-every-unit 'years' is invalid; use one of [milliseconds, seconds, minutes, hours]");
	}

	private void validateOk(String alias) {
		YamlConfigValidator.validate(config(alias));
	}

	private void validate(String alias, String expectedError) {
		try {
			YamlConfigValidator.validate(config(alias));
			fail();
		}
		catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().equals(expectedError) || e.getMessage().matches(expectedError));
		}
	}

	private YamlConfig config(String alias) {
		try {
			File configFile = new File(YamlConfigValidatorTest.class.getResource(alias + ".yml").getFile());
			YAMLFactory yamlFactory = new YAMLFactory();
			ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
			return objectMapper.readValue(configFile, YamlConfig.class);
		}
		catch (IOException e) {
			fail();
			throw new RuntimeException(e);
		}
	}

}
