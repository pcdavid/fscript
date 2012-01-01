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
import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.model.AbstractAxis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.util.Fractal;

/**
 * Implements the <code>parent</code> axis in FPath, which connects a component node to
 * the nodes representing the component's direct parents (if any). This axis is not
 * modifiable directly (although changes to the <code>child</code> axis will reflect on
 * this one).
 * 
 * @author Pierre-Charles David
 */
public class ParentAxis extends AbstractAxis {
    public ParentAxis(FractalModel model) {
        super(model, "parent", "component", "component");
    }

    public boolean isPrimitive() {
        return true;
    }

    public boolean isModifiable() {
        return false;
    }

    public Set<Node> selectFrom(Node source) {
        Component comp = ((ComponentNode) source).getComponent();
        try {
            Component[] parents = Fractal.getSuperController(comp).getFcSuperComponents();
            Set<Node> result = new HashSet<Node>();
            NodeFactory nf = (NodeFactory) model;
            for (Component parent : parents) {
                result.add(nf.createComponentNode(parent));
            }
            return Collections.unmodifiableSet(result);
        } catch (NoSuchInterfaceException e) {
            return Collections.emptySet();
        }
    }
}
