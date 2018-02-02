/**
 * 
 */
package org.jsstl.io;

import java.io.FileNotFoundException;

/**
 * @author lauretta
 *
 */
public interface SpatialQuantitativeSatisfactionWriter {
	
	public void write( double [] quantSat, String path) throws FileNotFoundException;

}
