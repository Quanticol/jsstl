/**
 * 
 */
package eu.quanticol.jsstl2;

/**
 * @author loreti
 *
 */
public interface SignalIterator<T> {
	
	public boolean hasNext();
	
	public double next();
	
	public T next( double t );
	
	public void jump( double t );

}
