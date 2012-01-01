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
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;
import org.objectweb.fractal.util.Fractal;

/**
 * Implements the <code>child</code>, <code>child-or-self</code>,
 * <code>descendant</code>, <code>descendant-or-self</code> axes.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class ChildAxis extends AbstractAxis<ComponentNodeImpl, ComponentNodeImpl> {
    /**
     * Possibles combinations:
     * <ul>
     * <li><code>includeSelf,  recurse</code>: <code>descendant-or-self</code></li>
     * <li><code>!includeSelf, recurse</code>:<code>descendant</code> </li>
     * <li><code>includeSelf,  !recurse</code>:<code>child-or-self</code> </li>
     * <li><code>!includeSelf, !recurse</code>:<code>child</code></li>
     */
    public ChildAxis(DefaultNodeFactory factory, boolean includeSelf, boolean recurse) {
        super(factory);
        this.includeSelf = includeSelf;
        this.recurse = recurse;
    }

    public String getName() {
        return (recurse ? "descendant" : "child") + (includeSelf ? "-or-self" : "");
    }

    public Set<ComponentNodeImpl> selectFrom(ComponentNodeImpl node) {
        Set<ComponentNodeImpl> nodes = createNodeSet();
        Component self = node.getComponent();
        ComponentNodeImpl selfNode = createComponentNode(self);
        if (includeSelf) {
            nodes.add(selfNode);
        }
        addChildren(nodes, self);
        return nodes;
    }

    private void addChildren(Set<ComponentNodeImpl> nodes, Component self) {
        try {
            ContentController cc = Fractal.getContentController(self);
            Component[] children = cc.getFcSubComponents();
            for (Component element : children) {
                ComponentNodeImpl kidNode = createComponentNode(element);
                nodes.add(kidNode);
                // TODO Decide which filtering semantics is the best:
                // 1. filter early, i.e. do not recurse on nodes which are
                // filtered out;
                // 2. filter late, i.e. recurse anyway, and filter children.
                // Current implementation is (2).
                // Choice (1) implies to move the condtional below inside
                // the one just above.
                if (recurse) {
                    addChildren(nodes, element);
                }
            }
        } catch (NoSuchInterfaceException e) {
            // self is not a composite,
        }
    }

    private boolean includeSelf;

    private boolean recurse;
}
