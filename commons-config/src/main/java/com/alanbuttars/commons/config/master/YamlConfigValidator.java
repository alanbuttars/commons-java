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

import static com.alanbuttars.commons.util.validators.Arguments.verify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Joiner;

/**
 * A validator object for {@link YamlConfig}.
 * 
 * @author Alan Buttars
 *
 */
public class YamlConfigValidator {

	public YamlConfigValidator() {
	}

	/**
	 * Validates a given config. This method maintains a list of running validation errors. If any validations fail, an
	 * {@link IllegalArgumentException} will be thrown containing a summary of errors.
	 * 
	 * @param config
	 *            Non-null config
	 */
	public static void validate(YamlConfig config) {
		List<String> errors = new ArrayList<>();
		validate(config.getMaster(), errors);
		validate(config.getFileConfigs(), errors);
		verify(errors.isEmpty(), Joiner.on("\n").join(errors));
	}

	/**
	 * Validates the master node of the {@link YamlConfig}.
	 * 
	 * @param master
	 *            Non-null master
	 * @param errors
	 *            Running list of errors
	 */
	private static void validate(YamlMasterConfig master, List<String> errors) {
		if (validate(master != null, "master must be non-null", errors)) {
			validate(master.getPollEvery() > 0, //
					"master.poll-every must be greater than 0", errors);
			validate(TimeUnits.isValidPollEveryUnit(master.getPollEveryUnitString()), //
					"master.poll-every-unit '" + master.getPollEveryUnitString() + "' is invalid; use one of " + TimeUnits.VALID_TIME_UNITS.keySet(), errors);
			validate(master.getPoolSize() > 0, //
					"master.pool-size must be greater than 0", errors);
		}
	}

	/**
	 * Validates the file node of the {@link YamlConfig}.
	 * 
	 * @param fileConfigs
	 *            Non-null mapping of files
	 * @param errors
	 *            Running list of errors
	 */
	private static void validate(Map<String, YamlFileConfig> fileConfigs, List<String> errors) {
		for (Entry<String, YamlFileConfig> entry : fileConfigs.entrySet()) {
			String sourceId = entry.getKey();
			YamlFileConfig fileConfig = entry.getValue();

			if (validateNonEmpty(fileConfig.getFile(), //
					"files." + sourceId + ".file must be non-empty", errors)) {
				File file = new File(fileConfig.getFile());
				if (validate(file.exists(), //
						"files." + sourceId + ".file '" + file.getAbsolutePath() + "' does not exist", errors)) {
					if (validate(file.isFile(), //
							"files." + sourceId + ".file '" + file.getAbsolutePath() + "' is a directory; it must be a file", errors)) {
						validate(file.canRead(), //
								"files." + sourceId + ".file '" + file.getAbsolutePath() + "' is unreadable", errors);
					}
				}
			}
			validate(fileConfig.getPollEvery() > 0, //
					"files." + sourceId + ".poll-every must be greater than 0", errors);
			validate(TimeUnits.isValidPollEveryUnit(fileConfig.getPollEveryUnitString()), //
					"files." + sourceId + ".poll-every-unit '" + fileConfig.getPollEveryUnitString() + "' is invalid; use one of " + TimeUnits.VALID_TIME_UNITS.keySet(), errors);
		}
	}

	private static boolean validate(boolean expression, String error, List<String> errors) {
		if (!expression) {
			errors.add(error);
			return false;
		}
		return true;
	}

	private static boolean validateNonEmpty(String string, String error, List<String> errors) {
		if (string == null || string.trim().isEmpty()) {
			validate(false, error, errors);
			return false;
		}
		return true;
	}

}
