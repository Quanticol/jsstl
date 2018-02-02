/**
 * 
 */
package org.jsstl.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import org.jsstl.core.formula.Signal;
import org.jsstl.core.space.GraphModel;

/**
 * @author loreti
 *
 */
public interface SignalReader {

	public Signal read( GraphModel model, int variables , Reader reader ) throws IOException;

	public Signal read( GraphModel model, int variables , File reader ) throws IOException;

	public Signal read( GraphModel model, int variables , String fileName ) throws IOException;
}
