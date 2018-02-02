package org.jsstl.core.formula;

import java.util.Map;

import org.jsstl.core.monitor.SpatialBooleanSignal;
import org.jsstl.core.monitor.SpatialBooleanSignalTransducer;
import org.jsstl.core.monitor.SpatialQuantitativeSignal;
import org.jsstl.core.monitor.SpatialQuantitativeSignalTransducer;
import org.jsstl.core.space.GraphModel;

public class NotFormula implements Formula {

	private Formula son;

	public NotFormula(Formula son) {
		super();
		this.son = son;
	}

	@Override
	public SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return SpatialBooleanSignalTransducer.not(this.son.booleanCheck(parameters, g, s));
	}

	@Override
	public SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return SpatialQuantitativeSignalTransducer.not(this.son
				.quantitativeCheck(parameters,g, s));
	}

}
