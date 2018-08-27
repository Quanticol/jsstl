/*******************************************************************************
 * jSSTL:  jSSTL : java Signal Spatio Temporal Logic
 * Copyright (C) 2018 
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package eu.quanticol.jsstl.core.formula;

/**
 * relative position of interval B w.r.t interval A
 * 
 * @author Luca
 */
public enum RelativePosition {
	/**
	 * B is on the left of A, i.e. sup B <= inf A, and A cap B = emptyset
	 */
	ON_THE_LEFT,
	/**
	 * B cap A is not empty
	 */
	OVERLAPS,
	/**
	 * B is on the right of A, i.e. sup B <= inf A, and A cap B = emptyset
	 */
	ON_THE_RIGHT;
}
