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
public interface Formula {

	SpatialBooleanSignal booleanCheck(Map<String,Double> parameters, GraphModel g, Signal s);

	SpatialQuantitativeSignal quantitativeCheck(Map<String,Double> parameters, GraphModel g, Signal s);

}
