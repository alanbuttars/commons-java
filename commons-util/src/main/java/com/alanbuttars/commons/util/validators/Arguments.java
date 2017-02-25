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
package com.alanbuttars.commons.util.validators;

public class Arguments {
	
	private Arguments() {}

	public static void verify(boolean expression) {
		verify(expression, null);
	}

	public static void verify(boolean expression, String message) {
		if (!expression) {
			if (message == null) {
				throw new IllegalArgumentException();
			}
			throw new IllegalArgumentException(message);
		}
	}

	public static void verifyNonNull(Object argument) {
		verifyNonNull(argument, null);
	}

	public static void verifyNonNull(Object argument, String message) {
		verify(argument != null, message);
	}

	public static void verifyNonEmpty(String argument) {
		verifyNonEmpty(argument, null);
	}

	public static void verifyNonEmpty(String argument, String message) {
		verify(argument != null && !argument.isEmpty(), message);
	}
}
