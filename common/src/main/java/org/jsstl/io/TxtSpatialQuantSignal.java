package org.jsstl.io;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;
import org.jsstl.core.space.Location;

import org.jsstl.core.monitor.SpatialQuantitativeSignal;

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
