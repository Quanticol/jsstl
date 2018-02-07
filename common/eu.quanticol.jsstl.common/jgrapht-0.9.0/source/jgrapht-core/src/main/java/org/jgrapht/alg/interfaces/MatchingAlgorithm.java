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
 * MatchingAlgorithm.java
 * -------------------------
 *
 * Original Author:  Alexey Kudinkin
 * Contributor(s):
 *
 */
package org.jgrapht.alg.interfaces;

import java.util.*;


/**
 * Allows to derive <a
 * href="http://en.wikipedia.org/wiki/Matching_(graph_theory)">matching</a> from
 * given graph
 *
 * @param <V> vertex concept type
 * @param <E> edge concept type
 */
public abstract interface MatchingAlgorithm<V, E>
{
    

    /**
     * Returns set of edges making up the matching
     */
    public Set<E> getMatching();
}

// End MatchingAlgorithm.java
