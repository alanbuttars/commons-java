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
package com.alanbuttars.commons.compress.util;

import java.io.File;
import java.io.IOException;

import com.alanbuttars.commons.compress.stub.compress.Compress;
import com.alanbuttars.commons.util.functions.Function;

/**
 * A test function used to verify {@link Compress} operations.
 * 
 * @author Alan Buttars
 *
 */
public abstract class FilesFunction extends Function<File, File> {

	@Override
	public File apply(File original) {
		try {
			return act(original);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public abstract File act(File original) throws IOException;

}
