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

import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.AttributesHelper;
import org.objectweb.fractal.fscript.model.AbstractAxis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.types.UnionType;

/**
 * Implements the <code>attribute</code> axis in FPath. This axis connects a component
 * to its attributes (if any), as defined by its <code>attribute-controller</code>
 * interface. This axis is not modifiable.
 * 
 * @author Pierre-Charles David
 */
public class AttributeAxis extends AbstractAxis {
    public AttributeAxis(FractalModel model) {
        super(model, "attribute", new UnionType(model.getNodeKind("component"), model
                .getNodeKind("interface")), model.getNodeKind("attribute"));
    }

    public boolean isPrimitive() {
        return true;
    }

    public boolean isModifiable() {
        return false;
    }

    public Set<Node> selectFrom(Node source) {
        Component comp = null;
        if (source instanceof ComponentNode) {
            comp = ((ComponentNode) source).getComponent();
        } else if (source instanceof InterfaceNode) {
            comp = ((InterfaceNode) source).getInterface().getFcItfOwner();
        } else {
            throw new IllegalArgumentException("Invalid source node kind " + source.getKind());
        }
        AttributesHelper attrHelper = new AttributesHelper(comp);
        Set<Node> result = new HashSet<Node>();
        for (String attrName : attrHelper.getAttributesNames()) {
            // Here we explicitly do not use the NodeFactory interface in order to reuse
            // the AttributeHelper, as these can be costly to create.
            Node node = new AttributeNode((FractalModel) model, attrHelper, attrName);
            result.add(node);
        }
        return result;
    }
}
