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
package eu.quanticol.jsstl2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author loreti
 *
 */
public class Signal<T> {

	private LinkedList<Sample<T>> data;
	private Sample<T> last;
	private Double end;
	
	public Signal() {
		this.data = new LinkedList<>();
		this.end = Double.NaN;
	}
	
	public double start() {
		if (data.isEmpty()) {
			return Double.NaN;
		}
		return data.getFirst().time;
	}
	
	public double end() {
		if (!end.isNaN()) {
			return end;
		}
		if (last == null) {
			return Double.NaN;
		}
		return last.time;
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	public double add( double t , T value ) {
		if (!end.isNaN()) {
			throw new IllegalArgumentException();
		}
		Sample<T> newSample = new Sample<T>(t, value);
		double interval = 0.0;
		if (last == null) {
			last = newSample;			
			data.add(newSample);
			interval = t;
		} else {
			if (last.time>t) {
				throw new IllegalArgumentException();
			} 
			if (last.value.equals(value)) {
				interval = last.time;
			} else {
				data.add(newSample);
				last = newSample;
				interval = newSample.time;
			}
		}
		return interval;
	}
	
	public <R> Signal<R> apply( Function<T, R> f ) {
		Signal<R> newSignal = new Signal<>();
		for (Sample<T> sample : data) {
			newSignal.add(sample.time, f.apply(sample.value));
		}
		if (!end.isNaN()) {
			newSignal.complete( end );
		}
		return newSignal;
	}
	
	public static <T,R> Signal<R> apply( Signal<T> s1 , BiFunction<T, T, R> f , Signal<T> s2 ) {
		Signal<R> newSignal = new Signal<>();
		SignalIterator<T> si1 = s1.getIterator();
		SignalIterator<T> si2 = s2.getIterator();
		
		double time = Math.max(s1.start(), s2.start());
		double end = Math.min(s1.end(), s2.end());
		if (time<end) {
			si1.jump(time);
			si2.jump(time);
			while ((time < end)) {
				T v1 = si1.next(time);
				T v2 = si2.next(time);
				newSignal.add(time, f.apply(v1, v2));
				time = Math.min(si1.next(), si2.next());
			}
			newSignal.complete(end);
		} 
		return newSignal;
	}
	
	public void complete(double end) {
		if (!this.end.isNaN()||(this.last==null)||(this.last.time>=end)) {
			throw new IllegalArgumentException();
		}
		this.data.add(new Sample<T>(end,this.last.value));
		this.end = end;
	}
	
	public void complete() {
		if (!this.end.isNaN()||(this.data.size()<2)) {
			throw new IllegalArgumentException();
		}
		this.end = this.last.time;
	}

	public SignalIterator<T> getIterator() {
		return new SignalIterator<T>() {
			
			private Iterator<Sample<T>> iterator = data.iterator();
			private Sample<T> current;
			private Sample<T> next;

			@Override
			public boolean hasNext() {
				return (next != null)||iterator.hasNext();
			}

			@Override
			public double next() {
				if (next == null) {
					if (iterator.hasNext()) {
						next = iterator.next();
					} else {
						return Double.NaN;
					}
				} 
				return next.time;
			}

			@Override
			public T next(double t) {
				if (t==next()) {
					current = next;
					next = null;
				} else {
					if ((current != null)&&(t>current.time)&&(t<next.time)) {
						current = new Sample<T>(t,current.value);
					} else {
						throw new IllegalArgumentException();
					}
				}
				return current.value;
			}

			@Override
			public void jump(double t) {
				if (t<start()) {
					throw new IllegalArgumentException();
				}
				if (t<end()) {
					double nextTime = next();
					while (t > nextTime) {
						next(nextTime);
						nextTime = next();
					} 
				} else {
					throw new IllegalArgumentException();
				}
			}
		};
	}

	public int size() {
		return data.size();
	}
	
}
