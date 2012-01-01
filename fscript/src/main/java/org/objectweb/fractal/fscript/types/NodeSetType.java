/*
 * Copyright (c) 2007-2008 ARMINES
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
package org.objectweb.fractal.fscript.types;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import net.jcip.annotations.Immutable;

import org.objectweb.fractal.fscript.model.NodeKind;

/**
 * Represents the type of an homogeneous set of nodes.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class NodeSetType implements Type {
    public static final NodeSetType ANY_NODE_SET_TYPE = new NodeSetType(NodeKind.ANY_NODE_KIND);

    private final NodeKind elementsType;

    /**
     * Creates a new <code>NodeSetType</code> for a given node type.
     * 
     * @param elementsType
     *            the type of the nodes in the set.
     */
    public NodeSetType(NodeKind elementsType) {
        checkNotNull(elementsType);
        this.elementsType = elementsType;
    }

    public boolean checkValue(Object value) {
        if (!(value instanceof Set<?>)) {
            return false;
        }

        Set<?> nodes = (Set<?>) value;
        if (nodes.isEmpty()) {
            return true; // The empty (node-)set is compatible with all node-set types.
        } else {
            Object first = nodes.iterator().next();
            return elementsType.checkValue(first);
        }
    }

    @Override
    public String toString() {
        return "{" + elementsType.toString() + "}";
    }
}
