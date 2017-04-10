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

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.util.FileTestHelper;

/**
 * Test class for {@link ConfigurationDirectoryImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationDirectoryImplTest {

	private static ConfigurationDirectoryImpl config;
	private static File file1;
	private static File file2;
	private static File file3;

	@BeforeClass
	public static void setup() throws IOException {
		File directory = FileTestHelper.directory();
		file1 = new File(directory, "file1.txt");
		file1.createNewFile();

		File file2Parent = new File(directory, "nest");
		file2Parent.mkdir();
		file2 = new File(file2Parent, "file2.txt");
		file2.createNewFile();

		File file3Parent = new File(file2Parent, "nest");
		file3Parent.mkdir();
		file3 = new File(file3Parent, "file3.txt");
		file3.createNewFile();

		config = new ConfigurationDirectoryImpl("test", directory, mock(EventBus.class));
	}

	@Test
	public void testGetDirectDescendent() {
		assertEquals(file1, config.getValue().get("file1.txt"));
	}
	
	@Test
	public void testSingleNestedDescendent() {
		assertEquals(file2, config.getValue().get("nest/file2.txt"));
	}
	
	@Test
	public void testDoubleNestedDescendent() {
		assertEquals(file3, config.getValue().get("nest/nest/file3.txt"));
	}
}
