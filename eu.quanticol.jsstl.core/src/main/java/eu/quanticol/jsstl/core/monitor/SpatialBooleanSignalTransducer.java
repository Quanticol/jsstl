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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import eu.quanticol.jsstl.core.signal.BooleanSignal;
import eu.quanticol.jsstl.core.signal.BooleanSignalTransducer;
import eu.quanticol.jsstl.core.space.DistanceStructure;
import eu.quanticol.jsstl.core.space.GraphModel;
import eu.quanticol.jsstl.core.space.Location;

public class SpatialBooleanSignalTransducer {
	// ///////// SURROUND ///////////////////////////////
	public static SpatialBooleanSignal surround(SpatialBooleanSignal sp1,
			SpatialBooleanSignal sp2, double d1, double d2) {

		// ///// computation of the distance matrix
		DistanceStructure infoGraph = new DistanceStructure(sp1.graph);

		SpatialBooleanSignal sp0 = new SpatialBooleanSignal(sp1.graph);
		double initialTime = Math.max(sp1.getInitialTime(),
				sp2.getInitialTime());
		double finalTime = Math.min(sp1.getFinalTime(), sp2.getFinalTime());
		for (Location loc : sp1.graph.getLocations()) {
			BooleanSignal currentSignal = new BooleanSignal(initialTime,
					finalTime);
			Set<Location> subSetLoc1 = infoGraph.computSubSetSatisfy(sp1.graph.getDM(), loc,
					0, d2);
			Set<Location> subSetLoc2 = infoGraph.computSubSetSatisfy(sp1.graph.getDM(), loc,
					d1, d2);
			ArrayList<BooleanSignal> signalsL1 = sp1.getSignals(subSetLoc1);
			ArrayList<BooleanSignal> signalsL2 = sp2.getSignals(subSetLoc1);

			LinkedList<Double> intervals = BooleanSignalTransducer
					.internalCovering(
							BooleanSignalTransducer.internalCovering(signalsL1),
							BooleanSignalTransducer.internalCovering(signalsL2));

			Iterator<Double> intervalIterator = intervals.iterator();
			// We know that intervals contains at least two elements.
			Double current = intervalIterator.next();
			Double next = intervalIterator.next();
			while (current != null && current >= initialTime
					&& current <= finalTime) {
				Set<Location> setV = sp1.filter(subSetLoc1, current);
				Set<Location> setQ = sp2.filter(subSetLoc2, current);
				if (checkSurround(loc, sp1.graph, setV, setQ)) {
					currentSignal.addNextPositive(current, next, true, false);
				}
				if (intervalIterator.hasNext()) {
					current = next;
					next = intervalIterator.next();
				} else {
					current = null;
					next = null;
				}
			}
			if (currentSignal.isEmptySignal())
				currentSignal.addNext(sp1.spatialBoleanSignal.get(loc)
						.getFinalTime(), true, false);
			currentSignal.finalise();
			sp0.addLoc(loc, currentSignal);
		}
		return sp0;
	}

	private static boolean checkSurround(Location loc, GraphModel graph,
			Set<Location> setV, Set<Location> setQ) {
		Set<Location> setV_Q = new HashSet<Location>();
		if (!(setV.contains(loc))) {
			return false;
		}
		setV_Q.addAll(setV);
		setV_Q.addAll(setQ);
		Set<Location> setT = graph.getExternalBorder(setV_Q);
		while (!setT.isEmpty()) {
			Set<Location> setT1 = new HashSet<Location>();
			for (Location l1 : setT) {
				for (Location l2 : l1.getNeighbourd()) {
					if (l2 == loc) {
						return false;
					}
					if (setV.remove(l2)) {
						if (!setQ.contains(l2)) {
							setT1.add(l2);
						}
					}
				}
			}
			setT = setT1;
		}
		return true;
	}

