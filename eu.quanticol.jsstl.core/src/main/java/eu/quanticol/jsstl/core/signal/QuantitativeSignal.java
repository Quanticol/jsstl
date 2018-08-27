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
package eu.quanticol.jsstl.core.signal;

import java.util.ArrayList;
import java.util.List;


public class QuantitativeSignal {
	ArrayList<Double> signal;
	double initialTime;
	double finalTime;
	double deltaT;
	int points;

	public QuantitativeSignal(double initialTime, double finalTime, int points) {
		this.initialTime = initialTime;
		this.finalTime = finalTime;
		this.deltaT = (finalTime - initialTime) / (points -1);
		this.points = points;
		setSignal(new ArrayList<Double>());
		if (this.initialTime !=0){
			for(int k=0; k<((int)(this.initialTime/this.deltaT));k++)
				this.signal.add(Double.NaN);
		}
	}

	public void addNextPoint(double tnext) {
		this.signal.add(tnext);
	}
	
	public void addNextPoint(int index, double tnext) {
		this.signal.add(index, tnext);
	}
	
	public void setPoint(int index, double tnext) {
		this.signal.add(index, tnext);
	}	
	
	/**
	 * @param signal
	 *            the signal to set
	 */
	public void setSignal(ArrayList<Double> signal) {
		this.signal = signal;
	}

	/**
	 * @return the signal
	 */
	public ArrayList<Double> getSignal() {
		return signal;
	}


	@Override
	public String toString() {
		String s = "";
		if (this.signal.isEmpty())
			s = " empty signal";
		else {
			for (double signal: this.signal) {
				String formattedString = String.format("%.02f", signal);
				s = s + " " + formattedString;
			}
		}
		return s;
	}




	public double getInitialTime() {
		return this.initialTime;
	}

	public double getFinalTime() {
		return this.finalTime;
	}

	public double getDeltaT() {
		return this.deltaT;
	}

	public boolean isEmptySignal() {
		// TODO Auto-generated method stub
		return this.getSignal().isEmpty();
	}

	
	public ArrayList<Double> complement() {
		ArrayList<Double> complSignal = new ArrayList<Double>();
		for (double value: this.signal) {
			complSignal.add(-value);
		}
		return complSignal;
	}
	
	
	


	public boolean belongsTo(double T) {
		return this.initialTime <= T && T <= this.finalTime;
	}

	public List<Double> getTimes() {
		ArrayList<Double> list = new ArrayList<Double>();
		for (int i = 0; i < this.points; i++) {
			list.add(i * this.deltaT);
		}
		return list;
	}

	public boolean contains(double t) {
		if (this.initialTime <= t || t <= finalTime)
			return true;
		else
			return false;
	}

	public int getPoints() {
		// TODO Auto-generated method stub
		return this.points;
	}

	
	public void minusInfinity() {	
		if (this.signal.isEmpty())
		for (int k = (int) (this.initialTime / this.deltaT); k < this.points; k++)
			this.addNextPoint(k, Double.NEGATIVE_INFINITY);
		else
			for (int k = (int) (this.initialTime / this.deltaT); k < this.points; k++)
				this.setPoint(k, Double.NEGATIVE_INFINITY);
		
	}

	public double getValueAt(double t) {
		int i = (int) (t/deltaT);		
		return this.signal.get(i);
	}

	public double getValueAt(int i) {
		return this.signal.get(i);
	}
//	public double[][] getTrace() {
//		double[][] trace = new double[2][this.points];
//		trace[0][0] = this.initialTime;
//		trace[1][0] = this.getNext();
//		for (int i = 1; i < this.points; i++) {
//			trace[0][i] = this.initialTime * deltaT * points;
//			trace[1][i] = this.getNext();
//		}
//		return trace;
//	}

	// public boolean getValueAt(double T) {
	//
	// return
	// }

}
