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
package com.alanbuttars.commons.config.master;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.alanbuttars.commons.config.master.TimeUnits;

/**
 * Test class for {@link TimeUnits}.
 * 
 * @author Alan Buttars
 *
 */
public class TimeUnitsTest {

	@Test
	public void testNull() {
		try {
			TimeUnits.fromPollEveryUnit(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("poll-every-unit must be non-null", e.getMessage());
		}
	}

	@Test
	public void testEmpty() {
		try {
			TimeUnits.fromPollEveryUnit("");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("poll-every-unit must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testBlank() {
		try {
			TimeUnits.fromPollEveryUnit(" ");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("poll-every-unit must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testInvalid() {
		try {
			TimeUnits.fromPollEveryUnit("days");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("poll-every-unit 'days' is not a valid value: [milliseconds, seconds, minutes, hours]", e.getMessage());
		}
	}

	@Test
	public void testLowerCase() {
		assertEquals(TimeUnit.MILLISECONDS, TimeUnits.fromPollEveryUnit("milliseconds"));
		assertEquals(TimeUnit.SECONDS, TimeUnits.fromPollEveryUnit("seconds"));
		assertEquals(TimeUnit.MINUTES, TimeUnits.fromPollEveryUnit("minutes"));
		assertEquals(TimeUnit.HOURS, TimeUnits.fromPollEveryUnit("hours"));
	}

	@Test
	public void testUpperCase() {
		assertEquals(TimeUnit.MILLISECONDS, TimeUnits.fromPollEveryUnit("MILLISECONDS"));
		assertEquals(TimeUnit.SECONDS, TimeUnits.fromPollEveryUnit("SECONDS"));
		assertEquals(TimeUnit.MINUTES, TimeUnits.fromPollEveryUnit("MINUTES"));
		assertEquals(TimeUnit.HOURS, TimeUnits.fromPollEveryUnit("HOURS"));
	}

	@Test
	public void testLowerCaseWithoutS() {
		assertEquals(TimeUnit.MILLISECONDS, TimeUnits.fromPollEveryUnit("millisecond"));
		assertEquals(TimeUnit.SECONDS, TimeUnits.fromPollEveryUnit("second"));
		assertEquals(TimeUnit.MINUTES, TimeUnits.fromPollEveryUnit("minute"));
		assertEquals(TimeUnit.HOURS, TimeUnits.fromPollEveryUnit("hour"));
	}

	@Test
	public void testUpperCaseWithoutS() {
		assertEquals(TimeUnit.MILLISECONDS, TimeUnits.fromPollEveryUnit("MILLISECOND"));
		assertEquals(TimeUnit.SECONDS, TimeUnits.fromPollEveryUnit("SECOND"));
		assertEquals(TimeUnit.MINUTES, TimeUnits.fromPollEveryUnit("MINUTE"));
		assertEquals(TimeUnit.HOURS, TimeUnits.fromPollEveryUnit("HOUR"));
	}
}
