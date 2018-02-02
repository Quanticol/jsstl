package org.jsstl.io;

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
