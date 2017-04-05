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

import static com.alanbuttars.commons.util.validators.Arguments.verify;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonEmpty;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNegativeNumber;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;
import static com.alanbuttars.commons.util.validators.Arguments.verifyPositive;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ArgumentsTest {

	@Test
	public void testVerify() {
		verify(true);
		verify(true, "Never throw");
	}

	@Test
	public void testVerifyFails() {
		try {
			verify(false);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}

	@Test
	public void testVerifyFailsWithMessage() {
		try {
			verify(false, "message");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("message", e.getMessage());
		}
	}

	@Test
	public void testVerifyNonNull() {
		verifyNonNull(new Object());
		verifyNonNull(new Object(), "Never throw");
	}

	@Test
	public void testVerifyNonNullFails() {
		try {
			verifyNonNull(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}

	@Test
	public void testVerifyNonNullFailsWithMessage() {
		try {
			verifyNonNull(null, "message");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("message", e.getMessage());
		}
	}

	@Test
	public void testVerifyNonEmpty() {
		verifyNonEmpty("present");
		verifyNonEmpty("present", "Never throw");
	}

	@Test
	public void testVerifyNonEmptyFailsOnNull() {
		try {
			verifyNonEmpty(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}

	@Test
	public void testVerifyNonEmptyFailsOnNullWithMessage() {
		try {
			verifyNonEmpty(null, "message");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("message", e.getMessage());
		}
	}

	@Test
	public void testVerifyNonEmptyFailsOnEmpty() {
		try {
			verifyNonEmpty("");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}

	@Test
	public void testVerifyNonEmptyFailsOnEmptyWithMessage() {
		try {
			verifyNonEmpty("", "message");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("message", e.getMessage());
		}
	}

	@Test
	public void testVerifyBlankFailsOnEmpty() {
		try {
			verifyNonEmpty(" ");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}

	@Test
	public void testVerifyBlankFailsOnEmptyWithMessage() {
		try {
			verifyNonEmpty(" ", "message");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("message", e.getMessage());
		}
	}

	@Test
	public void testVerifyPositive() {
		verifyPositive(1);
		verifyPositive(1, "message");
		verifyPositive(1L);
		verifyPositive(1L, "message");
		verifyPositive(1.0);
		verifyPositive(1.0, "message");
	}

	@Test
	public void testVerifyPositiveFails() {
		try {
			verifyPositive(0);
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}

	@Test
	public void testVerifyPositiveFailsWithMessage() {
		try {
			verifyPositive(-1, "message");
		}
		catch (IllegalArgumentException e) {
			assertEquals("message", e.getMessage());
		}
	}

	@Test
	public void testVerifyNonNegative() {
		verifyNonNegativeNumber(0);
		verifyNonNegativeNumber(1);
		verifyNonNegativeNumber(1, "message");
		verifyNonNegativeNumber(0L);
		verifyNonNegativeNumber(1L);
		verifyNonNegativeNumber(1L, "message");
		verifyNonNegativeNumber(0.0);
		verifyNonNegativeNumber(1.0);
		verifyNonNegativeNumber(1.0, "message");
	}

	@Test
	public void testVerifyNonNegativeFails() {
		try {
			verifyNonNegativeNumber(-1);
		}
		catch (IllegalArgumentException e) {
			assertNull(e.getMessage());
		}
	}

	@Test
	public void testVerifyNonNegativeFailsWithMessage() {
		try {
			verifyNonNegativeNumber(-1, "message");
		}
		catch (IllegalArgumentException e) {
			assertEquals("message", e.getMessage());
		}
	}

}
