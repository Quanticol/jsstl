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
import java.util.List;
import java.util.Locale;

import eu.quanticol.jsstl.core.signal.BooleanSignal;
import eu.quanticol.jsstl.core.signal.TimeInterval;

/**
 *
 * @author luca
 */
public class BooleanSignal {
	public LinkedList<TimeInterval> signal;
	double initial_time;
	double final_time;
	Iterator<TimeInterval> iterator;
	boolean invert = false;
	boolean initialised = false;
	boolean undefined = false;

	public BooleanSignal(double initial_time, double final_time) {
		this.initial_time = initial_time;
		this.final_time = final_time;
		signal = new LinkedList<TimeInterval>();
	}

	public BooleanSignal(double initial_time, double final_time, boolean value) {
		this(initial_time, final_time);
		addNext(final_time, true, value);
		finalise();
	}

	public void finalise() {
		// check the last time of an added interval,
		// and complete with the complementary value until final time.
		double t = this.getLastSignalEndTime();
		boolean v = !this.getLastSignalValue();
		boolean c = !this.getLastSignalEndClosure();
		if (this.signal.isEmpty()) {
			this.undefined = true;
		} else if (t < this.final_time)
			signal.addLast(new TimeInterval(t, this.final_time, v, c, true));
		this.initialised = true;
	}

	public boolean isUndefined() {
		return this.undefined;
	}

	double getLastSignalEndTime() {
		if (this.signal.isEmpty())
			return this.initial_time;
		else
			return this.signal.getLast().upper;
	}

	boolean getLastSignalValue() {
		if (this.signal.isEmpty())
			return true;
		else
			return this.signal.getLast().value;
	}

	boolean getLastSignalEndClosure() {
		if (this.signal.isEmpty())
			return false;
		else
			return this.signal.getLast().upperClosed;
	}

	public void quickComplement() {
		this.invert = !this.invert;
	}

	public void complement() {
		TimeInterval I = this.getNext();
		while (I != null) {
			I.value = !I.value;
			I = this.getNext();
//			if (I.lower!=initial_time) {
//			I.lowerClosed = !I.lowerClosed;
//		}
//		if (I.upper!=final_time) {
//			I.upperClosed = !I.upperClosed;
//		}
//		I = this.getNext();
		}
		this.invert = false;
	}

	public void addNext(double tnext, boolean tnextClose, boolean v) {
		if (tnext > this.final_time)
			tnext = this.final_time;
		double T = this.getLastSignalEndTime();
		boolean c = this.getLastSignalEndClosure();
		if (tnext > T)
			signal.add(new TimeInterval(T, tnext, v, !c, tnextClose));
		else if (tnext == T && tnextClose)
			signal.add(new TimeInterval(T, tnext, v, !c, tnextClose));
	}

	public void addNextPositive(double t0next, double t1next,
			boolean lowerClosed, boolean upperClosed) {
		if (t0next < this.initial_time)
			t0next = this.initial_time;
		if (t1next > this.final_time)
			t1next = this.final_time;
		if (t0next > t1next) {
			double t = t1next;
			t1next = t0next;
			t0next = t;
		}
		if (signal.isEmpty()) {
			if (t0next > this.initial_time || !lowerClosed) {
				//////////  problem  ////////
				signal.add(new TimeInterval(this.initial_time, t0next, false,
						true, !lowerClosed));
				signal.add(new TimeInterval(t0next, t1next, true, lowerClosed,
						upperClosed));
			} else
				signal.add(new TimeInterval(t0next, t1next, true, lowerClosed,
						upperClosed));
		} else {
			double T = this.getLastSignalEndTime();
			boolean v = this.getLastSignalValue();
			boolean c = this.getLastSignalEndClosure();
			if (t0next > T) {
				if (v)
					signal.add(new TimeInterval(T, t0next, false, !c,
							!lowerClosed));
				else {
					signal.getLast().upper = t0next;
					signal.getLast().upperClosed = !lowerClosed;
				}
				signal.add(new TimeInterval(t0next, t1next, true, lowerClosed,
						upperClosed));
			} else if (t0next <= T && T < t1next) {

				if (v) {
					if (t0next == T && !c && !lowerClosed) {
						signal.add(new TimeInterval(t0next, t0next, false,
								true, true));
						signal.add(new TimeInterval(t0next, t1next, true,
								lowerClosed, upperClosed));
					} else {
						signal.getLast().upper = t1next;
						signal.getLast().upperClosed = upperClosed;
					}
				} else {
					t0next = T;
					signal.getLast().upperClosed = !lowerClosed;
					signal.add(new TimeInterval(t0next, t1next, true,
							lowerClosed, upperClosed));
				}
			}
		}

	}