	// ///////// SOMEWHERE ///////////////////////////////
	public static SpatialBooleanSignal somewhere(SpatialBooleanSignal sp1,
			double d1, double d2) {

		
		DistanceStructure infoGraph = new DistanceStructure(sp1.graph);
		BooleanSignal s0;
		SpatialBooleanSignal sp0 = new SpatialBooleanSignal(sp1.graph);
		for (Location loc : sp1.graph.getLocations()) {
			Set<Location> subSetLoc = infoGraph.computSubSetSatisfy(sp1.graph.getDM(), loc,
					d1, d2);
			if (subSetLoc.isEmpty()) {
				s0 = new BooleanSignal(0, sp1.spatialBoleanSignal.get(loc)
						.getFinalTime());
				s0.addNext(sp1.spatialBoleanSignal.get(loc).getFinalTime(),
						true, false);
				s0.finalise();

			} else {
				Iterator<Location> locationIterator = subSetLoc.iterator();
				Location current = locationIterator.next();
				s0 = sp1.spatialBoleanSignal.get(current);
				while (locationIterator.hasNext()) {
					s0 = BooleanSignalTransducer.or(s0, sp1.spatialBoleanSignal
							.get(locationIterator.next()));
				}
			}
			sp0.addLoc(loc, s0);
		}
		return sp0;
	}

	// ///////// EVERYWHERE ///////////////////////////////
	public static SpatialBooleanSignal everywhere(SpatialBooleanSignal sp1,
			double d1, double d2) {

		DistanceStructure infoGraph = new DistanceStructure(sp1.graph);
		BooleanSignal s0;
		SpatialBooleanSignal sp0 = new SpatialBooleanSignal(sp1.graph);
		for (Location loc : sp1.graph.getLocations()) {
			Set<Location> subSetLoc = infoGraph.computSubSetSatisfy(sp1.graph.getDM(), loc,
					d1, d2);
			if (subSetLoc.isEmpty()) {
				s0 = new BooleanSignal(0, sp1.spatialBoleanSignal.get(loc)
						.getFinalTime());
				s0.addNext(sp1.spatialBoleanSignal.get(loc).getFinalTime(),
						true, true);
				s0.finalise();

			} else {
				Iterator<Location> locationIterator = subSetLoc.iterator();
				Location current = locationIterator.next();
				s0 = sp1.spatialBoleanSignal.get(current);
				while (locationIterator.hasNext()) {
					s0 = BooleanSignalTransducer
							.and(s0, sp1.spatialBoleanSignal
									.get(locationIterator.next()));
				}
			}
			sp0.addLoc(loc, s0);
		}
		return sp0;
	}

