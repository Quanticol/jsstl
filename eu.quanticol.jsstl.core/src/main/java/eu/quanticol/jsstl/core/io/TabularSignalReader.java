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
package eu.quanticol.jsstl.core.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import eu.quanticol.jsstl.core.formula.Signal;
import eu.quanticol.jsstl.core.space.GraphModel;

/**
 * @author loreti
 *
 */
public class TabularSignalReader extends AbstractSignalReader {
	
	//TODO: Add checking of errors!!!!

	public static final int VARIABLES_OF_LOCATIONS = 0;
	public static final int LOCATION_OF_VARIABLES = 1;

	private int header_size = 0;
	private int arrangment = VARIABLES_OF_LOCATIONS;

	public void setHeaderSize(int header_size) {
		if (header_size < 0) {
			throw new IllegalArgumentException();
		}
		this.header_size = header_size;
	}
	
	public void setArrangmentByLocations() {
		this.arrangment = VARIABLES_OF_LOCATIONS;
	}
	
	public void setArragnmentByVariables() {
		this.arrangment = LOCATION_OF_VARIABLES;
	}

	@Override
	public Signal read(GraphModel model, int variables, Reader reader)
			throws IOException {
		ArrayList<String[]> listString = dataLines(reader);
		double[] time = collectTimeArray(listString);
		double[][][] varData;
		varData = collectVariableArray(model, variables, listString);
		return new Signal(model, time, varData);
	}

	private double[] collectTimeArray(ArrayList<String[]> data) {
		double[] timeArray = new double[data.size()];
		for (int i = 0; i < data.size(); i++) {
			timeArray[i] = Double.parseDouble(data.get(i)[0]);
		}
		return timeArray;
	}

	private double[][][] collectVariableArray(GraphModel model,
			int variables, ArrayList<String[]> data) {
		double[][][] varData = new double[model.getNumberOfLocations()][data.size()][variables];
		for (int t = 0; t < data.size(); t++) {
			String[] dataRow = data.get(t);
			if (dataRow.length != (variables*model.getNumberOfLocations()+1)) {
				throw new IllegalArgumentException();//TODO: Change this exception in order to
				//provide better explanation about this error.
			}
			for (int k = 0; k < variables; k++) {
				for (int l = 0; l < model.getNumberOfLocations(); l++) {
					if (arrangment == VARIABLES_OF_LOCATIONS) {
						varData[l][t][k] = Double
								.parseDouble(dataRow[1+k*model.getNumberOfLocations()+l]);
					} else {
						varData[l][t][k] = Double
								.parseDouble(dataRow[1+l*variables+k]);
					}
				}
			}
		}
		return varData;
	}

//	private double[][][] collectVariableArrayLV(GraphModel model,
//			int variables, ArrayList<String[]> data) {
//		double[][][] varData = new double[model.getNumberOfLocations()][data
//				.size()][variables];
//		for (int t = 0; t < data.size(); t++) {
//			int countVar = 1;
//			String[] dataRow = data.get(t);
//			if (dataRow.length != (variables*model.getNumberOfLocations()+1)) {
//				throw new IllegalArgumentException();//TODO: Change this exception in order to
//				//provide better explanation about this error.
//			}
//			for (int l = 0; l < model.getNumberOfLocations(); l++) {
//				for (int k = 0; k < variables; k++) {
//					varData[l][t][k] = Double
//							.parseDouble(dataRow[1+l*variables+k]);
//					countVar = countVar + 1;
//				}
//			}
//
//		}
//
//		return varData;
//	}

	private ArrayList<String[]> dataLines(Reader source) throws IOException {
		ArrayList<String[]> toReturn = new ArrayList<>();
		BufferedReader br = new BufferedReader(source);
		String data = br.readLine();
		int counter = 0;
		while (data != null) {
			if (counter < header_size) {
				counter++;
			} else {
				toReturn.add(data.split(" "));
			}
			data = br.readLine();
		}
		br.close();
		return toReturn;
	}
}
