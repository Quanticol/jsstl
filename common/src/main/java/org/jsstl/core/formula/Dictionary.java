/**
 * 
 */
package org.jsstl.core.formula;

/**
 * @author loreti
 *
 */
public class Dictionary {
	
	private String[] variables;
	private String[] order;
	private int[] dictionary;
	
	public Dictionary(String[] variables) {
		this(variables,variables);
	}
	
	public Dictionary(String[] variables , String[] order) {
		this.variables = variables;
		this.order = order;
		computeDictionary();
	}

	private void computeDictionary() {
		this.dictionary = new int[variables.length];
		for( int i=0 ; i<this.dictionary.length ; i++) {
			this.dictionary[i] = find( this.variables[i] );
		}
	}
	
	private int find( String v ) {
		for( int i=0 ; i<order.length ; i++ ) {
			if (v.equals(order[i])) {
				return i;
			}
		}
		throw new IllegalArgumentException("Unknown variable "+v);
	}	
	
	public int get(int idx) {
		return dictionary[idx];
	}

	public String[] getVariables() {
		return variables;
	}

	public void setOrder(String[] order) {
		this.order = order;
		computeDictionary();
	}
	
}
