package org.jsstl.core.formula;

import java.util.Map;

import org.jsstl.core.formula.ParametricInterval;
import org.jsstl.core.monitor.SpatialBooleanSignal;
import org.jsstl.core.monitor.SpatialBooleanSignalTransducer;
import org.jsstl.core.monitor.SpatialQuantitativeSignal;
import org.jsstl.core.monitor.SpatialQuantitativeSignalTransducer;
import org.jsstl.core.space.GraphModel;

public class UntilFormula implements Formula {

	private ParametricInterval interval;
	private Formula left;
	private Formula right;

	public UntilFormula(ParametricInterval interval, Formula left, Formula right) {
		super();
		this.interval = interval;
		this.left = left;
		this.right = right;
	}

	@Override
	public SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return SpatialBooleanSignalTransducer.until(
				this.left.booleanCheck(parameters, g, s), this.right.booleanCheck(null, g, s),
				this.interval.getLower(parameters), this.interval.getUpper(parameters));
	}

	@Override
	public SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return SpatialQuantitativeSignalTransducer.until(
				this.left.quantitativeCheck(parameters,g, s),
				this.right.quantitativeCheck(parameters,g, s), this.interval.getLower(parameters),
				this.interval.getUpper(parameters));

	}

}
