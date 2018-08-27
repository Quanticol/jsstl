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
package eu.quanticol.jsstl.core.monitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import eu.quanticol.jsstl.core.signal.QuantitativeSignal;
import eu.quanticol.jsstl.core.signal.QuantitativeSignalTransducer;
import eu.quanticol.jsstl.core.space.DistanceStructure;
import eu.quanticol.jsstl.core.space.Location;

public class SpatialQuantitativeSignalTransducer {

	// ///////// SURROUND ///////////////////////////////
	public static SpatialQuantitativeSignal surround(
			SpatialQuantitativeSignal sp1, SpatialQuantitativeSignal sp2,
			double d1, double d2) {

		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp1.graph);
		double t0 = Math.max(sp1.getInitialTime(), sp2.getInitialTime());
		double tf = Math.min(sp1.getFinalTime(), sp2.getFinalTime());
		double deltaT = sp1.getDeltaT();
		int points = (int) (((tf - t0) / deltaT) + 1);
		for (Location loc : sp1.graph.getLocations()) {
			// put signal to -infinity for locations that don't satisfy the
			// constraints
			HashMap<Location, QuantitativeSignal> sp1Local = resetSignal(
					sp1.graph.getDM(), sp1, t0, tf, points, loc, 0, d2);
			HashMap<Location, QuantitativeSignal> sp2Local = resetSignal(
					sp2.graph.getDM(), sp2, t0, tf, points, loc, d1, d2);
			// instance for the quantitativeSignal
			QuantitativeSignal currentSignal = new QuantitativeSignal(t0, tf,
					points);
			// computation of the value for each time
			for (int k = (int) (t0 / deltaT); k < points; k++) {
				// set the value of phi1 and phi2 for each location at the step
				// k
				HashMap<Location, Double> valueProp1 = valueProp(sp1Local, k);
				HashMap<Location, Double> valueProp2 = valueProp(sp2Local, k);
				// computation of the value of the surround in all the location
				HashMap<Location, Double> surroundVal = fixPointSurround(
						valueProp1, valueProp2);
				currentSignal.addNextPoint(k, surroundVal.get(loc));
			}
			sp0.addLoc(loc, currentSignal);
		}
		return sp0;
	}

	// put signal to -infinity for locations that don't satisfy the
	// constraints
	private static HashMap<Location, QuantitativeSignal> resetSignal(
			double[][] dM, SpatialQuantitativeSignal sp, double t0, double tf,
			int points, Location loc, double d1, double d2) {
		DistanceStructure infoGraph = new DistanceStructure(sp.graph);
		QuantitativeSignal minusInfSignal = new QuantitativeSignal(t0, tf,
				points);
		minusInfSignal.minusInfinity();
		Set<Location> subSetLoc = infoGraph
				.computSubSetSatisfy(dM, loc, d1, d2);
		HashMap<Location, QuantitativeSignal> spLocal = new HashMap<Location, QuantitativeSignal>();
		spLocal.putAll(sp.spatialQuantitativeSignal);
		for (Location location : spLocal.keySet()) {
			if (!subSetLoc.contains(location))
				spLocal.replace(location, minusInfSignal);
		}
		return spLocal;
	}

	// function to set the value of phi1 and phi2 for each location at the step
	// k
	private static HashMap<Location, Double> valueProp(
			HashMap<Location, QuantitativeSignal> sp1Loc, int k) {
		HashMap<Location, Double> valProp = new HashMap<Location, Double>();
		for (Location loc : sp1Loc.keySet()) {
			double val = sp1Loc.get(loc).getSignal().get(k);
			valProp.put(loc, val);
		}
		return valProp;
	}

	// method to compute the value of the surround in all the loc
	private static HashMap<Location, Double> fixPointSurround(
			HashMap<Location, Double> valueProp1,
			HashMap<Location, Double> valueProp2) {
		double value;
		HashMap<Location, Double> preVal = valueProp1;
		HashMap<Location, Double> locVal = new HashMap<Location, Double>();
		int k = 1;
		while (k <= valueProp1.size() + 1) {
			for (Location loc : valueProp1.keySet()) {
				value = surroundFcn(k, loc, preVal, valueProp2);
				locVal.put(loc, value);
			}
			if (preVal.equals(locVal))
				return preVal;
			preVal.putAll(locVal);
		}
		return preVal;
	}

	private static double surroundFcn(int k, Location loc,
			HashMap<Location, Double> preVal,
			HashMap<Location, Double> valueProp2) {
		double value;
		double valMinAdj = Double.POSITIVE_INFINITY;
		for (Location locAdj : loc.getNeighbourd()) {
			valMinAdj = Math.min(valMinAdj,
					Math.max(preVal.get(locAdj), valueProp2.get(locAdj)));
		}
		value = Math.min(preVal.get(loc), valMinAdj);

		return value;
	}

	// ///////// SOMEWHERE ///////////////////////////////
	public static SpatialQuantitativeSignal somewhere(
			SpatialQuantitativeSignal sp1, double d1, double d2) {

		DistanceStructure infoGraph = new DistanceStructure(sp1.graph);
		QuantitativeSignal s0;
		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp1.graph);
		for (Location loc : sp1.graph.getLocations()) {
			Set<Location> subSetLoc = infoGraph.computSubSetSatisfy(sp1.graph.getDM(), loc,
					d1, d2);
			if (subSetLoc.isEmpty()) {
				double t0 = sp1.getInitialTime();
				double tf = sp1.getFinalTime();
				double deltaT = sp1.spatialQuantitativeSignal.get(loc)
						.getDeltaT();
				int points = (int) (((tf - t0) / deltaT) + 1);
				s0 = new QuantitativeSignal(t0, tf, points);
				for (int k = (int) (t0 / deltaT); k < points; k++)
					s0.addNextPoint(k, Double.NEGATIVE_INFINITY);

			} else {
				Iterator<Location> locationIterator = subSetLoc.iterator();
				Location current = locationIterator.next();
				s0 = sp1.spatialQuantitativeSignal.get(current);
				while (locationIterator.hasNext()) {
					s0 = QuantitativeSignalTransducer.or(s0,
							sp1.spatialQuantitativeSignal.get(locationIterator
									.next()));
				}
			}
			sp0.addLoc(loc, s0);
		}
		return sp0;
	}

	// ///////// EVERYWHERE ///////////////////////////////
	public static SpatialQuantitativeSignal everywhere(
			SpatialQuantitativeSignal sp1, double d1, double d2) {

		DistanceStructure infoGraph = new DistanceStructure(sp1.graph);
		QuantitativeSignal s0;
		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp1.graph);
		for (Location loc : sp1.graph.getLocations()) {
			Set<Location> subSetLoc = infoGraph.computSubSetSatisfy(sp1.graph.getDM(), loc,
					d1, d2);
			if (subSetLoc.isEmpty()) {
				double t0 = sp1.spatialQuantitativeSignal.get(loc)
						.getInitialTime();
				double tf = sp1.spatialQuantitativeSignal.get(loc)
						.getFinalTime();
				double deltaT = sp1.spatialQuantitativeSignal.get(loc)
						.getDeltaT();
				int points = (int) (((tf - t0) / deltaT) + 1);
				s0 = new QuantitativeSignal(t0, tf, points);
				for (int k = (int) (t0 / deltaT); k < points; k++)
					s0.addNextPoint(k, Double.POSITIVE_INFINITY);

			} else {
				Iterator<Location> locationIterator = subSetLoc.iterator();
				Location current = locationIterator.next();
				s0 = sp1.spatialQuantitativeSignal.get(current);
				while (locationIterator.hasNext()) {
					s0 = QuantitativeSignalTransducer.and(s0,
							sp1.spatialQuantitativeSignal.get(locationIterator
									.next()));
				}
			}
			sp0.addLoc(loc, s0);
		}
		return sp0;
	}

	// ///////// AND ///////////////////////////////
	public static SpatialQuantitativeSignal and(SpatialQuantitativeSignal sp1,
			SpatialQuantitativeSignal sp2) {

		QuantitativeSignal s0;
		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp1.graph);
		for (Location name : sp1.spatialQuantitativeSignal.keySet()) {
			s0 = QuantitativeSignalTransducer.and(
					sp1.spatialQuantitativeSignal.get(name),
					sp2.spatialQuantitativeSignal.get(name));
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// ///////// OR ///////////////////////////////
	public static SpatialQuantitativeSignal or(SpatialQuantitativeSignal sp1,
			SpatialQuantitativeSignal sp2) {

		QuantitativeSignal s0;
		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp1.graph);
		for (Location name : sp1.spatialQuantitativeSignal.keySet()) {
			s0 = QuantitativeSignalTransducer.or(
					sp1.spatialQuantitativeSignal.get(name),
					sp2.spatialQuantitativeSignal.get(name));
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// ///////// NOT ///////////////////////////////
	public static SpatialQuantitativeSignal not(SpatialQuantitativeSignal sp1) {

		QuantitativeSignal s0;
		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp1.graph);
		for (Location name : sp1.spatialQuantitativeSignal.keySet()) {
			s0 = QuantitativeSignalTransducer.not(sp1.spatialQuantitativeSignal
					.get(name));
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// ///////// UNTIL ///////////////////////////////

	public static SpatialQuantitativeSignal until(
			SpatialQuantitativeSignal sp1, SpatialQuantitativeSignal sp2,
			double t1, double t2) {

		QuantitativeSignal s0;
		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp1.graph);
		for (Location name : sp1.spatialQuantitativeSignal.keySet()) {
			s0 = QuantitativeSignalTransducer.until(
					sp1.spatialQuantitativeSignal.get(name),
					sp2.spatialQuantitativeSignal.get(name), t1, t2);
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// ///////// ALWAYS ///////////////////////////////

	public static SpatialQuantitativeSignal always(
			SpatialQuantitativeSignal sp, double t1, double t2) {

		QuantitativeSignal s0;
		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp.graph);
		for (Location name : sp.spatialQuantitativeSignal.keySet()) {
			s0 = QuantitativeSignalTransducer.always(
					sp.spatialQuantitativeSignal.get(name), t1, t2);
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// ///////// EVENTUALLY ///////////////////////////////

	public static SpatialQuantitativeSignal eventually(
			SpatialQuantitativeSignal sp, double t1, double t2) {

		QuantitativeSignal s0;
		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp.graph);
		for (Location name : sp.spatialQuantitativeSignal.keySet()) {
			s0 = QuantitativeSignalTransducer.eventually(
					sp.spatialQuantitativeSignal.get(name), t1, t2);
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	
	public static SpatialQuantitativeSignal add( SpatialQuantitativeSignal sp1 , SpatialQuantitativeSignal sp2 ) {
		QuantitativeSignal s0;
		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp1.graph);
		for (Location name : sp1.spatialQuantitativeSignal.keySet()) {
			s0 = QuantitativeSignalTransducer.add(
					sp1.spatialQuantitativeSignal.get(name),
					sp2.spatialQuantitativeSignal.get(name));
			sp0.addLoc(name, s0);
		}
		return sp0;		
	}

	public static SpatialQuantitativeSignal divide( SpatialQuantitativeSignal sp1 , double v ) {
		QuantitativeSignal s0;
		SpatialQuantitativeSignal sp0 = new SpatialQuantitativeSignal(sp1.graph);
		for (Location name : sp1.spatialQuantitativeSignal.keySet()) {
			s0 = QuantitativeSignalTransducer.divide(sp1.spatialQuantitativeSignal
					.get(name),v);
			sp0.addLoc(name, s0);
		}
		return sp0;
	}


}
