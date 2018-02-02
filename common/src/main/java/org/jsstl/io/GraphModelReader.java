/**
 * 
 */
package org.jsstl.io;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.jsstl.core.space.GraphModel;


/**
 * @author loreti
 *
 */
public interface GraphModelReader {

	public GraphModel read( Reader reader ) throws IOException, SyntaxErrorExpection ;
	
	public GraphModel read( File file ) throws IOException, SyntaxErrorExpection ;
	
	public GraphModel read( String filename ) throws IOException, SyntaxErrorExpection ;
	
}
