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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.quanticol.jsstl.formula;

/**
 * relative position of interval B w.r.t interval A
 * 
 * @author Luca
 */
public enum RelativePosition {
	/**
	 * B is on the left of A, i.e. sup B <= inf A, and A cap B = emptyset
	 */
	ON_THE_LEFT,
	/**
	 * B cap A is not empty
	 */
	OVERLAPS,
	/**
	 * B is on the right of A, i.e. sup B <= inf A, and A cap B = emptyset
	 */
	ON_THE_RIGHT;
}
