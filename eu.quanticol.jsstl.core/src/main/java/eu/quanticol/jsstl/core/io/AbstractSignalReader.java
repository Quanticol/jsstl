/*******************************************************************************
 * jSSTL:  jSSTL : java Signal Spatio Temporal Logic
 * Copyright (C) 2018 
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package eu.quanticol.jsstl.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import eu.quanticol.jsstl.core.formula.Signal;
import eu.quanticol.jsstl.core.space.GraphModel;

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
