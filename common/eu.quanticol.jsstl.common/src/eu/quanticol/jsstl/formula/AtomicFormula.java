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

import eu.quanticol.jsstl.monitor.SpatialBooleanSignal;
import eu.quanticol.jsstl.monitor.SpatialQuantitativeSignal;
import eu.quanticol.jsstl.space.GraphModel;

/**
 * @author lauretta
 *
 */
public class AtomicFormula implements Formula {

	private ParametricExpression expression;
	private boolean isStrictInequality;

	public AtomicFormula(ParametricExpression expression, boolean isStrictInequality) {
		super();
		this.expression = expression;
		this.isStrictInequality = isStrictInequality;
	}

	@Override
	public SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return s.getSpatialBooleanSignal(expression.eval(parameters), isStrictInequality);
	}

	@Override
	public SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return s.getSpatialQuantitativeSignal(expression.eval(parameters), isStrictInequality);

	}

}
