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

import org.tukaani.xz.LZMA2Options;

import com.alanbuttars.commons.compress.files.util.CompressedFiles;

/**
 * Extension of {@link CompressorOutputStreamConfig} used for {@link CompressedFiles#XZ} files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFileOutputStreamConfigXzImpl extends CompressedFileOutputStreamConfig {

	private int preset;

	public CompressedFileOutputStreamConfigXzImpl(OutputStream outputStream) {
		super(outputStream);
		this.preset = LZMA2Options.PRESET_DEFAULT;
	}

	public int getPreset() {
		return preset;
	}

	public void setPreset(int preset) {
		this.preset = preset;
	}

}
