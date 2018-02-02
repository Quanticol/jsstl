/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jsstl.core.formula;

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
