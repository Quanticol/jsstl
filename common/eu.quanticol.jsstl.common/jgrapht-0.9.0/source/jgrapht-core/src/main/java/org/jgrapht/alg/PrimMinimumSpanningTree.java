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
 * PrimMinimumSpanningTree.java
 * -------------------------
 *
 * Original Author:  Alexey Kudinkin
 * Contributor(s):
 *
 */
package org.jgrapht.alg;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;


/**
 * An implementation of <a href="http://en.wikipedia.org/wiki/Prim's_algorithm">
 * Prim's algorithm</a> that finds a minimum spanning tree/forest subject to
 * connectivity of the supplied weighted undirected graph. The algorithm was
 * developed by Czech mathematician V. Jarník and later independently by
 * computer scientist Robert C. Prim and rediscovered by E. Dijkstra.
 *
 * @author Alexey Kudinkin
 * @since Mar 5, 2013
 */
public class PrimMinimumSpanningTree<V, E>
    implements MinimumSpanningTree<V, E>
{
    

    /**
     * Minimum Spanning-Tree/Forest edge set
     */
    private final Set<E> minimumSpanningTreeEdgeSet;

    /**
     * Minimum Spanning-Tree/Forest edge set overall weight
     */
    private final double minimumSpanningTreeTotalWeight;

    

    public PrimMinimumSpanningTree(final Graph<V, E> g)
    {
        this.minimumSpanningTreeEdgeSet = new HashSet<E>(g.vertexSet().size());

        Set<V> unspanned = new HashSet<V>(g.vertexSet());

        while (!unspanned.isEmpty()) {
            Iterator<V> ri = unspanned.iterator();

            V root = ri.next();

            ri.remove();

            // Edges crossing the cut C = (S, V \ S), where S is set of
            // already spanned vertices

            PriorityQueue<E> dangling =
                new PriorityQueue<E>(
                    g.edgeSet().size(),
                    new Comparator<E>() {
                        @Override public int compare(E lop, E rop)
                        {
                            return Double.valueOf(g.getEdgeWeight(lop))
                                .compareTo(g.getEdgeWeight(rop));
                        }
                    });

            dangling.addAll(g.edgesOf(root));

            for (E next; (next = dangling.poll()) != null;) {
                V s,
                    t =
                        unspanned.contains(s = g.getEdgeSource(next)) ? s
                        : g.getEdgeTarget(next);

                // Decayed edges aren't removed from priority-queue so that
                // having them just ignored being encountered through min-max
                // traversal
                if (!unspanned.contains(t)) {
                    continue;
                }

                this.minimumSpanningTreeEdgeSet.add(next);

                unspanned.remove(t);

                for (E e : g.edgesOf(t)) {
                    if (unspanned.contains(
                            g.getEdgeSource(e).equals(t) ? g.getEdgeTarget(
                                e)
                            : g.getEdgeSource(e)))
                    {
                        dangling.add(e);
                    }
                }
            }
        }

        double spanningTreeWeight = 0;
        for (E e : minimumSpanningTreeEdgeSet) {
            spanningTreeWeight += g.getEdgeWeight(e);
        }

        this.minimumSpanningTreeTotalWeight = spanningTreeWeight;
    }

    

    @Override public Set<E> getMinimumSpanningTreeEdgeSet()
    {
        return Collections.unmodifiableSet(minimumSpanningTreeEdgeSet);
    }

    @Override public double getMinimumSpanningTreeTotalWeight()
    {
        return minimumSpanningTreeTotalWeight;
    }
}

// End PrimMinimumSpanningTree.java