	/**
	 * Not used, comment out!
	 * 
	 * @param I
	 */
	// public void replace(TimeInterval I) {
	// TimeInterval left=null,right=null;
	// iterator = signal.iterator();
	// TimeInterval current = null, old = null;
	// //identify lower and upper intervals containing extremes
	// while (iterator.hasNext()) {
	// current = iterator.next();
	// if (current.containsInInterior(I.lower) || current.isLower(I.lower)) {
	// left = current;
	// }
	// if (current.containsInInterior(I.upper) || current.isUpper(I.upper)) {
	// right = current;
	// break;
	// }
	// }
	// //removes intervals from lower to upper included
	// iterator = signal.iterator();
	// old = null;
	// boolean remove = false;
	// while (iterator.hasNext()) {
	// current = iterator.next();
	// if (!remove && current == left) {
	// remove = true;
	// iterator.remove();
	// if (current == right)
	// break;
	// } else if (remove) {
	// iterator.remove();
	// if (current == right)
	// break;
	// } else {
	// old = current;
	// }
	// }
	// iterator = null;
	//
	// int index;
	// if (old != null)
	// index = signal.indexOf(old);
	// else
	// index = 0;
	// //check if lower has to be split
	// if (left.value != I.value && left.lower < I.lower) {
	// left.upper = I.upper;
	// signal.add(index++, left);
	// } else {
	// I.lower = left.lower;
	// }
	// //check if upper has to be split
	// if (right.value != I.value && I.upper < right.upper) {
	// right.lower = I.upper;
	// signal.add(index++, I);
	// signal.add(index++, right);
	// } else
	// signal.add(index++, I);
	// }

	/**
	 * Merges two neighbours intervals with the same value.
	 */
	public void compact() {
		iterator = signal.iterator();
		TimeInterval next = null, current = (iterator.hasNext() ? iterator
				.next() : null);
		while (iterator.hasNext()) {
			next = iterator.next();
			if (current.value == next.value) {
				iterator.remove();
				current.upper = next.upper;
				current.upperClosed = next.upperClosed;
			} else {
				current = next;
			}
		}
		iterator = null;
	}

	public void addLast(TimeInterval I) {
		signal.addLast(I);
	}

	public void reset() {
		this.iterator = null;
	}

	public TimeInterval getNextPositive() {
		if (iterator == null) {
			iterator = signal.iterator();
		}
		while (iterator.hasNext()) {
			TimeInterval I = iterator.next();
			if (I.value ^ this.invert)
				return I;
		}
		iterator = null;
		return null;
	}

