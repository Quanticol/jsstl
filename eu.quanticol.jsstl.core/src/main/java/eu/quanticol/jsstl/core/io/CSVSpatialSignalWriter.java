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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import eu.quanticol.jsstl.core.monitor.SpatialBooleanSignal;
import eu.quanticol.jsstl.core.monitor.SpatialQuantitativeSignal;
import eu.quanticol.jsstl.core.signal.BooleanSignal;
import eu.quanticol.jsstl.core.signal.QuantitativeSignal;
import eu.quanticol.jsstl.core.signal.TimeInterval;
import eu.quanticol.jsstl.core.space.Location;

/**
 * @author loreti
 *
 */
public class CSVSpatialSignalWriter {
	
	protected void write( Writer out , BooleanSignal signal ) {
		PrintWriter pw = new PrintWriter(out);
		for (TimeInterval t : signal.signal) {
			pw.println(t.getLower()+";"+(t.getValue()?1.0:0.0));
			pw.println(t.getUpper()+";"+(t.getValue()?1.0:0.0));
		}
		pw.close();		
	}
	
	protected void write( Writer out , QuantitativeSignal signal ) {
		PrintWriter pw = new PrintWriter(out);
		double time = signal.getInitialTime();
		for (Double v : signal.getSignal()) {
			pw.println(time+";"+v);
			time = time+signal.getDeltaT();
		}
		pw.close();		
	}
	
	public void writeSatisfaction( String dir , String pref , SpatialBooleanSignal signal ) throws FileNotFoundException {
		writeSatisfaction( new File(dir) , pref+"_qualitative.csv" , signal );
	}
	
	public void writeSatisfaction(File parent, String name , SpatialBooleanSignal signal) throws FileNotFoundException {
		writeSatisfaction(parent,name,signal.graph.getLocations(),signal.boolSat());
	}

	private void writeSatisfaction(File parent, String name, ArrayList<Location> locations, double[] data) throws FileNotFoundException {
		if (parent.isDirectory()) {
			PrintWriter pw = new PrintWriter( new File( parent , name ) );
			for(int i=0 ; i<locations.size();i++) {
				pw.println(locations.get(i).getLabel()+";"+data[i]);
			}
			pw.flush();
			pw.close();
		}
	}

	public void writeSatisfaction( String dir , String pref , SpatialQuantitativeSignal signal ) throws FileNotFoundException {
		writeSatisfaction( new File(dir) , pref+"_quantitative.csv" , signal );
	}
	
	public void writeSatisfaction(File parent, String name , SpatialQuantitativeSignal signal) throws FileNotFoundException {
		writeSatisfaction(parent, name, signal.graph.getLocations(), signal.quantSat());
	}

	public void write( String dir , String pref , SpatialBooleanSignal signal ) throws FileNotFoundException {
		File parent = new File( dir );
		write( parent , pref , signal );
	}
	
	public void write( String dir , String pref , SpatialBooleanSignal signal , Location l ) throws FileNotFoundException {
		File parent = new File( dir );
		write( parent , pref , signal , l );
	}
	
	public void write(File parent, String pref, SpatialBooleanSignal signal) throws FileNotFoundException {
		if (parent.isDirectory()) {
			for (Entry<Location, BooleanSignal> e: signal.entrySets()) {
				PrintWriter pw = new PrintWriter(new File(parent, pref+"_"+e.getKey().getLabel()+".csv"));
				write( pw , e.getValue() );
			}
		}
	}

	public void write(File parent, String pref, SpatialBooleanSignal signal , Location l ) throws FileNotFoundException {
		if (parent.isDirectory()) {
			BooleanSignal s = signal.getSignal( l );
			if (s != null) {
				PrintWriter pw = new PrintWriter(new File(parent, pref+"_"+l.getLabel()+".csv"));
				write( pw , s );
			}
		}
	}

	public void write( String dir , String pref , SpatialQuantitativeSignal signal ) throws FileNotFoundException {
		File parent = new File( dir );
		write( parent , pref , signal );
	}

	public void write( String dir , String pref , SpatialQuantitativeSignal signal , Location l ) throws FileNotFoundException {
		File parent = new File( dir );
		write( parent , pref , signal , l );
	}

	public void write(File parent, String pref, SpatialQuantitativeSignal signal) throws FileNotFoundException {
		if (parent.isDirectory()) {
			for (Entry<Location, QuantitativeSignal> e: signal.entrySets()) {
				PrintWriter pw = new PrintWriter(new File(parent, pref+"_"+e.getKey().getLabel()+".csv"));
				write( pw , e.getValue() );
			}
		}
	}
	
	public void write(File parent, String pref, SpatialQuantitativeSignal signal, Location l ) throws FileNotFoundException {
		if (parent.isDirectory()) {
			QuantitativeSignal s = signal.getSignal( l );
			if (s != null) {
				PrintWriter pw = new PrintWriter(new File(parent, pref+"_"+l.getLabel()+".csv"));
				write( pw , s );
			}
		}
	}
}
