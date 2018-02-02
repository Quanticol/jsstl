/**
 * 
 */
package org.jsstl.core.formula;

import java.util.Map;

/**
 * @author loreti
 *
 */
public interface ParametricExpression {

	public SignalExpression eval( Map<String, Double> parameters );
	
}
