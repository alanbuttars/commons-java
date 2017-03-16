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

import com.alanbuttars.commons.util.functions.Function;

/**
 * Models a configuration object with the flexibility to return multiple configuration value types.
 * 
 * @author Alan Buttars
 *
 */
public interface Configuration<V1> {

	/**
	 * Retrieves the <code>byte</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public byte getByte(String key, byte defaultValue);

	/**
	 * Retrieves the <code>short</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public short getShort(String key, short defaultValue);

	/**
	 * Retrieves the <code>int</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public int getInt(String key, int defaultValue);

	/**
	 * Retrieves the <code>long</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public long getLong(String key, long defaultValue);

	/**
	 * Retrieves the <code>float</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public float getFloat(String key, float defaultValue);

	/**
	 * Retrieves the <code>double</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public double getDouble(String key, double defaultValue);

	/**
	 * Retrieves the <code>boolean</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public boolean getBoolean(String key, boolean defaultValue);

	/**
	 * Retrieves the <code>char</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The default value
	 */
	public char getChar(String key, char defaultValue);

	/**
	 * Retrieves the <code>String</code> configuration value associated with the given <code>key</code>, or the
	 * <code>defaultValue</code> if no value was configured.
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param defaultValue
	 *            The nullable default value
	 */
	public String getString(String key, String defaultValue);

	/**
	 * Retrieves the configuration value associated with the given <code>key</code> by transforming the value using the
	 * <code>valueMappingFunction</code>.
	 * 
	 * @param <V2>
	 * 
	 * @param key
	 *            Non-null configuration key
	 * @param valueMappingFunction
	 *            Non-null mapping function transforming the configuration value to the desired type
	 */
	public <V2> V2 getValue(String key, Function<V1, V2> valueMappingFunction);
}
