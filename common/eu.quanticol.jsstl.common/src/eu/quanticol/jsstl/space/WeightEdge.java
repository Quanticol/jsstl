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
package eu.quanticol.jsstl.space;

public class WeightEdge {
	public Location lStart; //
	public Location lEnd; //
	public double weight; //

	// -------------------------------------------------------------
	public WeightEdge(Location start, Location end, double w) // constructor
	{
		lStart = start;
		lEnd = end;
		weight = w;
	}
	// -------------------------------------------------------------

}
