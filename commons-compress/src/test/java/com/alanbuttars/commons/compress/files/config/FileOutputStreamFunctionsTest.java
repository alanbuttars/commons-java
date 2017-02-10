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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfig;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link FileOutputStreamFunctions}.
 * 
 * @author Alan Buttars
 *
 */
//@RunWith(PowerMockRunner.class)
public class FileOutputStreamFunctionsTest {

	private OutputStream outputStream;
	private Function<OutputStream, FileOutputStreamConfig> configFunction;
	private Function<FileOutputStreamConfig, CompressorOutputStream> streamFunction;

	@Before
	public void setup() {
		outputStream = new ByteArrayOutputStream();
	}

	private void prepare(String archiveType) {
		configFunction = FileOutputStreamFunctions.defaultConfigFunctions().get(archiveType);
		streamFunction = FileOutputStreamFunctions.defaultStreamFunctions().get(archiveType);
	}
}
