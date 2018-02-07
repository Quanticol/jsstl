/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Michele Loreti
 *  	Laura Nenzi
 *  	Luca Bortolussi
 *******************************************************************************/
/**
 * 
 */
package eu.quanticol.jsstl.io;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import eu.quanticol.jsstl.space.GraphModel;


/**
 * @author loreti
 *
 */
public interface GraphModelReader {

	public GraphModel read( Reader reader ) throws IOException, SyntaxErrorExpection ;
	
	public GraphModel read( File file ) throws IOException, SyntaxErrorExpection ;
	
	public GraphModel read( String filename ) throws IOException, SyntaxErrorExpection ;
	
}
