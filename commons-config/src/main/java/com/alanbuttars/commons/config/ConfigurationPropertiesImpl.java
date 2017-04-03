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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * {@link Configuration} implementation used for Java .properties files
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationPropertiesImpl extends ConfigurationAbstractImpl<Properties> {

	public ConfigurationPropertiesImpl(File configFile, EventBus eventBus) throws IOException {
		super(configFile);
		initEventBus(eventBus);
	}

	/**
	 * Retrieves the <code>byte</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public byte getByte(String key, byte defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Byte.parseByte(value);
		}
		return defaultValue;
	}

	/**
	 * Retrieves the <code>short</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public short getShort(String key, short defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Short.parseShort(value);
		}
		return defaultValue;
	}

	/**
	 * Retrieves the <code>int</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public int getInt(String key, int defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Integer.parseInt(value);
		}
		return defaultValue;
	}

	/**
	 * Retrieves the <code>long</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public long getLong(String key, long defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Long.parseLong(value);
		}
		return defaultValue;
	}

	/**
	 * Retrieves the <code>float</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public float getFloat(String key, float defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Float.parseFloat(value);
		}
		return defaultValue;
	}

	/**
	 * Retrieves the <code>double</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public double getDouble(String key, double defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Double.parseDouble(value);
		}
		return defaultValue;
	}

	/**
	 * Retrieves the <code>boolean</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return Boolean.parseBoolean(value);
		}
		return defaultValue;
	}

	/**
	 * Retrieves the <code>char</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public char getChar(String key, char defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return value.charAt(0);
		}
		return defaultValue;
	}

	/**
	 * Retrieves the <code>String</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The nullable default value
	 */
	public String getString(String key, String defaultValue) {
		verifyNonEmpty(key, "Key must be non-empty");
		String value = getValue(key);
		if (isPresent(value)) {
			return value;
		}
		return defaultValue;
	}

	/**
	 * Retrieves the configuration value associated with the given <code>key</code> by transforming the value using the
	 * <code>valueMappingFunction</code>.
	 * 
	 * @param <V>
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param valueMappingFunction
	 *            Non-null mapping function transforming the configuration value to the desired type
	 */
	public <V> V getValue(String key, Function<String, V> valueMappingFunction) {
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
	@VisibleForTesting
	protected String getValue(String key) {
		return getValue().getProperty(key);
	}

	@Override
	public Properties load(File configFile) throws IOException {
		Properties properties = new Properties();
		try (Reader reader = new FileReader(configFile)) {
			properties.load(reader);
		}
		return properties;
	}

}
