/**
 * 
 */
package org.jsstl.core.formula;

import java.util.Map;

import org.jsstl.core.monitor.SpatialBooleanSignal;
import org.jsstl.core.monitor.SpatialQuantitativeSignal;
import org.jsstl.core.space.GraphModel;

/**
 * @author loreti
 *
 */
public class ReferencedFormula implements Formula {
	
	private jSSTLScript script;
	
	private String name;
	
	public ReferencedFormula( jSSTLScript script , String name ) {
		this.script = script;
		this.name = name;
	}
	

	/* (non-Javadoc)
	 * @see org.jsstl.core.formula.Formula#booleanCheck(org.jsstl.core.space.GraphModel, org.jsstl.core.formula.Signal)
	 */
	@Override
	public SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return script.booleanCheck( parameters , name , g , s );
	}

	/* (non-Javadoc)
	 * @see org.jsstl.core.formula.Formula#quantitativeCheck(org.jsstl.core.space.GraphModel, org.jsstl.core.formula.Signal)
	 */
	@Override
	public SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, GraphModel g, Signal s) {
		return script.quantitativeCheck(parameters,name,g,s);
	}

}
