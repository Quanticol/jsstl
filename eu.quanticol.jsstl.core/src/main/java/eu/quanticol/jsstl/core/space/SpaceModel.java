/*******************************************************************************
 * jSSTL:  jSSTL : java Signal Spatio Temporal Logic
 * Copyright (C) 2018 
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package eu.quanticol.jsstl.core.space;

import java.util.Set;

public interface SpaceModel<L, E> {
	// TODO: Add the methods.

	/**
	 * This method returns the set of points in the model. The returned set can
	 * be modified.
	 * 
	 * @return the set of points in the model.
	 */
	public Set<L> getPoints();

	public Set<E> getEdges();

	public Set<L> getExternalBorder(Set<L> set);

	// /**
	// * Computes the closure of <code>set</code>.
	// * @param set a set
	// * @return the closure of <code>set</code>
	// */
	// public Set<S> closure( Set<S> set );
	//
	// /**
	// * Computes the external border of <code>set</code>.
	// *
	// * @param sets a set of points
	// *
	// * @return the closure of <code>set</code>.
	// */
	//
	// public Set<S> pre( S s );
	//
	// public Set<S> post( S s );
	//
	// public Set<S> pre( Set<S> set );
	//
	// public Set<S> post( Set<S> set );
	//
	// public Set<S> getSet( String name );
}
