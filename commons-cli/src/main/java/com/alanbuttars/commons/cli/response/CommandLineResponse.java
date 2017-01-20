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

import com.alanbuttars.commons.cli.util.EvaluationResult;

/**
 * Models a command line {@link Process} result. This object may be evaluated in the following manner: <br/>
 *
 * <pre>
 * CommandLineResponse response = getResponse();
 * if (response.succeeded()) {
 * 	logger.info(response.getInfoStream());
 * }
 * else if (response.getExitCode() == CommandLineResponse.EXCEPTION_THROWN_EXIT_CODE) {
 * 	throw response.getException();
 * }
 * else {
 * 	logger.error(response.getErrorStream());
 * }
 * </pre>
 *
 * @author Alan Buttars
 *
 */
public class CommandLineResponse extends EvaluationResult {

	public static final int EXCEPTION_THROWN_EXIT_CODE = Integer.MIN_VALUE;
	public static final int INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE = Integer.MAX_VALUE;
	private int exitCode;
	private String infoStream;
	private String errorStream;
	private Exception exception;

	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}

	/**
	 * @return the process's exit status
	 */
	public int getExitCode() {
		return exitCode;
	}

	public void setInfoStream(String infoStream) {
		this.infoStream = infoStream;
	}

	/**
	 * @return the process's info stream
	 */
	public String getInfoStream() {
		return infoStream;
	}

	public void setErrorStream(String errorStream) {
		this.errorStream = errorStream;
	}

	/**
	 * @return the process's error stream
	 */
	public String getErrorStream() {
		return errorStream;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * @return <code>null</code> if the process executed cleanly
	 */
	public Exception getException() {
		return exception;
	}

	public boolean exceptionThrown() {
		return exitCode == EXCEPTION_THROWN_EXIT_CODE;
	}

	public boolean interrupted() {
		return exitCode == INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE;
	}

}
