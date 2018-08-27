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
package eu.quanticol.jsstl.core.signal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import eu.quanticol.jsstl.core.signal.BooleanSignal;
import eu.quanticol.jsstl.core.signal.TimeInterval;

/**
 *
 * @author luca
 */
public class BooleanSignalTransducer {

	// //////////// INTERVAL COVERING //////////////
	public static LinkedList<Double> intervalCovering(BooleanSignal s1,
			BooleanSignal s2) {
		LinkedList<Double> timeInterval = new LinkedList<Double>();
		double t0 = Math.max(s1.initial_time, s2.initial_time);
		// double tf = Math.max(s1.final_time, s2.final_time);
		timeInterval.add(t0);
		double current = t0;
		TimeInterval I1, I2;
		I1 = s1.getNextPositive();
		I2 = s2.getNextPositive();
		while ((I1 != null) && (I2 != null)) {
			double foo = Math.min((I1 == null ? Integer.MAX_VALUE : I1.lower),
					(I2 == null ? Integer.MAX_VALUE : I2.lower));
			if (foo != current) {

			}
		}
		return timeInterval;
	}

	public static LinkedList<Double> internalCovering(BooleanSignal s) {
		LinkedList<Double> timeInterval = new LinkedList<Double>();
		timeInterval.add(s.initial_time);
		double current = s.initial_time;

		TimeInterval I1 = s.getNextPositive();
		while (I1 != null) {
			if (current != I1.lower) {
				timeInterval.add(I1.lower);
			}
			timeInterval.add(I1.upper);
			current = I1.upper;
			I1 = s.getNextPositive();
		}
		if (current != s.final_time)
			timeInterval.add(s.final_time);
		return timeInterval;
	}

	public static LinkedList<Double> internalCovering(LinkedList<Double> left,
			LinkedList<Double> right) {
		LinkedList<Double> timeInterval = new LinkedList<Double>();

		Iterator<Double> leftIterator = left.iterator();
		Iterator<Double> rightIterator = right.iterator();

		double cLeft = -1.0;
		double cRight = -1.0;
		while (leftIterator.hasNext() || (cLeft != -1)
				|| rightIterator.hasNext() || (cRight != -1)) {
			if ((cLeft == -1) && (leftIterator.hasNext())) {
				cLeft = leftIterator.next();
			}
			while ((rightIterator.hasNext()
					&& ((cLeft != -1) || !leftIterator.hasNext()) && (cRight <= cLeft))
					|| ((cRight != -1) && ((cLeft == -1) || (cRight <= cLeft)))) {
				if ((cRight == -1) && (rightIterator.hasNext())) {
					cRight = rightIterator.next();
				} else {
					timeInterval.add(cRight);
					if (cRight == cLeft) {
						cLeft = -1;
					}
					cRight = -1;

				}
			}
			if (cLeft != -1) {
				timeInterval.add(cLeft);
				cLeft = -1;
			}
		}

		return timeInterval;
	}

	public static LinkedList<Double> internalCovering(
			ArrayList<BooleanSignal> array) {
		return internalCovering(array, 0, array.size());
	}

	public static LinkedList<Double> internalCovering(
			ArrayList<BooleanSignal> array, int init, int end) {
		if (init + 1 == end) {
			return internalCovering(array.get(init));
		} else {
			return internalCovering(
					internalCovering(array, init, (init + (end - init) / 2)),
					internalCovering(array, (init + (end - init) / 2), end));
		}
	}

	// ///////// AND ///////////////////////////////
	public static BooleanSignal and(BooleanSignal s1, BooleanSignal s2) {
		if (s1.undefined || s2.undefined) {
			BooleanSignal s0 = new BooleanSignal(0, 0);
			s0.finalise();
			return s0;
		}
		double t0 = Math.max(s1.initial_time, s2.initial_time);
		double tf = Math.min(s1.final_time, s2.final_time);
		BooleanSignal s0 = new BooleanSignal(t0, tf);

		TimeInterval I1, I2;
		I1 = s1.getNextPositive();
		I2 = s2.getNextPositive();

		while (I1 != null && I2 != null) {
			TimeInterval I = I1.intersection(I2);
			if (I != null)// && !I.isPoint())
				s0.addNextPositive(I.lower, I.upper, I.lowerClosed,
						I.upperClosed);
			if (I1.upper < I2.upper)
				I1 = s1.getNextPositive();
			else
				I2 = s2.getNextPositive();
		}
		if (s0.signal.isEmpty())
			s0.addNext(tf, true, false);
		s0.finalise();
		s1.reset();
		s2.reset();
		return s0;
	}

