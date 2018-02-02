/**
 * 
 */
package org.jsstl.core.formula;

import java.util.Map;

import org.jsstl.core.monitor.SpatialBooleanSignal;
import org.jsstl.core.monitor.SpatialQuantitativeSignal;
import org.jsstl.core.space.GraphModel;

/**
 * @author lauretta
 *
 */
public class AtomicFormula implements Formula {

	private ParametricExpression expression;
	private boolean isStrictInequality;

	public AtomicFormula(ParametricExpression expression, boolean isStrictInequality) {
		super();
		this.expression = expression;
		this.isStrictInequality = isStrictInequality;
	}

	@Override
	public SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return s.getSpatialBooleanSignal(expression.eval(parameters), isStrictInequality);
	}

	@Override
	public SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return s.getSpatialQuantitativeSignal(expression.eval(parameters), isStrictInequality);

	}

}
