/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Michele Loreti
 *  	Laura Nenzi
 *  	Luca Bortolussi
 *******************************************************************************/
/* This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.generate;

import org.jgrapht.*;


public abstract class WeightedGraphGenerator<V, E>
    implements GraphGenerator<V, E, V>
{
    

    protected Class<? extends E> edgeClass;

    protected EdgeFactory<V, E> edgeFactory;

    protected double [][] weights;

    

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public WeightedGraphGenerator<V, E> edgeFactory(
        EdgeFactory<V, E> edgeFactory)
    {
        this.edgeFactory = edgeFactory;
        return this;
    }

    public WeightedGraphGenerator<V, E> edgeClass(Class<? extends E> edgeClass)
    {
        this.edgeClass = edgeClass;
        return this;
    }

    public WeightedGraphGenerator<V, E> weights(double [][] weights)
    {
        this.weights = weights;
        return this;
    }
}

// End WeightedGraphGenerator.java
