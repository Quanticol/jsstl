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
package eu.quanticol.jsstl.space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class GraphModel implements SpaceModel<Location, WeightEdge> {

	private ArrayList<Location> locList; // list of locations
	private ArrayList<WeightEdge> wEdgeList; // list of locations
	private double[][] dM;

	// -------------------------------------------------------------
	public GraphModel() // constructor
	{
		locList = new ArrayList<Location>();
		wEdgeList = new ArrayList<WeightEdge>();
	} // end constructor

	// -------------------------------------------------------------
	// Add location

	public void addLoc(String label, int position) {
		Location l = new Location(label, position);
		locList.add(l);
	}

	// -------------------------------------------------------------
	// Add edge   //// problem puoi avere diversi archi da stesso input output
	public void addEdge(int start, int end, double weight) {
		if (weight<0.0) {
			throw new IllegalArgumentException();
		}
		locList.get(start).addNeighbour(locList.get(end));
		locList.get(end).addNeighbour(locList.get(start));
		wEdgeList.add(new WeightEdge(locList.get(start), locList.get(end),
				weight));
	}

	// -------------------------------------------------------------
	// locations position
	public int getPosition(String label) {
		for (Location l : this.locList) {
			if (l.getLabel().equals(label)) {
				return l.getPosition();
			}
		}
		return 0;
	}

	// -------------------------------------------------------------
	// -------------------------------------------------------------
	// locations Set
	public Set<Location> getPoints() {
		Set<Location> setLoc = new HashSet<Location>(locList);
		return setLoc;
	}

	// -------------------------------------------------------------
	// edges Set
	public Set<WeightEdge> getEdges() {
		Set<WeightEdge> setEdg = new HashSet<WeightEdge>(wEdgeList);
		return setEdg;
	}
	
	public Object[] getEdgesArray() {
		return wEdgeList.toArray();
	}
	
	// -------------------------------------------------------------
	// Neighborhoods

	public ArrayList<Location> returnNeighbourd(Location l) {
		return l.getNeighbourd();
	}

	// Boundary
	public Set<Location> getExternalBorder(Set<Location> subSetLoc) {
		Set<Location> externalBorder = new HashSet<Location>();
		for (Location l : subSetLoc) {
			for (Location lAdj : l.getNeighbourd()) {
				if (!subSetLoc.contains(lAdj)) {
					if (!externalBorder.contains(lAdj)) {
						externalBorder.add(lAdj);
					}
				}
			}
		}
		return externalBorder;
	}

	// distance Matrix
	public void dMcomputation() {
		DistanceStructure infoGraph = new DistanceStructure(this);
		this.dM = infoGraph.returnDistMatrix();
	}

	// distance Matrix
	public void dMcomputationOld() {
		DistanceStructure infoGraph = new DistanceStructure(this);
		this.dM = infoGraph.returnDistMatrixOld();
	}
	
	public double[][] getDM() {
		return this.dM;
	}
	
	public int getNumberOfLocations() {
		return this.locList.size();
	}

	public Location getLocation(int j) {
		return this.locList.get(j);
	}

	public int getNumberOfEdges() {
		return this.wEdgeList.size();
	}

	public WeightEdge getEdge(int i) {
		return this.wEdgeList.get(i);
	}

	public ArrayList<Location> getLocations() {
		return this.locList;
	}
	
	public double getWeight( int i , int j ) {
		for (WeightEdge e : wEdgeList) {
			if ((e.lStart.getPosition() == i)&&(e.lEnd.getPosition()==j)) {
				return e.weight;
			}
		}
		return -1;
	}
	
	
	public static GraphModel createGrid( int rows , int columns , double distance ) {
        GraphModel model = new GraphModel();
        for( int i=0 ; i<rows ; i++ ) {
        	for( int j=0 ; j<columns ; j++ ) {
        		model.addLoc((i+1)+"_"+(j+1), i*columns+j);
        	}        	
        }
        for( int i=0 ; i<rows ; i++ ) {
        	for( int j=0 ; j<columns ; j++ ) {
        		if (j!=columns-1) {
            		model.addEdge(i*columns+j, i*columns+(j+1), distance);
        		}
        		if (i!=rows-1) {
        			model.addEdge(i*columns+j, (i+1)*columns+j, distance);
        		}
        	}        	
        }
        return model;
	}

}
