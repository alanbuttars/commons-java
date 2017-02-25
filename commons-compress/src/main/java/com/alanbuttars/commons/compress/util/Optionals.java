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

/**
 * Utility class for handling optional values.
 * 
 * @author Alan Buttars
 *
 */
public class Optionals {

	/**
	 * Returns the <code>primary</code> if is non-null; the <code>secondary</code> otherwise.
	 */
	public static <T> T or(T primary, T secondary) {
		if (primary != null) {
			return primary;
		}
		return secondary;
	}

	/**
	 * Returns the <code>primary</code> if it is positive; the <code>secondary</code> otherwise.
	 */
	public static short or(short primary, short secondary) {
		if (primary > 0) {
			return primary;
		}
		return secondary;
	}

	/**
	 * Returns the <code>primary</code> if it is positive; the <code>secondary</code> otherwise.
	 */
	public static int or(int primary, int secondary) {
		if (primary > 0) {
			return primary;
		}
		return secondary;
	}

}
