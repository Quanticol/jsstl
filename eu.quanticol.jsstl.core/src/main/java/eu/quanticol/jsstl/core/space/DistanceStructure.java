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
package eu.quanticol.jsstl.core.space;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.*;
import org.jgrapht.graph.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DistanceStructure {
	GraphModel g;
	public SimpleWeightedGraph<String, DefaultWeightedEdge> jGraphT;

	public DistanceStructure(GraphModel graph) { // constructor
		this.g = graph;
		this.jGraphT = new SimpleWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		// // vertex in JgraphT
		for (int i = 0; i < g.getNumberOfLocations(); i++) {
			jGraphT.addVertex(g.getLocation(i).getLabel());
		}

		// // edge in JgraphT
		DefaultWeightedEdge[] e = new DefaultWeightedEdge[g.getNumberOfLocations()
				* g.getNumberOfLocations()];

		for (int i = 0; i < g.getNumberOfEdges(); i++) {
			e[i] = jGraphT.addEdge(g.getEdge(i).lStart.getLabel(),
					g.getEdge(i).lEnd.getLabel());
			jGraphT.setEdgeWeight(e[i], g.getEdge(i).weight);
		}
	}

	// /// shortest path
	public double DistShortestPath(String start, String end) {
		DijkstraShortestPath<String, DefaultWeightedEdge> path = new DijkstraShortestPath<String, DefaultWeightedEdge>(
				jGraphT, start, end);
		// DijkstraShortestPath<String, DefaultWeightedEdge> path = new
		// DijkstraShortestPath<>(graph, start, end);

		double length_shortest_path = path.getPathLength();
		return length_shortest_path;
	}

	// /// return list path
	public List<DefaultWeightedEdge> ShortestPath(String start, String end) {
		List<DefaultWeightedEdge> shortestPath = DijkstraShortestPath
				.findPathBetween(jGraphT, start, end);

		return shortestPath;

	}

	// // return distance matrix
	public double[][] returnDistMatrix() {
		
		double[][] dMatrix = new double[g.getNumberOfLocations()][g.getNumberOfLocations()];
		
		FloydWarshallShortestPaths<String, DefaultWeightedEdge> path = new FloydWarshallShortestPaths<String, DefaultWeightedEdge>(
				this.jGraphT);

		Collection<GraphPath<String, DefaultWeightedEdge>> pathCollection = path
				.getShortestPaths();
		
		for (GraphPath<String, DefaultWeightedEdge> gPath : pathCollection) {
			int start = g.getPosition(gPath.getStartVertex());
			int end = g.getPosition(gPath.getEndVertex());
			dMatrix[start][end]=gPath.getWeight();
		}

		for (int i = 0; i < g.getNumberOfLocations(); i++)
			dMatrix[i][i] = 0;
		return dMatrix;
	}

	// // return distance matrix
	public double[][] returnDistMatrixOld() {
		double[][] dMatrix = new double[g.getNumberOfLocations()][g.getNumberOfLocations()];

		for (int i = 0; i < g.getNumberOfLocations(); i++) {
			for (int j = 0; j < g.getNumberOfLocations(); j++) {
				if (j == i)
					dMatrix[j][j] = 0;
				else if (i < j) {
					DijkstraShortestPath<String, DefaultWeightedEdge> path = new DijkstraShortestPath<String, DefaultWeightedEdge>(
							jGraphT, g.getLocation(i).getLabel(), g.getLocation(i).getLabel());
					dMatrix[i][j] = path.getPathLength();
				}
			}

		}
		for (int i = 0; i < g.getNumberOfLocations(); i++) {
			for (int j = 0; j < g.getNumberOfLocations(); j++) {
				if (i > j)
					dMatrix[i][j] = dMatrix[j][i];
			}
		}
		return dMatrix;
	}

	public Set<Location> computSubSetSatisfy(double[][] dM, Location loc,
			double d1, double d2) {
		HashSet<Location> subSetLoc = new HashSet<Location>();
		for (int j = 0; j < dM[loc.getPosition()].length; j++) {
			if (dM[loc.getPosition()][j] >= d1) {
				if (dM[loc.getPosition()][j] <= d2) {
					subSetLoc.add(g.getLocation(j));
				}
			}
		}
		return subSetLoc;
	}

	// public int getDistance(String start,String end){
	// if (!(graph.containsVertex(start) && graph.containsVertex(end))) {
	//
	// }
	// DijkstraShortestPath<String,DefaultWeightedEdge> sp=new
	// DijkstraShortestPath<String, DefaultWeightedEdge>(graph,start, end);
	// double pathLength=sp.getPathLength();
	// System.out.println("DistanceGraph.getDistance() " +
	// sp.getPathEdgeList());
	// if (pathLength == Double.POSITIVE_INFINITY) {
	// pathLength=0;
	// Set<String> allSupers=hierarchy.getAllSupers(end.getClassName());
	// for ( String sup : allSupers) {
	// String super1=end.getSuper(sup);
	// int distance=getDistance(start,super1);
	// if (distance > 0) {
	// return distance;
	// }
	// }
	// }
	// if (pathLength < 0.) {
	// pathLength=0;
	// }
	// return (int)pathLength;
	// }

	// neighborListOf(V v)
	// http://jgrapht.org/javadoc/org/jgrapht/alg/NeighborIndex.html

}
