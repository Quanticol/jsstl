package org.jsstl.core.formula;

import java.util.Map;

import org.jsstl.core.monitor.SpatialBooleanSignal;
import org.jsstl.core.monitor.SpatialBooleanSignalTransducer;
import org.jsstl.core.monitor.SpatialQuantitativeSignal;
import org.jsstl.core.monitor.SpatialQuantitativeSignalTransducer;
import org.jsstl.core.space.GraphModel;

public class OrFormula implements Formula {

	private Formula left;
	private Formula right;

	public OrFormula(Formula left, Formula right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return SpatialBooleanSignalTransducer.or(this.left.booleanCheck(parameters, g, s),
				this.right.booleanCheck(parameters, g, s));
	}

	@Override
	public SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return SpatialQuantitativeSignalTransducer.or(
				this.left.quantitativeCheck(parameters,g, s),
				this.right.quantitativeCheck(parameters,g, s));
	}

}
