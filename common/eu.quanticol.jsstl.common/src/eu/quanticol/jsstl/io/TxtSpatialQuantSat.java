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

public class TxtSpatialQuantSat implements SpatialQuantitativeSatisfactionWriter {

	@Override
	public void write(double[] quantSat, String path) throws FileNotFoundException {
		String textq = "";
		for (int i = 0; i < quantSat.length; i++) {
					textq += String.format(Locale.US, " %20.10f", quantSat[i]);
			}	
			PrintWriter printer = new PrintWriter(path);
			printer.print(textq);
			printer.close();

	}

}