	public TimeInterval getNext() {
		if (iterator == null) {
			iterator = signal.iterator();
		}
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			iterator = null;
			return null;
		}
	}

	// public void refine(List<Double> times) {
	// int i=0;
	// TimeInterval current;
	// current = signal.get(i);
	// for (Double d : times) {
	// while (!current.contains(d)) {
	// i++;
	// if (i < signal.size())
	// current = signal.get(i);
	// else
	// return;
	// }
	// if (current.containsInInterior(d)) {
	// TimeInterval I = new TimeInterval(current.lower,d,current.value);
	// current.lower = d;
	// signal.add(i++, I);
	// }
	// }
	// }

	public List<Double> getTimes() {
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(this.initial_time);
		for (TimeInterval I : signal) {
			if (I.upper < this.final_time)
				list.add(I.upper);
		}
		list.add(this.final_time);
		return list;
	}

	BooleanSignal copy() {
		BooleanSignal s = new BooleanSignal(this.initial_time, this.final_time);
		TimeInterval I = this.getNext();
		while (I != null) {
			s.signal.add(I.copy());
			I = this.getNext();
		}
		s.invert = this.invert;
		s.initialised = true;
		return s;
	}

	public TimeInterval getLastPositiveIntervalBeforeT(double T) {
		TimeInterval I0 = null, I = this.getNextPositive();
		while (I != null && I.lower < T) {
			I0 = I;
			I = this.getNextPositive();
		}
		this.reset();
		return I0;
	}

	public TimeInterval getFirstPositiveIntervalAfterT(double T) {
		TimeInterval I = this.getNextPositive();
		while (I != null && I.upper <= T) {
			I = this.getNextPositive();
		}
		this.reset();
		return I;
	}

	/**
	 * Check if the time instant T belongs to the interval.
	 * 
	 * @param T
	 * @return
	 */
	public boolean belongsTo(double T) {
		return this.initial_time <= T && T <= this.final_time;
	}

	public double getFinalTime() {
		return final_time;
	}

	public double getInitialTime() {
		return initial_time;
	}

	public double[][] getTrace() {
		if (!this.initialised)
			return null;
		int n = this.signal.size();
		double[][] trace = new double[2][2 * n];
		TimeInterval I = this.getNext();
		int i = 0;
		while (I != null) {
			trace[0][i] = (i > 0 ? I.lower : I.lower);
			trace[1][i++] = (I.value ? 1 : 0);
			trace[0][i] = I.upper;
			trace[1][i++] = (I.value ? 1 : 0);
			I = this.getNext();
		}
		return trace;
	}

	public boolean getValueAt(double T) {
		if (!this.belongsTo(T))
			throw new RuntimeException("Cannot get value at time " + T
					+ ": it does not belong to signal");
		TimeInterval I = this.getNext();
		while (I != null) {
			if (I.contains(T))
				break;
			I = this.getNext();
		}
		this.reset();
		return I.value;
	}

	@Override
	public String toString() {
		String s = "";
		s += "(" + String.format(Locale.UK, "%.2f", this.initial_time) + ") ";
		TimeInterval I = this.getNext();
		while (I != null) {
			s += (I.lowerClosed ? "[" : "]")
					+ String.format(Locale.UK, "%.2f", I.lower)
					+ (I.value ^ this.invert ? "+++" : "---")
					+ String.format(Locale.UK, "%.2f", I.upper)
					+ (I.upperClosed ? "]" : "[") + ";";
			I = this.getNext();
		}
		s += "(" + String.format(Locale.UK, "%.2f", this.final_time) + ") ";
		return s;
	}

	public boolean isEmptySignal() {
		// TODO Auto-generated method stub
		return this.signal.isEmpty();
	}

}

// ////LinkedList<PositiveInterval> signal;
// //// double initial_time;
// //// double final_time;
// //// Iterator<PositiveInterval> iterator;
// ////
// //// public BooleanSignal(double initial_time, double final_time) {
// //// this.initial_time = initial_time;
// //// this.final_time = final_time;
// //// signal = new LinkedList<PositiveInterval>();
// //// }
// ////
// //// public void addPositive(TimeInterval I) {
// //// //check if it overlaps with any interval, and find the correct position
// //// int n = signal.size();
// //// //ArrayList<PositiveInterval> overlapping = new
// ArrayList<PositiveInterval>();
// //// int i;
// //// for (i=0;i<n;i++) {
// //// //search the position w.r.t lower bound?
// //// if (signal.get(i).lower >= I.lower) {
// //// signal.addPositive(i, I);
// //// break;
// //// }
// //// }
// //// if (i==n) signal.addLast(I);
// //// }
// ////
// //// public void addLast(TimeInterval I) {
// //// signal.addLast(I);
// //// }
// ////
// ////
// //// public TimeInterval getFirst() {
// //// iterator = signal.iterator();
// //// return iterator.next();
// //// }
// ////
// //// public TimeInterval getNext() {
// //// if (iterator == null)
// //// return null;
// //// if (iterator.hasNext())
// //// return iterator.next();
// //// else {
// //// iterator = null;
// //// return null;
// //// }
// //// }
// ////
// //// //sort
// ////
// //// //compact
// ////
// //// @Override
// //// public String toString() {
// //// String s = "";
// //// s += "(" + String.format(Locale.UK, "%.2f", this.initial_time) + ") ";
// //// TimeInterval I = this.getFirst();
// //// while (I!=null) {
// //// s += String.format(Locale.UK, "%.2f", I.lower) + "--"
// //// + String.format(Locale.UK, "%.2f", I.upper) + "; ";
// //// I = this.getNext();
// //// }
// //// s += "(" + String.format(Locale.UK, "%.2f", this.final_time) + ") ";
// //// return s;
// //// }