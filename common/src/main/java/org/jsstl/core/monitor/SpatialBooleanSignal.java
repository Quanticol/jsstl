package org.jsstl.core.monitor;

import org.jsstl.core.space.GraphModel;
import org.jsstl.core.space.Location;
import org.jsstl.util.signal.BooleanSignal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class SpatialBooleanSignal {

	public GraphModel graph;
	public HashMap<Location, BooleanSignal> spatialBoleanSignal;
	private double minTimeSignal = 0.0;
	private double maxTimeSignal = Double.MAX_VALUE;

	// public SpatialBooleanSignal() {
	// graph = new GraphModel();
	// spatialBoleanSignal = new HashMap<Location, BooleanSignal>();
	// }

	public SpatialBooleanSignal(GraphModel g) {
		graph = g;
		spatialBoleanSignal = new HashMap<Location, BooleanSignal>(
				g.getNumberOfLocations());
	}

	public void addLoc(Location loc, BooleanSignal bolSign) {
		this.spatialBoleanSignal.put(loc, bolSign);
		this.minTimeSignal = Math.max(this.minTimeSignal,
				bolSign.getInitialTime());
		this.maxTimeSignal = Math.min(this.maxTimeSignal,
				bolSign.getFinalTime());
	}

	public void setLoc(Location loc, BooleanSignal bolSign) {
		spatialBoleanSignal.replace(loc, bolSign);
		this.minTimeSignal = Math.max(this.minTimeSignal,
				bolSign.getInitialTime());
		this.maxTimeSignal = Math.min(this.maxTimeSignal,
				bolSign.getFinalTime());
	}

	@Override
	public String toString() {
		String toReturn = "";
		for (Location l : graph.getLocations()) {
			BooleanSignal bs = spatialBoleanSignal.get(l);
			if (bs != null) {
				toReturn += l + ": " + bs + " \n";
			}
		}
		return toReturn;
	}

	public double[] boolSat(){
		double [] boolVect = new double [graph.getNumberOfLocations()];
		int i =0;
		for (Location l : graph.getLocations()) {
			double t = spatialBoleanSignal.get(l).getInitialTime();
			if (spatialBoleanSignal.get(l).getValueAt(t) == true)
				boolVect[i]=1;
			else
				boolVect[i]=0;
			i=i+1;
		}
		return boolVect;
	}

	public ArrayList<BooleanSignal> getSignals(Set<Location> subLoc) {
		ArrayList<BooleanSignal> signals = new ArrayList<BooleanSignal>();
		for (Location l : subLoc) {
			signals.add(spatialBoleanSignal.get(l));
		}
		return signals;
	}

	public Set<Location> filter(Set<Location> subSetLoc1, Double current) {
		Set<Location> toReturn = new HashSet<Location>();
		for (Location location : subSetLoc1) {
			BooleanSignal s = spatialBoleanSignal.get(location);
			if ((s != null) && (s.getValueAt(current))) {
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
	
	public Set<Entry<Location, BooleanSignal>> entrySets() {
		return spatialBoleanSignal.entrySet();
	}

	public BooleanSignal getSignal(Location l) {
		return spatialBoleanSignal.get(l);
	}

}
