/**
 * 
 */
package org.jsstl.io;

import java.io.FileNotFoundException;

/**
 * @author lauretta
 *
 */
public interface SpatialBooleanSatisfactionWriter {
	
	public void write( double [] boolSat, String path) throws FileNotFoundException;

}
