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
package org.objectweb.fractal.fscript.model.fractal;

import java.util.Collections;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.model.AbstractAxis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.NodeKind;
import org.objectweb.fractal.fscript.types.UnionType;

/**
 * Implements the <code>component</code> axis which connects interface and attribute
 * nodes to their owner component. This axis is not modifiable.
 * 
 * @author Pierre-Charles David
 */
public class ComponentAxis extends AbstractAxis {
    public ComponentAxis(FractalModel model) {
        super(model, "component", new UnionType(model.getNodeKinds().toArray(new NodeKind[0])),
                model.getNodeKind("component"));
    }

    public boolean isPrimitive() {
        return true;
    }

    public boolean isModifiable() {
        return false;
    }

    public Set<Node> selectFrom(Node source) {
        Component dest = null;
        if (source instanceof ComponentNode) {
            dest = ((ComponentNode) source).getComponent();
        } else if (source instanceof AttributeNode) {
            dest = ((AttributeNode) source).getOwner();
        } else if (source instanceof InterfaceNode) {
            dest = ((InterfaceNode) source).getInterface().getFcItfOwner();
        } else {
            throw new IllegalArgumentException("Invalid node kind.");
        }
        NodeFactory nf = (NodeFactory) model;
        return Collections.singleton((Node) nf.createComponentNode(dest));
    }
}
