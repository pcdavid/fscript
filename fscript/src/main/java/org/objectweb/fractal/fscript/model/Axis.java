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
package org.objectweb.fractal.fscript.model;

import java.util.Set;

import org.objectweb.fractal.fscript.types.Type;
import org.objectweb.fractal.fscript.types.UnionType;

/**
 * An axis represents a type of relation between nodes (architectural elements).
 * 
 * @author Pierre-Charles David
 */
public interface Axis {
    /**
     * Returns the model this axis is part of.
     * 
     * @return the model this axis is part of.
     */
    Model getModel();

    /**
     * Returns the name of this axis, as used in FPath expressions (e.g.
     * <code>child</code> in <code>./child::*</code>.)
     * 
     * @return
     */
    String getName();

    /**
     * Checks whether this axis is primitive, i.e. not implemented in terms of another
     * axis.
     * 
     * @return <code>true</code> iff this axis is not implemented in terms of another
     *         axis.
     */
    boolean isPrimitive();

    /**
     * Checks whether this axis supports direct manipulation by the user, i.e. if actions
     * should be available in FScript programs to connect and disconnect arcs belonging to
     * this axis.
     * 
     * @return <code>true</code> iff this axis supports direct manipulation by the user.
     */
    boolean isModifiable();

    /**
     * Returns the type of the possible source nodes for this axis. This may be a simple
     * {@link NodeKind} or a {@link UnionType} of several node-kinds.
     * 
     * @return the type of the possible source nodes for this axis.
     */
    Type getInputNodeType();

    /**
     * Returns the type of the possible destination nodes for this axis. This must be a
     * simple {@link NodeKind}.
     * 
     * @return the type of the possible destination nodes for this axis.
     */
    Type getOutputNodeType();

    /**
     * Locates all the destination nodes the given source node is connected to through
     * this axis.
     * 
     * @param source
     *            the source node from which to select adjacent nodes.
     * @return all the destination nodes the given source node is connected to through
     *         this axis.
     */
    Set<Node> selectFrom(Node source);

    /**
     * Adds an arc in the underlying model connecting the given source and destination
     * nodes with this axis.
     * 
     * @param source
     *            the source node for the new arc to create.
     * @param dest
     *            the destination node for the new arc to create.
     * @throws UnsupportedOperationException
     *             if this axis does not support direct manipulation of its arcs.
     * @throws IllegalArgumentException
     *             if it is not possible to create the requested arc.
     */
    void connect(Node source, Node dest);

    /**
     * Removes the arc in the underlying model connecting the given source and destination
     * nodes with this axis.
     * 
     * @param source
     *            the source node of the arc to remove.
     * @param dest
     *            the destination node of the arc to remove.
     * @throws UnsupportedOperationException
     *             if this axis does not support direct manipulation of its arcs.
     * @throws IllegalArgumentException
     *             if it is not possible to remove the requested arc or if it does not
     *             exist.
     */
    void disconnect(Node source, Node dest);
}
