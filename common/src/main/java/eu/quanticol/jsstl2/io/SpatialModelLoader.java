/**
 * 
 */
package eu.quanticol.jsstl2.io;

import java.io.IOException;
import java.io.InputStream;

import eu.quanticol.jsstl2.SpatialModel;

/**
 * @author loreti
 *
 */
public interface SpatialModelLoader {
	
	public SpatialModel load( InputStream is ) throws IOException;

}
