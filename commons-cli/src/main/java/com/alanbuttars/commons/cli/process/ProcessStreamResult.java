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
package com.alanbuttars.commons.cli.process;

import java.io.InputStream;

/**
 * Simple POJO containing the results of an {@link InputStream} being consumed via {@link ProcessStreamReaderTest}.
 * 
 * @author Alan Buttars
 *
 */
class ProcessStreamResult {

	private StringBuilder stream;
	private boolean success;
	private boolean interrupted;
	private Exception exception;

	public ProcessStreamResult() {
		this.stream = new StringBuilder();
		this.success = true;
	}

	public String getStream() {
		return stream.toString();
	}

	public void appendToStream(String line) {
		this.stream.append(line).append("\n");
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean succeeded() {
		return success;
	}

	public boolean interrupted() {
		return interrupted;
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public boolean failedWithoutException() {
		return !success && exception == null;
	}

	@Override
	public String toString() {
		return new StringBuilder()//
				.append("stream=").append(stream).append("&")//
				.append("success=").append(success).append("&")//
				.append("interrupted=").append(interrupted).append("&")//
				.append("exception=").append(exception)//
				.toString();
	}

}
