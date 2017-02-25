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
package com.alanbuttars.commons.util.functions;

/**
 * A general interface intended for lambdas with two input arguments. For Java 1.7 and below.
 * 
 * @author Alan Buttars
 *
 * @param <A>
 * @param <B>
 * @param <C>
 */
public abstract class BiFunction<A, B, C> {

	/**
	 * Applies the function.
	 * 
	 * @param inputA
	 *            Nullable input
	 * @param inputB
	 *            Nullable input
	 */
	public abstract C apply(A inputA, B inputB);
}
