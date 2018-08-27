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

public class ScriptParameter {
	
	private final String name;
	private final double lb;
	private final double ub;
	private Double value;
	
	public ScriptParameter( String name , double lb , double ub ) {
		this.name = name;
		this.lb = lb;
		this.ub = ub;
		this.value = null;
	}
	
	public double getValue( ) {
		if (value == null) {
			return lb;
		} else {
			return value;
		}
	}
	
	public boolean isValid( double value ) {
		return (value>=lb)&&(value<=ub);
	}
	
	public void setValue( double value ) {
		if (isValid(value)) {
			this.value = value;
		}
		throw new IllegalArgumentException();
	}

	public String getName() {
		return name;
	}

	public double getLowerBound() {
		return lb;
	}

	public double getUpperBound() {
		return ub;
	}
}
