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
package com.alanbuttars.commons.config.stub;

import static com.alanbuttars.commons.util.validators.Arguments.verify;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonEmpty;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Simple utility class for {@link TimeUnit}.
 * 
 * @author Alan Buttars
 *
 */
class TimeUnits {

	private static final Map<String, TimeUnit> VALID_TIME_UNITS;

	static {
		VALID_TIME_UNITS = new LinkedHashMap<>();
		VALID_TIME_UNITS.put("milliseconds", TimeUnit.MILLISECONDS);
		VALID_TIME_UNITS.put("seconds", TimeUnit.SECONDS);
		VALID_TIME_UNITS.put("minutes", TimeUnit.MINUTES);
		VALID_TIME_UNITS.put("hours", TimeUnit.HOURS);
	}

	/**
	 * Returns a {@link TimeUnit} which corresponds to a <code>String</code> configuration value. Valid configuration
	 * values are maintained in {@link #VALID_TIME_UNITS}.
	 * 
	 * @param pollEveryUnit
	 *            Non-null and non-empty string
	 */
	public static TimeUnit fromPollEveryUnit(String pollEveryUnit) {
		verifyNonNull(pollEveryUnit, "poll-every-unit must be non-null");
		verifyNonEmpty(pollEveryUnit, "poll-every-unit must be non-empty");
		String sanitized = sanitizePollEveryUnit(pollEveryUnit);
		verify(VALID_TIME_UNITS.containsKey(sanitized), "poll-every-unit '" + pollEveryUnit + "' is not a valid value: " + VALID_TIME_UNITS.keySet());
		return VALID_TIME_UNITS.get(sanitized);
	}

	/**
	 * Transforms the input to lower case and adds an 's' if it is not already added.
	 */
	private static String sanitizePollEveryUnit(String pollEveryUnit) {
		String pollEveryUnitLower = pollEveryUnit.toLowerCase();
		int length = pollEveryUnit.length();
		if (pollEveryUnitLower.charAt(length - 1) != 's') {
			pollEveryUnitLower += 's';
		}
		return pollEveryUnitLower;
	}

}
