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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author loreti
 *
 */
public class SpatialModel {

	protected String[] labels;
	protected Map<Integer,Map<Integer,Double>> post = new HashMap<>();
	protected Map<Integer,Map<Integer,Double>> pre = new HashMap<>();
	protected Map<Integer,Map<Integer,Double>> distance = new HashMap<>(); 
	
	public SpatialModel( int size ) {
		this.labels = new String[size];
		Arrays.setAll(labels, x -> x+"");
	}
	
	public SpatialModel( String ... labels ) {
		this.labels = Arrays.copyOf(labels, labels.length);
	}
	
	public void set(int i, int j, double w) {
		Map<Integer,Double> next = this.post.getOrDefault(i, new HashMap<>());
		next.put(j, w);
		this.post.put(i, next);
	}
	
	public double weight( int src , int trg ) {
		Map<Integer,Double> nextMap = post.get(src);
		if (nextMap == null) {
			return Double.NaN;
		}
		return nextMap.getOrDefault(trg, Double.NaN);
	}
	
	public Set<Integer> getNext( int src ) {
		Map<Integer,Double> next = post.get(src);
		if (next == null) {
			return new HashSet<>();
		}
		return next.keySet();
	}
	
	public double distance( int src , int trg ) {
		Map<Integer,Double> next = distance.get(src);
		if (next == null) {
			return Double.NaN;
		}
		return next.getOrDefault(trg, Double.NaN);
	}
	
	public void computeDistances() {
		initDistance();
		for( int k=0 ; k<labels.length ; k++ ) {
			for( int i=0 ; i<labels.length ; i++ ) {
				Map<Integer,Double> distI = distance.get(i);
				for( int j=0 ; j<labels.length ; j++ ) {
					Double dist_i_k = distI.getOrDefault(k, Double.NaN);
					if (!dist_i_k.isNaN()) {
						Double dist_k_j = distance(k,j);
						if (!dist_k_j.isNaN()) {
							Double dist_i_j = distI.getOrDefault(j, Double.NaN);
							double v = dist_i_k+dist_k_j;
							if ((dist_i_j.isNaN())||(dist_i_j>v)) {
								distI.put(j, v);
							}
						}
					}
				}
			}
		}
	}

	private void initDistance() {
		this.distance = new HashMap<Integer, Map<Integer,Double>>();
		for( int i=0 ; i<labels.length ; i++ ) {
			HashMap<Integer,Double> d = new HashMap<>();
			this.distance.put(i, d);
			Map<Integer,Double> next = post.get(i);
			d.put(i, 0.0);
			if (next != null) {
				for (Entry<Integer, Double> entry : next.entrySet()) {
					d.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	public Double get(int i, int j) {
		Map<Integer,Double> next = this.post.get(i);		
		return next.getOrDefault(j, Double.NaN);
	}

	public int size() {
		return labels.length;
	}
	
	public Set<Integer> get(int i, Predicate<Double> p) {
		Map<Integer,Double> distI = this.distance.get(i);
		Set<Integer> toReturn = new HashSet<>();
		for (Entry<Integer, Double> e : distI.entrySet()) {
			if (p.test(e.getValue())) {
				toReturn.add(e.getKey());
			}
		}
		return toReturn;
	}
}
