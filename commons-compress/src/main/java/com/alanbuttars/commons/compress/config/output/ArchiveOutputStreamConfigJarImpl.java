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
package com.alanbuttars.commons.compress.config.output;

import java.io.OutputStream;

import com.alanbuttars.commons.compress.util.Archives;

/**
 * Extension of {@link ArchiveOutputStreamConfig} used for {@link Archives#JAR} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamConfigJarImpl extends ArchiveOutputStreamConfigZipImpl {

	public ArchiveOutputStreamConfigJarImpl(OutputStream outputStream) {
		super(outputStream);
	}

}