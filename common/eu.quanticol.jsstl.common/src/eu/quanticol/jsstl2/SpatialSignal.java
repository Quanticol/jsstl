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

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author loreti
 *
 */
public class SpatialSignal<T> {
	
	private ArrayList<Signal<T>> data;

	public SpatialSignal( int size ) {
		this.data = new ArrayList<>(size);
		for( int i=0 ; i<size ; i++ ) {
			this.data.set(i, new Signal<>());
		}
	}
	
	private SpatialSignal( ArrayList<Signal<T>> data ) {
		this.data = data;
	}
	
	public void add( double t , ArrayList<T> v ) {
		if (this.data.size() != v.size()) {
			throw new IllegalArgumentException();
		}
		for( int i=0 ; i<data.size() ; i++ ) {
			this.data.get(i).add(t, v.get(i));
		}
	}
	
	public void complete(double end) {
		for (Signal<T> signal : data) {
			signal.complete(end);
		}
	}
	
	public Signal<T> getSignal( int loc ) {
		return this.data.get(loc);
	}
	
	public <R> SpatialSignal<R> apply( Function<T,R> f ) {
		ArrayList<Signal<R>> newData = new ArrayList<>(data.size());
		for( int i=0 ; i<data.size() ; i++ ) {
			newData.set(i, data.get(i).apply(f));
		}
		return new SpatialSignal<>(newData);
	}
	
	public static <T,R> SpatialSignal<R> apply( SpatialSignal<T> ss1, BiFunction<T, T, R> f, SpatialSignal<T> ss2 ) {
		if (ss1.data.size()!=ss2.data.size()) {
			throw new IllegalArgumentException();
		}
		ArrayList<Signal<R>> newData = new ArrayList<>(ss1.data.size());
		for( int i=0 ; i<ss1.data.size() ; i++ ) {
			newData.set(i, Signal.apply(ss1.data.get(i), f, ss2.data.get(i)));
		}
		return new SpatialSignal<>(newData);
	}
}

