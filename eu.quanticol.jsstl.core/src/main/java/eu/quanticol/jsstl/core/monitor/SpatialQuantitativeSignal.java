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
import java.util.Map.Entry;
import java.util.Set;

import eu.quanticol.jsstl.core.signal.BooleanSignal;
import eu.quanticol.jsstl.core.signal.QuantitativeSignal;
import eu.quanticol.jsstl.core.space.GraphModel;
import eu.quanticol.jsstl.core.space.Location;

public class SpatialQuantitativeSignal {
	public GraphModel graph;
	public HashMap<Location, QuantitativeSignal> spatialQuantitativeSignal;
	private double minTimeSignal = 0.0;
	private double maxTimeSignal = Double.MAX_VALUE;
	private double deltaT;

	// public SpatialBooleanSignal() {
	// graph = new GraphModel();
	// spatialBoleanSignal = new HashMap<Location, BooleanSignal>();
	// }

	public SpatialQuantitativeSignal(GraphModel g) {
		graph = g;
		spatialQuantitativeSignal = new HashMap<Location, QuantitativeSignal>(
				g.getNumberOfLocations());
	}
	
	public void addLoc(Location loc, QuantitativeSignal quantSign) {
		this.spatialQuantitativeSignal.put(loc, quantSign);
		this.minTimeSignal = Math.max(this.minTimeSignal,
				quantSign.getInitialTime());
		this.maxTimeSignal = Math.min(this.maxTimeSignal,
				quantSign.getFinalTime());
		this.deltaT=quantSign.getDeltaT();
	}

	
	public void setLoc(Location loc, QuantitativeSignal quantSign) {
		spatialQuantitativeSignal.replace(loc, quantSign);
		this.minTimeSignal = Math.max(this.minTimeSignal,
				quantSign.getInitialTime());
		this.maxTimeSignal = Math.min(this.maxTimeSignal,
				quantSign.getFinalTime());
		this.deltaT=quantSign.getDeltaT();
	}

	@Override
	public String toString() {
		String toReturn = "";
		for (Location l : graph.getLocations()) {
			QuantitativeSignal bs = spatialQuantitativeSignal.get(l);
			if (bs != null) {
				toReturn += l + ": " + bs + " \n";
				//toReturn += l + ": " + bs + " \n";
			}
		}
		return toReturn;
	}
	
	public String toPrint() {
		String toReturn = "";
		for (Location l : graph.getLocations()) {
			QuantitativeSignal bs = spatialQuantitativeSignal.get(l);
			if (bs != null) {
				toReturn += l + ": " + bs + " \n";
				//toReturn += l + ": " + bs + " \n";
			}
		}
		return toReturn;
	}
	
	public double[] quantSat(){
		double [] quantVect = new double [graph.getNumberOfLocations()];
		int i =0;
		for (Location l : graph.getLocations()) {
			double t = spatialQuantitativeSignal.get(l).getInitialTime();
			quantVect[i]=spatialQuantitativeSignal.get(l).getValueAt(t);
			i=i+1;
		}
		return quantVect;
	}

	public double[][] quantTraj(){
		int steps = spatialQuantitativeSignal.get(graph.getLocation(0)).getPoints();
		double [][] quantVect = new double [graph.getNumberOfLocations()][steps];
		int i =0;
		for (Location l : graph.getLocations()) {
				for (int t=0; t<steps;t++){
				quantVect[i][t]=spatialQuantitativeSignal.get(l).getValueAt(t);
				}
			i=i+1;
		}		
		return quantVect;
	}
	
	public ArrayList<QuantitativeSignal> getSignals(Set<Location> subLoc) {
		ArrayList<QuantitativeSignal> signals = new ArrayList<QuantitativeSignal>();
		for (Location l : subLoc) {
			signals.add(spatialQuantitativeSignal.get(l));
		}
		return signals;
	}

	public Set<Location> filter(Set<Location> subSetLoc1, Double current) {
		Set<Location> toReturn = new HashSet<Location>();
		for (Location location : subSetLoc1) {
			QuantitativeSignal s = spatialQuantitativeSignal.get(location);
			int index= (int) (current/s.getDeltaT());
			if ((s != null) && (s.getSignal().get(index)>0)) {
				toReturn.add(location);
			}
		}
		return toReturn;
	}

	public double getInitialTime() {
		return minTimeSignal;
	}

	public double getFinalTime() {
		return maxTimeSignal;
	}

	public double getDeltaT() {
		// TODO Auto-generated method stub
		return deltaT;
	}

	public int minrun() {
		int minRuns= 10000000;
		for (Location l: graph.getLocations()){
			int k = spatialQuantitativeSignal.get(l).getPoints();
			if (k < minRuns)
				minRuns =k;
		}
		return minRuns;
	}

	public Set<Entry<Location, QuantitativeSignal>> entrySets() {
		return spatialQuantitativeSignal.entrySet();
	}

	public QuantitativeSignal getSignal(Location l) {
		return spatialQuantitativeSignal.get(l);
	}

}
