/*******************************************************************************
 * jSSTL:  jSSTL : java Signal Spatio Temporal Logic
 * Copyright (C) 2018 
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package eu.quanticol.jsstl.core.formula;

import java.util.Locale;

/**
 *
 * @author Luca
 */
public class Interval {
	double lower;
	double upper;
	boolean lowerClosed;
	boolean upperClosed;
	boolean upperIsInfinity;

	public Interval() {
		this.lower = 0.0;
		this.upper = Double.POSITIVE_INFINITY;
		this.upperIsInfinity = true;
		this.lowerClosed = true;
		this.upperClosed = false;
	}

	public void setLower(double v, boolean closed) {
		this.lower = v;
		this.lowerClosed = closed;
		if (v <= 0.0)
			this.lower = 0.0;
	}

	public void setUpper(double v, boolean closed) {
		this.upper = v;
		this.upperClosed = closed;
		if (!Double.isInfinite(v))
			this.upperIsInfinity = false;
		else
			this.upperIsInfinity = true;
	}

	public boolean checkConsistency() {
		return (lowerClosed && upperClosed ? lower <= upper : lower < upper);
	}

	public void setUpperToInfinity() {
		this.upperIsInfinity = true;
		this.upperClosed = false;
		this.upper = Double.POSITIVE_INFINITY;
	}

	public double getLower() {
		return lower;
	}

	public double getUpper() {
		return upper;
	}

	public boolean isLowerClosed() {
		return lowerClosed;
	}

	public boolean isUpperClosed() {
		return upperClosed;
	}

	public boolean isUpperInfinity() {
		return upperIsInfinity;
	}

	/**
	 * Checks if the interval contains t. t must be a positive number!
	 * 
	 * @param t
	 * @return
	 */
	public boolean contains(double t) {
		if ((lowerClosed ? t >= lower : t > lower)
				&& (this.upperIsInfinity || (upperClosed ? t <= upper
						: t < upper)))
			return true;
		else
			return false;
	}

	public boolean upperBoundSmallerThan(double t) {
		if (!this.upperIsInfinity && (upperClosed ? t > upper : t >= upper))
			return true;
		else
			return false;
	}

	public boolean lowerBoundGreaterThan(double t) {
		if (lowerClosed ? t < lower : t <= lower)
			return true;
		else
			return false;
	}

	public RelativePosition relativePositionOf(Interval I) {
		if ((lowerClosed && I.upperClosed && I.upper < lower)
				|| ((!lowerClosed || !I.upperClosed) && I.upper <= lower))
			return RelativePosition.ON_THE_LEFT;
		else if ((upperClosed && I.lowerClosed && I.lower > upper)
				|| ((!upperClosed || !I.lowerClosed) && I.lower >= upper))
			return RelativePosition.ON_THE_RIGHT;
		else
			return RelativePosition.OVERLAPS;
	}

	/**
	 * Returns the relative position of number t w.r.t the inteval. t can be on
	 * the left, on the right, or it can belong to the interval In this case, it
	 * returns overlap
	 * 
	 * @param t
	 * @return
	 */
	public RelativePosition relativePositionOf(double t) {
		if ((lowerClosed && t < lower) || (!lowerClosed && t <= lower))
			return RelativePosition.ON_THE_LEFT;
		else if ((upperClosed && t > upper) || (!upperClosed && t >= upper))
			return RelativePosition.ON_THE_RIGHT;
		else
			return RelativePosition.OVERLAPS;
	}

	@Override
	public String toString() {
		String s = "[";
		s += String.format(Locale.UK, "%.4f", this.lower);
		s += ", ";
		s += (this.upperIsInfinity ? "infinity" : String.format(Locale.UK,
				"%.4f", this.upper));
		s += "]";
		return s;
	}

}
