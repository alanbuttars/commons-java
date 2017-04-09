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

import static com.alanbuttars.commons.util.validators.Arguments.verifyPositive;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YamlFileConfig {

	private String file;
	@JsonProperty("poll-every")
	private int pollEvery;
	@JsonProperty("poll-every-unit")
	private String pollEveryUnit;

	public String getFile() {
		return file;
	}

	void setFile(String file) {
		this.file = file;
	}

	public int getPollEvery() {
		return pollEvery;
	}

	void setPollEvery(int pollEvery) {
		verifyPositive(pollEvery, "poll-every must be positive but was " + pollEvery);
		this.pollEvery = pollEvery;
	}
	
	public String getPollEveryUnitString() {
		return pollEveryUnit;
	}

	public TimeUnit getPollEveryUnit() {
		return TimeUnits.fromPollEveryUnit(pollEveryUnit);
	}

	void setPollEveryUnit(String pollEveryUnit) {
		this.pollEveryUnit = pollEveryUnit;
	}
	
}
