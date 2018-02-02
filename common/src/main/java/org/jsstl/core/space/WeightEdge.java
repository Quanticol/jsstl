package org.jsstl.core.space;

public class WeightEdge {
	public Location lStart; //
	public Location lEnd; //
	public double weight; //

	// -------------------------------------------------------------
	public WeightEdge(Location start, Location end, double w) // constructor
	{
		lStart = start;
		lEnd = end;
		weight = w;
	}
	// -------------------------------------------------------------

}
