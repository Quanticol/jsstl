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
package eu.quanticol.jsstl.io;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

import eu.quanticol.jsstl.monitor.SpatialQuantitativeSignal;
import eu.quanticol.jsstl.space.Location;

public class TxtSpatialQuantSignal implements SpatialQuantitativeSignalWriter {
	@Override
public void write(SpatialQuantitativeSignal q, String path) throws FileNotFoundException {
		//int maxRuns = q.get().getPoints();
		int minRuns = q.minrun();
		String text = "";
		for (Location l: q.graph.getLocations()) {
			for (int j = 0; j < minRuns; j++) {
					text += String.format(Locale.US, " %20.10f", q.spatialQuantitativeSignal.get(l).getValueAt(j));
			}
			text += "\n";
		}
		PrintWriter printer = new PrintWriter(path);
		printer.print(text);
		printer.close();

	}
}
