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
package eu.quanticol.jsstl.core.io;

/**
 * @author loreti
 *
 */
public class SyntaxErrorExpection extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 176438192405734003L;
	
	private int line;
	private String expected;
	private String actual;

	public SyntaxErrorExpection(int line, String expected,
			String actual) {
		super( "Syntax error at line "+line+": Expected "+expected+" found "+actual);
		this.line = line;
		this.expected = expected;
		this.actual = actual;
	}

	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @return the expected
	 */
	public String getExpected() {
		return expected;
	}

	/**
	 * @return the actual
	 */
	public String getActual() {
		return actual;
	}

}
