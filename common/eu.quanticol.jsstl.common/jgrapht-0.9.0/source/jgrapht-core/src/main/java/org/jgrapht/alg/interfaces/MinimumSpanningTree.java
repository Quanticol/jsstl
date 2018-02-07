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

import java.util.*;


/**
 * Allows to derive <a href=http://en.wikipedia.org/wiki/Minimum_spanning_tree>
 * minimum spanning tree</a> from given undirected connected graph. In the case
 * of disconnected graphs it would rather derive minimum spanning <i>forest<i/>
 *
 * @param <V> vertex concept type
 * @param <E> edge concept type
 */
public interface MinimumSpanningTree<V, E>
{
    

    /**
     * Returns edges set constituting the minimum spanning tree/forest
     *
     * @return minimum spanning-tree edges set
     */
    public Set<E> getMinimumSpanningTreeEdgeSet();

    /**
     * Returns total weight of the minimum spanning tree/forest.
     *
     * @return minimum spanning-tree total weight
     */
    public double getMinimumSpanningTreeTotalWeight();
}

// End MinimumSpanningTree.java
