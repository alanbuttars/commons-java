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
package com.alanbuttars.commons.config;

import static com.alanbuttars.commons.util.validators.Arguments.verifyNonEmpty;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import com.alanbuttars.commons.util.functions.Function;

/**
 * An implementation of {@link Configuration} which assumes that all values are {@link String}s, such as a basic Java
 * properties file.
 * 
 * @author Alan Buttars
 *
 */
public abstract class ConfigurationStringValuesImpl implements Configuration<String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getByte(String key, byte defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Byte.parseByte(value);
		}
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short getShort(String key, short defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Short.parseShort(value);
		}
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(String key, int defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Integer.parseInt(value);
		}
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(String key, long defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Long.parseLong(value);
		}
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(String key, float defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Float.parseFloat(value);
		}
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(String key, double defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Double.parseDouble(value);
		}
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Boolean.parseBoolean(value);
		}
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public char getChar(String key, char defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return value.charAt(0);
		}
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(String key, String defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return value;
		}
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V2> V2 getValue(String key, Function<String, V2> valueMappingFunction) {
		verifyNonEmpty(key, "Key must be non-empty");
		verifyNonNull(valueMappingFunction, "Value mapping function must be non-null");
		String value = getValue(key);
		return valueMappingFunction.apply(value);
	}

	private boolean isPresent(String value) {
		return value != null && value.length() > 0;
	}

	/**
	 * Fetches the configuration value.
	 * 
	 * @param key
	 *            Non-null key
	 * @return Null if no configuration value exists
	 */
	protected abstract String getValue(String key);

}
