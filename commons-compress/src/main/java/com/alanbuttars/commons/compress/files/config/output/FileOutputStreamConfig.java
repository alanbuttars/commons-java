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
package com.alanbuttars.commons.compress.files.config.output;

import java.io.OutputStream;

import org.apache.commons.compress.compressors.CompressorOutputStream;

/**
 * Class which models a config object used to construct instances of {@link CompressorOutputStream}.
 * 
 * @author Alan Buttars
 *
 */
public class FileOutputStreamConfig {

	private final OutputStream outputStream;

	public FileOutputStreamConfig(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}
}