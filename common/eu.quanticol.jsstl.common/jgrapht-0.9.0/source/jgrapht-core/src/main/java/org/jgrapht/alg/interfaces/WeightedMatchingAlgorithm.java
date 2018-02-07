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
/* -------------------------
 * MinimumSpanningTree.java
 * -------------------------
 *
 * Original Author:  Alexey Kudinkin
 * Contributor(s):
 *
 */
package org.jgrapht.alg.interfaces;

/**
 * Allows to derive weighted matching from <i>general</i> graph
 *
 * @param <V>
 * @param <E>
 *
 * @see MatchingAlgorithm
 */
public interface WeightedMatchingAlgorithm<V, E>
    extends MatchingAlgorithm<V, E>
{
    

    /**
     * Returns weight of a matching found
     *
     * @return weight of a matching found
     */
    public double getMatchingWeight();
}

// End WeightedMatchingAlgorithm.java
