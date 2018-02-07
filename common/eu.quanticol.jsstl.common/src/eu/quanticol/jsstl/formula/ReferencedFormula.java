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
 * @author loreti
 *
 */
public class ReferencedFormula implements Formula {
	
	private jSSTLScript script;
	
	private String name;
	
	public ReferencedFormula( jSSTLScript script , String name ) {
		this.script = script;
		this.name = name;
	}
	

	/* (non-Javadoc)
	 * @see org.jsstl.core.formula.Formula#booleanCheck(org.jsstl.core.space.GraphModel, org.jsstl.core.formula.Signal)
	 */
	@Override
	public SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return script.booleanCheck( parameters , name , g , s );
	}

	/* (non-Javadoc)
	 * @see org.jsstl.core.formula.Formula#quantitativeCheck(org.jsstl.core.space.GraphModel, org.jsstl.core.formula.Signal)
	 */
	@Override
	public SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return script.quantitativeCheck(parameters,name,g,s);
	}

}
