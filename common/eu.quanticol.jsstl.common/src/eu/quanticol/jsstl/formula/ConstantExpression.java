/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Michele Loreti
 *  	Laura Nenzi
 *  	Luca Bortolussi
 *******************************************************************************/
/**
 * 
 */
package eu.quanticol.jsstl.formula;

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
