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
package com.alanbuttars.commons.cli.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@link CommandLineResponse}.
 * 
 * @author Alan Buttars
 *
 */
public class CommandLineResponseTest {

	@Test
	public void testConstructorSuccess() {
		CommandLineResponse response = CommandLineResponse.success(1, "info", "error");
		assertEquals(1, response.getExitCode());
		assertEquals("info", response.getInfoStream());
		assertEquals("error", response.getErrorStream());
		assertTrue(response.succeeded());
		assertNull(response.getException());
	}

	@Test
	public void testConstructorFailure() {
		CommandLineResponse response = CommandLineResponse.failure(1, "info", "error");
		assertEquals(1, response.getExitCode());
		assertEquals("info", response.getInfoStream());
		assertEquals("error", response.getErrorStream());
		assertFalse(response.succeeded());
		assertNull(response.getException());
	}

	@Test
	public void testConstructorFailureWithException() {
		CommandLineResponse response = CommandLineResponse.failure(new RuntimeException("exception"));
		assertEquals(CommandLineResponse.EXCEPTION_THROWN_EXIT_CODE, response.getExitCode());
		assertNull(response.getInfoStream());
		assertNull(response.getErrorStream());
		assertFalse(response.succeeded());
		assertNotNull(response.getException());
	}

}
