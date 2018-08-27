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

import java.util.ArrayList;
import java.util.Arrays;

import eu.quanticol.jsstl.core.monitor.SpatialBooleanSignal;
import eu.quanticol.jsstl.core.monitor.SpatialQuantitativeSignal;
import eu.quanticol.jsstl.core.signal.BooleanSignalTransducer;
import eu.quanticol.jsstl.core.signal.QuantitativeSignal;
import eu.quanticol.jsstl.core.space.GraphModel;
import eu.quanticol.jsstl.core.space.Location;

/**
 * @author lauretta
 *
 */
public class Signal {

	private double[] time;
	private double[][][] data;
	private GraphModel g;	
	
	private String[] variables;

	
	public Signal(GraphModel g , String[] vars , double[][][][] traj) {
		this.g = g;
		this.time = new double[traj.length];
		for (int i = 0; i < traj.length; i++)
			time[i] = i;
		final int size = traj[0].length * traj[0][0].length;
		final double[][][] trajMono = new double[size][traj.length][vars.length];
		for (int t = 0; t < traj.length; t++) {
			int i = 0;
			for (int y = 0; y < traj[0][0].length; y++)
				for (int x = 0; x < traj[0].length; x++)
					trajMono[i++][t] = traj[t][x][y];
		}
		this.data = trajMono;
		for (int location = 0; location < g.getNumberOfLocations(); location++) {
			final int timepoins_actual = data[location].length;
			final double[] lastValues = data[location][timepoins_actual - 1];
			final double[][] temporalSignal = new double[traj.length][];
			for (int timeIndex = 0; timeIndex < traj.length; timeIndex++) {
				if (timeIndex < timepoins_actual)
					temporalSignal[timeIndex] = data[location][timeIndex];
				else
					temporalSignal[timeIndex] = lastValues;
			}
			data[location] = temporalSignal;
		}		
	}
	
	public Signal(GraphModel g , double[] time , double[][][] data ) {
		this.g = g;
		this.time = time;
		this.data = data;
	}
	
	public String[] getVariables() {
		return variables;
	}
	
	public void setVariables( String[] variables ) {
		this.variables = variables;
	}
	
	public Signal(GraphModel g, double[][] data, int variables) {

		int size = lastValidIndex(data[0]);

		this.g = g;
		// this.variables = variables;

		collectTimeData(data[0], size);
		collectSignalData(g, data, size, variables);
	}

	private void collectSignalData(GraphModel g, double[][] data, int size,
			int variables) {

		this.data = new double[g.getNumberOfLocations()][size][variables];

		for (int l = 0; l < g.getNumberOfLocations(); l++) {
			int j = 1 + l;
			for (int k = 0; k < variables; k++) {
				for (int t = 0; t < size; t++) {
					this.data[l][t][k] = data[j][t];
				}
				j = j + g.getNumberOfLocations();
			}
		}
	}

	private void collectTimeData(double[] time, int size) {
		this.time = new double[size];
		for (int i = 0; i < size; i++) {
			this.time[i] = time[i];
		}
	}

	private int lastValidIndex(double[] time) {
		for (int i = 1; i < time.length; i++) {
			if (time[i] == 0) {
				return i;
			}
		}
		return time.length;
	}

	public SpatialBooleanSignal getSpatialBooleanSignal(
			SignalExpression expression, boolean isStrictInequality) {
		SpatialBooleanSignal sbs = new SpatialBooleanSignal(this.g);
		for (Location name : this.g.getLocations()) {
			double[][] var = this.data[name.getPosition()];
			double[] secondarySignal = new double[this.time.length];
			for (int i = 0; i < this.time.length; i++) {
				secondarySignal[i] = expression.eval(var[i]);
			}
			sbs.addLoc(name, BooleanSignalTransducer
					.convertPWConstantSignalToBoolean(time, secondarySignal,
							!isStrictInequality));
		}
		return sbs;
	}
	
	public SpatialQuantitativeSignal getSpatialQuantitativeSignal(
			SignalExpression expression, boolean isStrictInequality) {
		SpatialQuantitativeSignal sqs = new SpatialQuantitativeSignal(this.g);
		for (Location name : this.g.getLocations()) {
			QuantitativeSignal secondarySignal = new QuantitativeSignal(this.time[0], this.time[this.time.length-1], this.time.length);
			double[][] var = this.data[name.getPosition()];
			int k = (int) (this.time[0]/secondarySignal.getDeltaT());
			for (int i = 0; i < this.time.length; i++) {
				secondarySignal.addNextPoint(i+k,expression.eval(var[i]));
			}
			sqs.addLoc(name, secondarySignal);
		}
		return sqs;
	}
	
	public void transfomTimeStep(double endTime, double deltaT){
		int steps = (int) (endTime/deltaT)+1;
		double [] timeFix = new double[steps];
		double t = 0;
		for (int i=0; i<steps;i++){		
			timeFix [i] = t;
			t = t + deltaT;
		}

		int varnumber = this.data[0][0].length;
		double [][][] dataFix = new double[this.g.getNumberOfLocations()][steps][varnumber];
			for (int location = 0; location < g.getNumberOfLocations(); location++){
				for(int varS=0; varS<varnumber;varS++){
					int i= 0;
					for (int k=0; k<this.time.length;k++){
					while (timeFix[i]<=this.time[k]){
							dataFix[location][i][varS]=this.data[location][k][varS];
							i++;
						}
					}
				}
			}
		this.time =timeFix;
		this.data=dataFix;
				
	}

	public double[][][] getData() {
		return data;
	}
	
	public double[] getTime() {
		return this.time;
	}
	
	
	public double getValue( int sample , int location , int variable ) {
		return this.data[location][sample][variable];
	}

	public double getTime( int sample ) {
		return this.time[sample];
	}
	
	public int getSize() {
		return this.time.length;
	}
	
	public ArrayList<Location> getLocations() {
		return g.getLocations();
	}

}
