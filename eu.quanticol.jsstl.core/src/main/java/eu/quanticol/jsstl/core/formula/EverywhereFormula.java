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
package eu.quanticol.jsstl.core.formula;

import java.util.Map;

import eu.quanticol.jsstl.core.formula.ParametricInterval;
import eu.quanticol.jsstl.core.monitor.SpatialBooleanSignal;
import eu.quanticol.jsstl.core.monitor.SpatialBooleanSignalTransducer;
import eu.quanticol.jsstl.core.monitor.SpatialQuantitativeSignal;
import eu.quanticol.jsstl.core.monitor.SpatialQuantitativeSignalTransducer;
import eu.quanticol.jsstl.core.space.GraphModel;

public class EverywhereFormula implements Formula {

	private ParametricInterval interval;
	private Formula son;

	public EverywhereFormula(ParametricInterval interval, Formula son) {
		super();
		this.interval = interval;
		this.son = son;
	}

	@Override
	public SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return SpatialBooleanSignalTransducer.everywhere(
				this.son.booleanCheck(parameters, g, s), this.interval.getLower(parameters),
				this.interval.getUpper(parameters));
	}

	@Override
	public SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return SpatialQuantitativeSignalTransducer.everywhere(
				this.son.quantitativeCheck(parameters,g, s), this.interval.getLower(parameters),
				this.interval.getUpper(parameters));
	}

}