	// ///////// OR ///////////////////////////////

	public static BooleanSignal or(BooleanSignal s1, BooleanSignal s2) {
		if (s1.undefined || s2.undefined) {
			BooleanSignal s0 = new BooleanSignal(0, 0);
			s0.finalise();
			return s0;
		}
		s1.quickComplement();
		s2.quickComplement();
		BooleanSignal s0 = and(s1, s2);
		s0.complement();
		s1.quickComplement();
		s2.quickComplement();
		return s0;
	}

	// ///////// IMPLY ///////////////////////////////

	public static BooleanSignal imply(BooleanSignal s1, BooleanSignal s2) {
		if (s1.undefined || s2.undefined) {
			BooleanSignal s0 = new BooleanSignal(0, 0);
			s0.finalise();
			return s0;
		}
		s2.quickComplement();
		BooleanSignal s0 = and(s1, s2);
		s0.complement();
		s2.quickComplement();
		return s0;
	}

	// ///////// NOT ///////////////////////////////

	public static BooleanSignal not(BooleanSignal s) {
		if (s.undefined) {
			BooleanSignal s0 = new BooleanSignal(0, 0);
			s0.finalise();
			return s0;
		}
		BooleanSignal s0 = s.copy();
		s0.complement();
		return s0;
	}

	// ///////// UNTIL ///////////////////////////////

	public static BooleanSignal until(BooleanSignal s1, BooleanSignal s2,
			double t1, double t2) {
		/*
		 * WARNING: this implementation has to be checked.
		 */

		if (s1.undefined || s2.undefined) {
			BooleanSignal s0 = new BooleanSignal(0, 0);
			s0.finalise();
			return s0;
		}
		double t0 = Math.max(s1.initial_time, s2.initial_time);
		double tf = Math.min(s1.final_time, s2.final_time);
		BooleanSignal s0 = new BooleanSignal(t0, tf);

		TimeInterval I1, I2, I0, I;
		I1 = s1.getNextPositive();
		I2 = s2.getNextPositive();

		while (I1 != null && I2 != null) {
			if (I1.intersects(I2)) { // (I1.intersectsNonemptyInterior(I2)) {
				I0 = I1.intersection(I2);
				I0 = I0.backshift(t1, t2);
				I = I0.intersection(I1);
				if (I != null)// && !I.isPoint())
					s0.addNextPositive(I.lower, I.upper, I.lowerClosed,
							I.upperClosed);
			}
			if (I1.upper < I2.upper)
				I1 = s1.getNextPositive();
			else
				I2 = s2.getNextPositive();
		}
		// try to extend the final false region as much as possible
		double tt;
		if (s0.signal.isEmpty()) {
			tt = Math.max(tf - t2, t0);
		} else {
			double t = s0.getLastSignalEndTime();
			tt = Math.max(t, tf - t2);
		}
		s1.reset();
		I1 = s1.getNextPositive();
		if (I1 == null)
			tt = s1.final_time;
		while (I1 != null) {
			if (I1.contains(tt))
				if (I1.upper - tt < t1)
					tt = I1.upper;
				else
					break;
			else if (I1.isOnTheRightOf(tt)) {
				if (I1.upper - I1.lower < t1)
					tt = I1.upper;
				else {
					tt = I1.lower;
					break;
				}
			}
			I1 = s1.getNextPositive();
		}
		if (tt > t0) {
			s0.final_time = tt;
			s0.addNext(tt, true, false);
		}
		s0.finalise();
		s1.reset();
		s2.reset();
		return s0;
	}

	// ///////// ALWAYS ///////////////////////////////

	public static BooleanSignal always(BooleanSignal s, double t1, double t2) {
		if (s.undefined) {
			BooleanSignal s0 = new BooleanSignal(0, 0);
			s0.finalise();
			return s0;
		}
		s.quickComplement();
		BooleanSignal s0 = eventually(s, t1, t2);
		s0.complement();
		s.quickComplement();
		return s0;
	}

