/*
 * Copyright (c) 2008 ARMINES
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript.simulation;

import org.objectweb.fractal.fscript.model.Node;

/**
 * Keeps a mapping from base nodes to their representatives as {@link DerivedNode}s
 * during a simulation.
 * 
 * @author Pierre-Charles David
 */
public interface DerivedNodeMapper {
    /**
     * Finds and returns the <code>DerivedNode</code> representing the specified base
     * node (i.e. a node from the model being simulated). During a given simulation
     * session, successive calls to this method with the same base node should always
     * return an identical <code>DerivedNode</code>.
     * 
     * @param node
     *            a base node.
     * @return the derived node associated to the given base node during the current
     *         simulation run.
     */
    DerivedNode getDerivedNodeFor(Node node);

    /**
     * Clears the mapping from base nodes to derived nodes from the current simulation
     * run. This method should be called at the end of every simulation run.
     */
    void clear();
}