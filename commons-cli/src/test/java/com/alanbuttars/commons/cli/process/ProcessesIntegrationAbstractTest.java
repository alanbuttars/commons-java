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

import com.alanbuttars.commons.cli.util.Argument;

/**
 * Utility class which provides handles on test scripts.
 * 
 * @author Alan Buttars
 *
 */
public class ProcessesIntegrationAbstractTest {

	protected Argument sh() {
		return new Argument("sh");
	}

	protected Argument exitCode0Script() {
		return script("exit_code_0.sh");
	}

	protected Argument exitCode0SlowScript() {
		return script("exit_code_0_slow.sh");
	}

	protected Argument exitCode1Script() {
		return script("exit_code_1.sh");
	}

	private Argument script(String scriptFileName) {
		return new Argument(getClass().getResource(scriptFileName).getFile());
	}
}
