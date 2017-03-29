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
package com.alanbuttars.commons.config.source;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigItem {

	private String file;
	private String type;
	@JsonProperty("poll-every")
	private int pollEvery;
	@JsonProperty("poll-every-unit")
	private String pollEveryUnit;

	String getFile() {
		return file;
	}

	void setFile(String file) {
		this.file = file;
	}

	String getType() {
		return type;
	}

	void setType(String type) {
		this.type = type;
	}

	int getPollEvery() {
		return pollEvery;
	}

	void setPollEvery(int pollEvery) {
		this.pollEvery = pollEvery;
	}

	TimeUnit getPollEveryUnit() {
		return TimeUnits.fromPollEveryUnit(pollEveryUnit);
	}

	void setPollEveryUnit(String pollEveryUnit) {
		this.pollEveryUnit = pollEveryUnit;
	}

}