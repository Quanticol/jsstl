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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import eu.quanticol.jsstl.formula.Signal;
import eu.quanticol.jsstl.space.GraphModel;

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
