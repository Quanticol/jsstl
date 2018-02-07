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

/**
 * @author loreti
 *
 */
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
