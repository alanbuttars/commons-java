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
package com.alanbuttars.commons.cli.evaluator;

import com.alanbuttars.commons.cli.response.CommandLineResponse;

/**
 * A high-level interface modeling an evaluation function which maps a running {@link Process} to a
 * {@link CommandLineResponse}.
 *
 * @author Alan Buttars
 *
 */
public interface CommandLineEvaluator {

	/**
	 * Evaluates the exit code of a completed {@link Process}.
	 *
	 * @param exitCode
	 * @return <code>false</code> if the <code>exitCode</code> indicates a failure
	 */
	public boolean evaluateExitCode(int exitCode);

	/**
	 * Evaluates a single line from a running {@link Process}'s info stream.
	 *
	 * @param infoStreamLine
	 *            Non-null but potentially empty line from the input stream
	 * @return <code>false</code> if the <code>infoStreamLine</code> indicates a failure
	 */
	public boolean evaluateInfoStream(String infoStreamLine);

	/**
	 * Evaluates a single line from a running {@link Process}'s error stream.
	 *
	 * @param errorStreamLine
	 *            Non-null but potentially empty line from the error stream
	 * @return <code>false</code> if the <code>errorStreamLine</code> indicates a failure
	 */
	public boolean evaluateErrorStream(String errorStreamLine);

	/**
	 * Evaluates an exception thrown during the deployment or runtime of a {@link Process}.
	 *
	 * @param e
	 *            Non-null exception
	 * @return final response object
	 */
	public CommandLineResponse evaluateException(Exception e);

}
