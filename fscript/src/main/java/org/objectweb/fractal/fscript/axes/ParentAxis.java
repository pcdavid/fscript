/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2007 ARMINES
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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.axes;

import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.SuperController;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;
import org.objectweb.fractal.util.Fractal;

/**
 * Implements the <code>parent</code>, <code>parent-or-self</code>,
 * <code>ancestor</code> and <code>ancestor-or-self</code> axes.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class ParentAxis extends AbstractAxis<ComponentNodeImpl, ComponentNodeImpl> {
    /**
     * Possibles combinations:
     * <ul>
     * <li><code>includeSelf,  recurse</code>:<code>ancestor-or-self</code> </li>
     * <li><code>!includeSelf, recurse</code>:<code>ancestor</code></li>
     * <li><code>includeSelf,  !recurse</code>:<code>parent-or-self</code> </li>
     * <li><code>!includeSelf, !recurse</code>:<code>parent</code></li>
     */
    public ParentAxis(DefaultNodeFactory factory, boolean includeSelf, boolean recurse) {
        super(factory);
        this.includeSelf = includeSelf;
        this.recurse = recurse;
    }

    public String getName() {
        return (recurse ? "ancestor" : "parent") + (includeSelf ? "-or-self" : "");
    }

    public Set<ComponentNodeImpl> selectFrom(ComponentNodeImpl node) {
        Set<ComponentNodeImpl> nodes = createNodeSet();
        Component self = node.getComponent();
        if (includeSelf) {
            nodes.add(createComponentNode(self));
        }
        addParents(nodes, self);
        return nodes;
    }

    private void addParents(Set<ComponentNodeImpl> nodes, Component self) {
        try {
            SuperController sc = Fractal.getSuperController(self);
            for (Component element : sc.getFcSuperComponents()) {
                nodes.add(createComponentNode(element));
                // TODO Decide which filtering semantics to adopt.
                // See ChildAxis comments for details.
                if (recurse) {
                    addParents(nodes, element);
                }
            }
        } catch (NoSuchInterfaceException e) {
            // self is not a sub-component
        }
    }

    private boolean includeSelf;

    private boolean recurse;
}
