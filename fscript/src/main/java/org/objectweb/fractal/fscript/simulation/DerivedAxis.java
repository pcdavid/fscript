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

import java.util.Collections;
import java.util.Set;

import org.objectweb.fractal.fscript.model.AbstractAxis;
import org.objectweb.fractal.fscript.model.Axis;
import org.objectweb.fractal.fscript.model.ModelListener;
import org.objectweb.fractal.fscript.model.Node;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;

/**
 * An axis which gets its (initial) set of arcs from a base axis, but stores all
 * modifications locally.
 * 
 * @author Pierre-Charles David
 */
public class DerivedAxis extends AbstractAxis {
    /**
     * The base axis from which to derive the initial set of arcs.
     */
    private final Axis baseAxis;

    /**
     * The (derived) arcs for this axis: the key is the source (derived) node, the value
     * is the set of destination (derived) nodes. Only <code>DerivedNode</code> are
     * stored in the multi-map. It is declared using simple <code>Node</code>s to be
     * compatible with the base <code>Axis</code> API.
     */
    private final HashMultimap<Node, Node> arcs = Multimaps.newHashMultimap();
    
    /**
     * The listener to notify of any change in the model connectivity.
     */
    private final ModelListener listener;

    public DerivedAxis(DerivedModel model, Axis baseAxis, ModelListener listener) {
        super(model, baseAxis.getName(), baseAxis.getInputNodeType(), baseAxis.getOutputNodeType());
        this.baseAxis = baseAxis;
        this.listener = listener;
    }

    public boolean isPrimitive() {
        return baseAxis.isPrimitive();
    }

    public boolean isModifiable() {
        return baseAxis.isModifiable();
    }

    @Override
    public void connect(Node source, Node dest) {
        fetch((DerivedNode) source);
        arcs.put(source, dest);
        if (listener != null) {
            listener.connectionAdded(source, dest, this);
        }
    }

    @Override
    public void disconnect(Node source, Node dest) {
        fetch((DerivedNode) source);
        boolean removed = arcs.remove(source, dest);
        if (!removed) {
            throw new IllegalArgumentException("Trying to remove a non-existent arc.");
        }
        if (listener != null) {
            listener.connectionRemoved(source, dest, this);
        }
    }

    public Set<Node> selectFrom(Node source) {
        fetch((DerivedNode) source);
        return Collections.unmodifiableSet(arcs.get(source));
    }

    private void fetch(DerivedNode source) {
        if (arcs.containsKey(source)) {
            return;
        }
        Set<Node> baseNodes = baseAxis.selectFrom(source.getBaseNode());
        DerivedNodeMapper mapper = (DerivedNodeMapper) model;
        for (Node baseDest : baseNodes) {
            DerivedNode dest = mapper.getDerivedNodeFor(baseDest);
            arcs.put(source, dest);
        }
    }
}
