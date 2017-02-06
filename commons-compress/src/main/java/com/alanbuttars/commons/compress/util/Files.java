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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility functions class for files.
 * 
 * @author Alan Buttars
 *
 */
public class Files {

	public static String BZIP2 = "bzip2";
	public static String DEFLATE = "deflate";
	public static String GZIP = "gzip";
	public static String LZMA = "lzma";
	public static String PACK200 = "pack200";
	public static String SNAPPY = "snappy";
	public static String XZ = "xz";
	public static String Z = "z";

	static Set<String> COMPRESS_TYPES = new HashSet<>(Arrays.asList(//
			BZIP2, //
			DEFLATE, //
			GZIP, //
			LZMA, //
			PACK200, //
			SNAPPY, //
			XZ, //
			Z));

}
