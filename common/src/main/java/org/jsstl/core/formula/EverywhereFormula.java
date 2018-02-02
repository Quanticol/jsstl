package org.jsstl.core.formula;

import java.util.Map;

import org.jsstl.core.formula.ParametricInterval;
import org.jsstl.core.monitor.SpatialBooleanSignal;
import org.jsstl.core.monitor.SpatialBooleanSignalTransducer;
import org.jsstl.core.monitor.SpatialQuantitativeSignal;
import org.jsstl.core.monitor.SpatialQuantitativeSignalTransducer;
import org.jsstl.core.space.GraphModel;

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
