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

public class TxtSpatialBoolSat implements SpatialBooleanSatisfactionWriter {

	@Override
	public void write(double [] boolSat, String path) throws FileNotFoundException {
		String textb = "";
		for (int i = 0; i < boolSat.length; i++) {
					textb += String.format(Locale.US, " %20.10f", boolSat[i]);
			}	
			PrintWriter printer = new PrintWriter(path);
			printer.print(textb);
			printer.close();

	}

}
