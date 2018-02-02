package org.jsstl.io;

import java.io.FileNotFoundException;

import org.jsstl.core.monitor.SpatialQuantitativeSignal;

public interface SpatialQuantitativeSignalWriter {

	void write(SpatialQuantitativeSignal q, String path)
			throws FileNotFoundException;

}