	// ///////// AND ///////////////////////////////
	public static SpatialBooleanSignal and(SpatialBooleanSignal sp1,
			SpatialBooleanSignal sp2) {

		BooleanSignal s0;
		SpatialBooleanSignal sp0 = new SpatialBooleanSignal(sp1.graph);
		for (Location name : sp1.spatialBoleanSignal.keySet()) {
			s0 = BooleanSignalTransducer.and(sp1.spatialBoleanSignal.get(name),
					sp2.spatialBoleanSignal.get(name));
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// ///////// OR ///////////////////////////////
	public static SpatialBooleanSignal or(SpatialBooleanSignal sp1,
			SpatialBooleanSignal sp2) {

		BooleanSignal s0;
		SpatialBooleanSignal sp0 = new SpatialBooleanSignal(sp1.graph);
		for (Location name : sp1.spatialBoleanSignal.keySet()) {
			s0 = BooleanSignalTransducer.or(sp1.spatialBoleanSignal.get(name),
					sp2.spatialBoleanSignal.get(name));
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// ///////// IMPLY ///////////////////////////////
	public static SpatialBooleanSignal imply(SpatialBooleanSignal sp1,
			SpatialBooleanSignal sp2) {

		BooleanSignal s0;
		SpatialBooleanSignal sp0 = new SpatialBooleanSignal(sp1.graph);
		for (Location name : sp1.spatialBoleanSignal.keySet()) {
			s0 = BooleanSignalTransducer.imply(
					sp1.spatialBoleanSignal.get(name),
					sp2.spatialBoleanSignal.get(name));
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// ///////// NOT ///////////////////////////////
	public static SpatialBooleanSignal not(SpatialBooleanSignal sp1) {

		BooleanSignal s0;
		SpatialBooleanSignal sp0 = new SpatialBooleanSignal(sp1.graph);
		for (Location name : sp1.spatialBoleanSignal.keySet()) {
			s0 = BooleanSignalTransducer.not(sp1.spatialBoleanSignal.get(name));
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// ///////// UNTIL ///////////////////////////////
	public static SpatialBooleanSignal until(SpatialBooleanSignal sp1,
			SpatialBooleanSignal sp2, double t1, double t2) {

		BooleanSignal s0;
		SpatialBooleanSignal sp0 = new SpatialBooleanSignal(sp1.graph);
		for (Location name : sp1.spatialBoleanSignal.keySet()) {
			s0 = BooleanSignalTransducer.until(
					sp1.spatialBoleanSignal.get(name),
					sp2.spatialBoleanSignal.get(name), t1, t2);
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// // ///////// Truth UntilZero ///////////////////////////////
	// public static Truth[] untilZero(SpatialBooleanSignal sp1,
	// SpatialBooleanSignal sp2, double t1, double t2) {
	// int i = 0;
	// Truth[] truth = new Truth[sp1.spatialBoleanSignal.size()];
	// for (Location name : sp1.spatialBoleanSignal.keySet()) {
	// truth[i] = BooleanSignalTransducer.untilZero(
	// sp1.spatialBoleanSignal.get(name),
	// sp2.spatialBoleanSignal.get(name), t1, t2);
	// i++;
	// }
	// return truth;
	// }

	// ///////// ALWAYS ///////////////////////////////
	public static SpatialBooleanSignal always(SpatialBooleanSignal sp1,
			double t1, double t2) {

		BooleanSignal s0;
		SpatialBooleanSignal sp0 = new SpatialBooleanSignal(sp1.graph);
		for (Location name : sp1.spatialBoleanSignal.keySet()) {
			s0 = BooleanSignalTransducer.always(
					sp1.spatialBoleanSignal.get(name), t1, t2);
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// // ///////// Truth AlwaysZero ///////////////////////////////
	// public static Truth[] alwaysZero(SpatialBooleanSignal sp1, double t1,
	// double t2) {
	// int i = 0;
	// Truth[] truth = new Truth[sp1.spatialBoleanSignal.size()];
	// for (Location name : sp1.spatialBoleanSignal.keySet()) {
	// truth[i] = BooleanSignalTransducer.alwaysZero(
	// sp1.spatialBoleanSignal.get(name), t1, t2);
	// i++;
	// }
	// return truth;
	// }

	// ///////// EVENTUALLY ///////////////////////////////
	public static SpatialBooleanSignal eventually(SpatialBooleanSignal sp1,
			double t1, double t2) {

		BooleanSignal s0;
		SpatialBooleanSignal sp0 = new SpatialBooleanSignal(sp1.graph);
		for (Location name : sp1.spatialBoleanSignal.keySet()) {
			s0 = BooleanSignalTransducer.eventually(
					sp1.spatialBoleanSignal.get(name), t1, t2);
			sp0.addLoc(name, s0);
		}
		return sp0;
	}

	// // ///////// Truth EventuallyZero ///////////////////////////////
	// public static Truth[] eventuallyZero(SpatialBooleanSignal sp1, double t1,
	// double t2) {
	// int i = 0;
	// Truth[] truth = new Truth[sp1.spatialBoleanSignal.size()];
	// for (Location name : sp1.spatialBoleanSignal.keySet()) {
	// truth[i] = BooleanSignalTransducer.eventuallyZero(
	// sp1.spatialBoleanSignal.get(name), t1, t2);
	// i++;
	// }
	// return truth;
	// }

	// ------------------------------------------------------------------------------------------

	// /////////////// convertSignalInSpatialSignal
	public static void convertContSignInGraph(double[] space, double[] time,
			double[] signal) {
		String[] label = new String[space.length];
		for (int i = 0; i < space.length; i++) {
			label[i] = String.valueOf(space[i]);
			GraphModel g = new GraphModel();
			for (int j = 0; j < label.length; j++)
				g.addLoc(label[j], 0);

		}
	}

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------

	// /////////////// convertPWLinearSignalToBoolean
	public static SpatialBooleanSignal convertSpatialPWLinearSignalToBoolean(
			GraphModel g, ArrayList<Location> loc, double[] time,
			double[] signal, boolean zeroIsTrue) {
		HashMap<Location, double[]> locSignal = new HashMap<Location, double[]>();
		int k = 0;
		for (int i = 0; i < loc.size(); i++) {
			locSignal.put(
					loc.get(i),
					java.util.Arrays.copyOfRange(signal, k, k + signal.length
							/ loc.size()));
			k = k + signal.length / loc.size();
		}

		SpatialBooleanSignal spaBolSign = new SpatialBooleanSignal(g);
		BooleanSignal convSign;
		for (int i = 0; i < 3; i++) {
			convSign = BooleanSignalTransducer.convertPWLinearSignalToBoolean(
					time, locSignal.get(loc.get(i)), zeroIsTrue);

			spaBolSign.addLoc(loc.get(i), convSign);
		}

		return spaBolSign;
	}

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------

	// /////////////// convertPWLinearSignalToBoolean with LOC-SPATIAL SIGNAL
	public static SpatialBooleanSignal convertSpatialPWLinearSignalToBoolean(
			GraphModel g, double[] time,
			HashMap<Location, double[]> spatialSignal, boolean zeroIsTrue) {

		SpatialBooleanSignal spaBolSign = new SpatialBooleanSignal(g);
		BooleanSignal convSign;
		for (Location name : spatialSignal.keySet()) {
			convSign = BooleanSignalTransducer.convertPWLinearSignalToBoolean(
					time, spatialSignal.get(name), zeroIsTrue); // false means
																// zero
																// is false
			spaBolSign.addLoc(name, convSign);
		}

		return spaBolSign;
	}

	// ------------------------------------------------------------------------------------------

	// /////////////// convertPWConstantSignalToBoolean
	public static SpatialBooleanSignal convertSpatialPWConstantSignalToBoolean(
			GraphModel g, ArrayList<Location> loc, double[] time,
			double[] signal, boolean zeroIsTrue) {
		HashMap<Location, double[]> locSignal = new HashMap<Location, double[]>();
		int k = 0;
		for (int i = 0; i < loc.size(); i++) {
			locSignal.put(
					loc.get(i),
					java.util.Arrays.copyOfRange(signal, k, k + signal.length
							/ loc.size()));
			k = k + signal.length / loc.size();
		}

		SpatialBooleanSignal spaBolSign = new SpatialBooleanSignal(g);
		BooleanSignal convSign;
		for (int i = 0; i < 3; i++) {
			convSign = BooleanSignalTransducer
					.convertPWConstantSignalToBoolean(time,
							locSignal.get(loc.get(i)), zeroIsTrue); // false
																	// means
			// zero
			// is false
			spaBolSign.addLoc(loc.get(i), convSign);
		}

		return spaBolSign;
	}

	// ------------------------------------------------------------------------------------------
	// /////////////// convertPWConstantSignalToBoolean with LOC-SPATIAL SIGNAL
	public static SpatialBooleanSignal convertSpatialPWConstantSignalToBoolean(
			GraphModel g, double[] time,
			HashMap<Location, double[]> spatialSignal, boolean zeroIsTrue) {

		SpatialBooleanSignal spaBolSign = new SpatialBooleanSignal(g);
		BooleanSignal convSign;
		for (Location name : spatialSignal.keySet()) {
			convSign = BooleanSignalTransducer
					.convertPWConstantSignalToBoolean(time,
							spatialSignal.get(name), zeroIsTrue); // false means
																	// zero
																	// is false
			spaBolSign.addLoc(name, convSign);
		}

		return spaBolSign;
	}
	// ------

}
