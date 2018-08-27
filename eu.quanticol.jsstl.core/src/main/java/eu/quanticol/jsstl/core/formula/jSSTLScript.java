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
package eu.quanticol.jsstl.core.formula;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import eu.quanticol.jsstl.core.monitor.SpatialBooleanSignal;
import eu.quanticol.jsstl.core.monitor.SpatialQuantitativeSignal;
import eu.quanticol.jsstl.core.space.GraphModel;

/**
 * @author loreti
 *
 */
public abstract class jSSTLScript {
	
	private Dictionary dictionary;
	
	private HashMap<Map<String,Double>,HashMap<String,SpatialBooleanSignal>> spatialBooleanCache;
	private HashMap<Map<String,Double>,HashMap<String,SpatialQuantitativeSignal>> spatialQuantitativeCache;
	
	private HashMap<String,Formula> formulae;
	
	private HashMap<String,String[]> dependencies;
	
	private TreeMap<String,ScriptParameter> parameters;

	public jSSTLScript( String[] variables ) {
		this.dictionary = new Dictionary( variables );
		this.spatialBooleanCache = new HashMap<>();
		this.spatialQuantitativeCache = new HashMap<>();
		this.spatialBooleanCache = new HashMap<>();
		this.spatialQuantitativeCache = new HashMap<>();
		this.formulae = new HashMap<>();
		this.dependencies = new HashMap<>();
		this.parameters = new TreeMap<>();
	}
	
	public void setVariablesOrder( String[] order ) {
		dictionary.setOrder( order );
	}
	
	public void addFormula( String name , Formula f , String[] references ) {
		formulae.put(name, f);
		dependencies.put(name,references);
	}
	
	public String[] getVariables() {
		return dictionary.getVariables();
	}
	
	public String[] getFormulae() {
		String[] toReturn = this.formulae.keySet().toArray( new String[this.formulae.size()] );
		Arrays.sort(toReturn);
		return toReturn;
	}
	
	public Formula getFormula( String name ) {
		return this.formulae.get(name);
	}
	
	protected SpatialBooleanSignal getBooleanCache( Map<String,Double> parameters, String name ) {
		HashMap<String,SpatialBooleanSignal> cache = spatialBooleanCache.get(parameters);
		if (cache != null) {
			return cache.get(name);
		}
		return null;
	}
	
	protected SpatialQuantitativeSignal getQuantitativeCache( Map<String,Double> parameters, String name ) {
		HashMap<String,SpatialQuantitativeSignal> cache = spatialQuantitativeCache.get(parameters);
		if (cache != null) {
			return cache.get(name);
		}
		return null;
	}
	
	public SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, String name, GraphModel g, Signal s) {
		Formula f = formulae.get(name);
		if (f != null) {
			return f.booleanCheck(parameters, g, s);
		}
		return null;
	}

	public SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, String name, GraphModel g, Signal s) {
		Formula f = formulae.get(name);
		if (f != null) {
			return f.quantitativeCheck(parameters,g, s);
		}
		return null;
	}
	
	public List<ScriptParameter> getParameters() {
		LinkedList<ScriptParameter> toReturn = new LinkedList<>();
		for (String name : parameters.keySet()) {
			toReturn.add(parameters.get(name));
		}
		return toReturn;
	}
	
	
	public void addParameter( String p , Double lb , Double ub ) {
		this.parameters.put(p,new ScriptParameter(p, lb, ub));
	}
	
	public ScriptParameter getParameter( String p ) {
		return this.parameters.get(p);
	}
	
	public int getIndex( int idx ) {
		return this.dictionary.get(idx);
	}

	public Map<String, Double> getParameterValues() {
		HashMap<String, Double> toReturn = new HashMap<>();
		for (String name : parameters.keySet()) {
			toReturn.put(name , parameters.get(name).getValue());
		}
		return toReturn;
	}

	public double[] booleanSat( String formula , Map<String,Double> parameters , GraphModel graph , Signal signal ) {
		Formula f = getFormula(formula);
		if (f != null) {
			return f.booleanCheck(parameters, graph, signal).boolSat();
		} else {
			return new double[graph.getNumberOfLocations()];
		}
		
	}
	
	public double[][] booleanSat( String formula , Map<String,Double> parameters , GraphModel graph , List<Signal> signals ) {
		double[][] data = new double[signals.size()][graph.getNumberOfLocations()];
		int count = 0;
		for( Signal s: signals ) {
			data[count] = booleanSat(formula,parameters,graph,s);
		}
		return data;
	}

	public double[][] booleanSat( String formula , Map<String,Double> parameters , GraphModel graph , Signal[] signals ) {
		double[][] data = new double[signals.length][graph.getNumberOfLocations()];
		for( int i=0 ; i<signals.length ; i++ ) {
			data[i] = booleanSat(formula,parameters,graph,signals[i]);
		}
		return data;
	}

	public double[] quantitativeSat( String formula , Map<String,Double> parameters , GraphModel graph , Signal signal ) {
		Formula f = getFormula(formula);
		if (f != null) {
			return f.quantitativeCheck(parameters, graph, signal).quantSat();
		} else {
			return new double[graph.getNumberOfLocations()];
		}
	}

	public double[][] quantitativeSat( String formula , Map<String,Double> parameters , GraphModel graph , List<Signal> signals ) {
		double[][] data = new double[signals.size()][graph.getNumberOfLocations()];
		int count = 0;
		for( Signal s: signals ) {
			data[count] = quantitativeSat(formula,parameters,graph,s);
			count++;
		}
		return data;
	}

	public double[][] quantitativeSat( String formula , Map<String,Double> parameters , GraphModel graph , Signal[] signals ) {
		double[][] data = new double[signals.length][graph.getNumberOfLocations()];
		for( int i=0 ; i<signals.length ; i++ ) {
			data[i] = quantitativeSat(formula,parameters,graph,signals[i]);
		}
		return data;
	}

	
}
