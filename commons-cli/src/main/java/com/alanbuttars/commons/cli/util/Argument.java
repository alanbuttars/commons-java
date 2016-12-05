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
package com.alanbuttars.commons.cli.util;

/**
 * Models a command line argument.
 * 
 *
 * @author Alan Buttars
 *
 */
public class Argument {

	private final String value;
	private final Character wrapper;

	/**
	 * Creates an argument whose value will not be wrapped when deployed to the command line.
	 *
	 * @param value
	 *            Nullable value
	 */
	public Argument(String value) {
		this(value, null);
	}

	/**
	 * Creates an argument whose value will be wrapped by a given character if the value contains a space.
	 *
	 * @param value
	 *            Nullable value
	 * @param wrapper
	 *            Nullable wrapper character
	 */
	public Argument(String value, Character wrapper) {
		this.value = value;
		this.wrapper = wrapper;
	}

	/**
	 * Returns the argument value, which may be wrapped.
	 */
	public String getValue() {
		if (value != null) {
			if (wrapper != null) {
				if (value.contains(" ")) {
					return wrapper + value + wrapper;
				}
			}
		}
		return value;
	}

}
