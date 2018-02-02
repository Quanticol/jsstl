package org.jsstl.io;

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
