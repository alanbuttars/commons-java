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
package com.alanbuttars.commons.cli.util;

import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;

/**
 * Abstract class encompassing a single {@link Evaluation} result and accompanying helper methods.
 * 
 * @author Alan Buttars
 *
 */
public abstract class EvaluationResult {

	protected Evaluation evaluation = Evaluation.NON_CONCLUSIVE;

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	/**
	 * @return <code>true</code> if this object models conclusive success
	 */
	public boolean succeeded() {
		return evaluation != null && evaluation.succeeded();
	}

	/**
	 * @return <code>true</code> if this object models conclusive failure
	 */
	public boolean failed() {
		return evaluation != null && evaluation.failed();
	}

	/**
	 * @return <code>true</code> if this object models a non-conclusive result
	 */
	public boolean nonConclusive() {
		return evaluation == null || evaluation.nonConclusive();
	}

}
