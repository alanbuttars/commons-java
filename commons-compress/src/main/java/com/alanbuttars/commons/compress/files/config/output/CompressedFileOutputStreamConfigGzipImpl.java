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

import org.apache.commons.compress.compressors.gzip.GzipParameters;

import com.alanbuttars.commons.compress.files.util.CompressedFiles;

/**
 * Extension of {@link CompressorOutputStreamConfig} used for {@link CompressedFiles#GZIP} files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFileOutputStreamConfigGzipImpl extends CompressedFileOutputStreamConfig {
	
	private GzipParameters parameters;

	public CompressedFileOutputStreamConfigGzipImpl(OutputStream outputStream) {
		super(outputStream);
		this.parameters = new GzipParameters();
	}

	public GzipParameters getParameters() {
		return parameters;
	}

	public void setParameters(GzipParameters parameters) {
		this.parameters = parameters;
	}

}
