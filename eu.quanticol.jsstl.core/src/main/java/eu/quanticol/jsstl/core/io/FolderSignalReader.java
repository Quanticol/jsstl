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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import eu.quanticol.jsstl.core.formula.Signal;
import eu.quanticol.jsstl.core.space.GraphModel;

/**
 * @author loreti
 *
 */
public class FolderSignalReader {
 
	private final static String[] VALID_EXTENSIONS = new String[] { "" , ".dat" , ".txt" };
	
	private final static String TIME_FILE_NAME = "time";
	
	private String fileExtension = ".dat";
	private final GraphModel model;
	private final String[] variables;

	public FolderSignalReader( GraphModel model , String[] variables ) {
		this.model = model;
		this.variables = variables;
	}
	
	public Signal read( String folder ) throws IOException {
		return read( new File(folder) );
	}

	public Signal read(File file) throws IOException {
		if (file.isDirectory()) {
			return doReadSignalFrom( file );
		}
		throw new IllegalArgumentException();
	}
	
	private String checkFileExtension( File folder ) throws IOException {
		for (String ext : VALID_EXTENSIONS) {
			if (exists(folder,FolderSignalReader.TIME_FILE_NAME+ext)) {
				return ext;
			}
		}
		throw new IOException("Illegal folder structure!");
	}
	
	private boolean exists( File folder , String file ) {
		File f = new File(folder, file);
		return f.exists();
	}

	private Signal doReadSignalFrom(File folder) throws IOException {
		fileExtension = checkFileExtension(folder);
		double[] time = readTimeFrom( folder , "time"+ fileExtension );
		double[][][] data = new double[model.getNumberOfLocations()][time.length][variables.length];

		for( int i=0 ; i<model.getNumberOfLocations() ; i++ ) {
			for( int j=0 ; j<variables.length ; j++ ) {
				double[] data_i_j = readTimeFrom( folder , "values_"+model.getLocation(i).getLabel()+"_"+variables[j]+fileExtension);
				if (data_i_j.length != time.length) {
					throw new IllegalArgumentException();
				}
				fill( data , data_i_j , i , j );
			}
		}

		return new Signal(model, time, data);
	}

	private void fill(double[][][] data, double[] data_i_j, int l, int v) {
		for( int i=0 ; i<data_i_j.length ; i++ ) {
			data[l][i][v] = data_i_j[i];
		}
	}

	private double[] readTimeFrom(File parent , String file ) throws IOException {
		FileReader source = new FileReader(new File( parent ,  file ) );
		return dataLines(source);
	}
	

	private double[] dataLines(Reader source) throws IOException {
		ArrayList<String> lines = new ArrayList<>();
		BufferedReader br = new BufferedReader(source);
		String data = br.readLine();
		while (data != null) {
			lines.add(data);
			data = br.readLine();
		}
		br.close();
		double[] dataArray = new double[lines.size()];
		for (int i=0 ; i<dataArray.length ; i++) {
			dataArray[i] = Double.parseDouble(lines.get(i));
		}
		return dataArray;
	}

}
