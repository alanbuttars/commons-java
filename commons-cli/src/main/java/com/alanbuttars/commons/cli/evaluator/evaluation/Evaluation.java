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
package com.alanbuttars.commons.cli.evaluator.evaluation;

/**
 * A wrapper around {@link EvaluationEnum} used to offer more context to each enumerated type.
 * 
 * @author Alan Buttars
 *
 */
public class Evaluation {

	/**
	 * Indicates that neither success nor failure could be conclusively identified
	 */
	public static Evaluation NON_CONCLUSIVE = new Evaluation(EvaluationEnum.NON_CONCLUSIVE);

	private EvaluationEnum evaluation;

	protected Evaluation(EvaluationEnum evaluation) {
		this.evaluation = evaluation;
	}

	/**
	 * @return <code>true</code> to indicate that the {@link Process} conclusively succeeded
	 */
	public boolean succeeded() {
		return evaluation == EvaluationEnum.SUCCESS;
	}

	/**
	 * @return <code>true</code> to indicate that the {@link Process} conclusively failed
	 */
	public boolean failed() {
		return evaluation == EvaluationEnum.FAILURE;
	}

	/**
	 * @return <code>true</code> to indicate that the {@link Process} cannot yet be identified as successful or
	 *         non-successful
	 */
	public boolean nonConclusive() {
		return evaluation == EvaluationEnum.NON_CONCLUSIVE;
	}

}