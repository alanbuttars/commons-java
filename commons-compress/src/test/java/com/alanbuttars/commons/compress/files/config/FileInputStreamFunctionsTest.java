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
package com.alanbuttars.commons.compress.files.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfig;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link FileInputStreamFunctions}.
 * 
 * @author Alan Buttars
 *
 */
//@RunWith(PowerMockRunner.class)
public class FileInputStreamFunctionsTest {

	private InputStream inputStream;
	private Function<InputStream, FileInputStreamConfig> configFunction;
	private Function<FileInputStreamConfig, CompressorInputStream> streamFunction;

	@Before
	public void setup() {
		inputStream = new ByteArrayInputStream("a\nb\nc".getBytes());
	}

	private void prepare(String archiveType) {
		configFunction = FileInputStreamFunctions.defaultConfigFunctions().get(archiveType);
		streamFunction = FileInputStreamFunctions.defaultStreamFunctions().get(archiveType);
	}
}
