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
package com.alanbuttars.commons.compress.files.config.input;

import java.io.InputStream;

import org.apache.commons.compress.compressors.deflate.DeflateParameters;

import com.alanbuttars.commons.compress.files.util.Files;

/**
 * Extension of {@link FileInputStreamConfig} used for {@link Files#DEFLATE} files.
 * 
 * @author Alan Buttars
 *
 */
public class FileInputStreamConfigDeflateImpl extends FileInputStreamConfig {

	private DeflateParameters parameters;

	public FileInputStreamConfigDeflateImpl(InputStream inputStream) {
		super(inputStream);
		this.parameters = new DeflateParameters();
	}

	public DeflateParameters getParameters() {
		return parameters;
	}

	public void setParameters(DeflateParameters parameters) {
		this.parameters = parameters;
	}

}