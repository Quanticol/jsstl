/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jsstl.core.formula;

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
