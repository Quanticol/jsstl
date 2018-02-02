/**
 * 
 */
package org.jsstl.core.formula;

import java.util.Map;

/**
 * @author loreti
 *
 */
public class ConstantExpression implements SignalExpression, ParametricExpression {

	
	private final double value;
	
	public ConstantExpression( double value ) {
		this.value = value;
	}
 	
	@Override
	public SignalExpression eval(Map<String, Double> parameters) {
		return this;
	}

	@Override
	public double eval(double... variables) {
		return value;
	}


}
