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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test class for {@link Argument}.
 * 
 * @author Alan Buttars
 *
 */
public class ArgumentTest {

	@Test
	public void testGetValue_UnwrappedNullValue() {
		assertNull(new Argument(null).getValue());
	}

	@Test
	public void testGetValue_WrappedNullValue() {
		assertNull(new Argument(null, '"').getValue());
	}

	@Test
	public void testGetValue_UnwrappedEmptyValue() {
		assertEquals("", new Argument("").getValue());
	}

	@Test
	public void testGetValue_WrappedEmptyValue() {
		assertEquals("", new Argument("", '"').getValue());
	}

	@Test
	public void testGetValue_UnwrappedNonEmptyValue() {
		assertEquals("blah", new Argument("blah").getValue());
	}

	@Test
	public void testGetValue_WrappedNonEmptyValue() {
		assertEquals("blah", new Argument("blah", '"').getValue());
	}

	@Test
	public void testGetValue_UnwrappedSpaceValue() {
		assertEquals("blah blah", new Argument("blah blah").getValue());
	}

	@Test
	public void testGetValue_WrappedSpaceValue() {
		assertEquals("\"blah blah\"", new Argument("blah blah", '"').getValue());
	}
}
