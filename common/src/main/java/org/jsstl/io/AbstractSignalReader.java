/**
 * 
 */
package org.jsstl.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jsstl.core.formula.Signal;
import org.jsstl.core.space.GraphModel;

/**
 * @author loreti
 *
 */
public abstract class AbstractSignalReader implements SignalReader {

	@Override
	public Signal read(GraphModel model, int variables , File file) throws IOException {
		return read( model, variables ,  new FileReader(file));
	}

	@Override
	public Signal read(GraphModel model, int variables , String fileName) throws IOException {
		return read( model, variables, new File(fileName) );
	}

}
