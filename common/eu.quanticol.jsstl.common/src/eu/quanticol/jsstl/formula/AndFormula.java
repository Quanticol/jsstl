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
package eu.quanticol.jsstl.formula;

import java.util.Map;

import eu.quanticol.jsstl.monitor.SpatialBooleanSignal;
import eu.quanticol.jsstl.monitor.SpatialBooleanSignalTransducer;
import eu.quanticol.jsstl.monitor.SpatialQuantitativeSignal;
import eu.quanticol.jsstl.monitor.SpatialQuantitativeSignalTransducer;
import eu.quanticol.jsstl.space.GraphModel;

public class AndFormula implements Formula {

	private Formula left;
	private Formula right;

	public AndFormula(Formula left, Formula right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return SpatialBooleanSignalTransducer.and(this.left.booleanCheck(parameters, g, s),
				this.right.booleanCheck(parameters, g, s));
	}

	@Override
	public SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return SpatialQuantitativeSignalTransducer.and(
				this.left.quantitativeCheck(parameters, g, s),
				this.right.quantitativeCheck(parameters, g, s));
	}

}
