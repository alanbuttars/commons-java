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
import java.util.Map;

import org.apache.commons.compress.compressors.pack200.Pack200Strategy;

import com.alanbuttars.commons.compress.files.util.CompressedFiles;

/**
 * Extension of {@link CompressorOutputStreamConfig} used for {@link CompressedFiles#PACK200} files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFileOutputStreamConfigPack200Impl extends CompressedFileOutputStreamConfig {

	private Pack200Strategy mode;
	private Map<String, String> properties;

	public CompressedFileOutputStreamConfigPack200Impl(OutputStream outputStream) {
		super(outputStream);
		this.mode = Pack200Strategy.IN_MEMORY;
		this.properties = null;
	}

	public Pack200Strategy getMode() {
		return mode;
	}

	public void setMode(Pack200Strategy mode) {
		this.mode = mode;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

}