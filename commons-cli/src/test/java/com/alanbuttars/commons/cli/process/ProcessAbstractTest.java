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

import java.io.File;

import com.alanbuttars.commons.cli.util.Argument;

/**
 * An abstract test class to test classes in the {@link com.alanbuttars.commons.cli.process} package.
 * 
 * @author Alan Buttars
 *
 */
public abstract class ProcessAbstractTest {

	protected Argument shArg() {
		return new Argument("sh");
	}

	protected String exitCode0Script() {
		return getClass().getResource("exit_code_0.sh").getFile();
	}

	protected File exitCode0ScriptFile() {
		return new File(exitCode0Script());
	}

	protected Argument exitCode0ScriptArg() {
		return new Argument(exitCode0Script());
	}

	protected String exitCode0SlowScript() {
		return getClass().getResource("exit_code_0_slow.sh").getFile();
	}

	protected File exitCode0SlowScriptFile() {
		return new File(exitCode0SlowScript());
	}

	protected Argument exitCode0SlowScriptArg() {
		return new Argument(exitCode0SlowScript());
	}

	protected String exitCode1Script() {
		return getClass().getResource("exit_code_1.sh").getFile();
	}

	protected File exitCode1ScriptFile() {
		return new File(exitCode1Script());
	}

	protected Argument exitCode1ScriptArg() {
		return new Argument(exitCode1Script());
	}
}