	// ///////// EVENTUALLY ///////////////////////////////
	public static BooleanSignal eventually(BooleanSignal s, double t1, double t2) {
		if (s.undefined) {
			BooleanSignal s0 = new BooleanSignal(0, 0);
			s0.finalise();
			return s0;
		}
		double t0 = s.initial_time;
		double tf = s.final_time;
		BooleanSignal s0 = new BooleanSignal(t0, tf);

		TimeInterval I1;
		I1 = s.getNextPositive();
		while (I1 != null) {
			TimeInterval I = I1.backshift(t1, t2);
			if (I != null) // && !I.isPoint())
				s0.addNextPositive(I.lower, I.upper, I.lowerClosed,
						I.upperClosed);
			I1 = s.getNextPositive();
		}
		double t = s0.getLastSignalEndTime();
		tf = Math.max(t, s.final_time - t2);
		if (s0.signal.isEmpty() && tf > t)
			s0.addNext(tf, true, false);
		s0.final_time = tf;
		s0.finalise();
		s.reset();
		return s0;
	}

	// /////////////// convertPWLinearSignalToBoolean

	public static BooleanSignal convertPWLinearSignalToBoolean(double[] time,
			double[] signal, boolean zeroIsTrue) {
		if (time == null || signal == null || time.length < 2
				|| signal.length < 2 || time.length != signal.length)
			throw new RuntimeException("Error in the input of the PWL signal.");
		// int n = 1;
		// while (n < time.length && time[n] > 0.0)
		// n++;
		int n = time.length;
		BooleanSignal s = new BooleanSignal(time[0], time[n - 1]);
		boolean current;
		int j;
		// double t0 = time[0];

//		if (signal[0] < 0)
//			current = false;
//		else if (signal[0] > 0)
//			current = true;
//		else {
//			if (signal[1] < 0)
//				current = false;
//			else if (signal[1] > 0)
//				current = true;
//			else
//				current = zeroIsTrue;
//		}
		if (signal[0]==0) {
			current = zeroIsTrue;
		} else {
			current = (signal[0]>0);
		}
		j = 1;
		while (j < n) {
			if ((current && (signal[j] < 0 || (!zeroIsTrue && signal[j] == 0)))
					|| (!current && (signal[j] > 0 || (zeroIsTrue && signal[j] == 0)))) {
				double t = zeroCrossing(time[j - 1], time[j], signal[j - 1],
						signal[j]);
				s.addNext(t, !(current ^ zeroIsTrue), current);
				current = !current;
				// t0 = t;
			}
			j++;
		}
		if (s.signal.isEmpty()) {
			// the signal has never changed truth value, hence it is always true
			// or always false
			s.addNext(time[n - 1], true, current);
		}
		s.finalise();
		return s;
	}

	// /////////////// convertPWConstantSignalToBoolean

	public static BooleanSignal convertPWConstantSignalToBoolean(double[] time,
			double[] signal, boolean zeroIsTrue) {
		if (time == null || signal == null || time.length < 2
				|| signal.length < 2 || time.length != signal.length)
			throw new RuntimeException("Error in the input of the PWL signal.");
		// find effective length of signal.
		// int n = 1;
		// while (n < time.length && time[n] > 0.0)
		// n++;
		int n = time.length;
		BooleanSignal s = new BooleanSignal(time[0], time[n - 1]);
		boolean current;
		int j;
		// double t0 = time[0];
		if (signal[0] < 0)
			current = false;
		else if (signal[0] > 0)
			current = true;
		else
			current = zeroIsTrue;
		j = 1;
		while (j < n - 1) { // in n-1 we have the final time, and signal[n-1] ==
							// signal[n-1].
			if ((current && (signal[j] < 0 || (!zeroIsTrue && signal[j] == 0)))
					|| (!current && (signal[j] > 0 || (zeroIsTrue && signal[j] == 0)))) {
				s.addNext(time[j], false, current);
				current = !current;
				// t0 = time[j];
			}
			j++;
		}
		if (s.signal.isEmpty()) {
			// the signal has never changed truth value, hence it is always true
			// or always false
			s.addNext(time[n - 1], true, current);
		}
		s.finalise();
		return s;
	}

	// // Zero Crossing
	private static double zeroCrossing(double t1, double t2, double v1,
			double v2) {
		if (t1 >= t2)
			throw new RuntimeException("Times are in inverted order");
		if (v1 == v2) // default
			return t2;
		double t = t1 + (t1 - t2) / (v2 - v1) * v1;
		return t;
	}

}
