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
