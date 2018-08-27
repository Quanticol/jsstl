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

import java.util.Map;

/**
 *
 * @author Luca
 */
public class ParametricInterval {
	
	ParametricExpression lower;
	ParametricExpression upper;

	public ParametricInterval() {
		this.lower = new ConstantExpression(0.0);
		this.upper = new ConstantExpression(Double.POSITIVE_INFINITY);
	}
	
	public ParametricInterval( ParametricExpression lower , ParametricExpression upper ) {
		this.lower = lower;
		this.upper = upper;
	}
	
	public ParametricInterval( double lower , double upper ) {
		this();
		this.lower = new ConstantExpression(lower);
		this.upper = new ConstantExpression(upper);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ParametricInterval)) {
			return false;
		} else {
			ParametricInterval pi = (ParametricInterval) obj;
			return this.lower.equals( pi.lower )&&this.upper.equals( pi.upper );
		}
	}

	public void setLower(double v) {
		this.lower = new ConstantExpression(v);
	}

	public void setUpper(double v) {
		this.upper = new ConstantExpression(v);
	}
	
	public void setLower( ParametricExpression e ) {
		if (e == null) {
			throw new NullPointerException();
		}
		this.lower = e;
	}

	public void setUpper( ParametricExpression e ) {
		if (e == null) {
			throw new NullPointerException();
		}
		this.upper = e;
	}
	
	
	public void setUpperToInfinity() {
		this.upper = new ConstantExpression(Double.POSITIVE_INFINITY);
	}

	public void setLowerToZero() {
		this.lower = new ConstantExpression(0.0);
	}

	public double getLower(Map<String,Double> parameters) {
		if (lower != null) {
			return lower.eval(parameters).eval();
		}
		return 0.0;
	}

	public double getUpper(Map<String,Double> parameters) {
		if (upper != null) {
			return upper.eval(parameters).eval( );
		}
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public String toString() {
		return "["+lower.toString()+","+upper.toString()+"]";
	}

}
