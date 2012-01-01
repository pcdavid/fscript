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

import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;

/**
 * Selects all the siblings of a component, i.e. all the components which share at least
 * one parent with the initial one.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class SiblingAxis extends AbstractAxis<ComponentNodeImpl, ComponentNodeImpl> {
    public SiblingAxis(DefaultNodeFactory factory, boolean includeSelf) {
        super(factory);
        this.includeSelf = includeSelf;
        childAxis = new ChildAxis(factory, false, false);
        parentAxis = new ParentAxis(factory, false, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.objectweb.fractal.fscript.axes.Axis#getName()
     */
    public String getName() {
        return "sibling" + (includeSelf ? "-or-self" : "");
    }

    public Set<ComponentNodeImpl> selectFrom(ComponentNodeImpl node) {
        // TODO Decide which filtering semantics to adopt.
        // See ChildAxis note for details. In this class,
        // the choice is either to pass the predicate as is
        // to parent and child axis, or to use a TRUE predicate
        // in these cases and use pred only on the final candidates.
        Set<ComponentNodeImpl> nodes = createNodeSet();
        for (ComponentNodeImpl parent : parentAxis.selectFrom(node)) {
            for (ComponentNodeImpl kid : childAxis.selectFrom(parent)) {
                if (!kid.equals(node)) {
                    nodes.add(kid);
                }
            }
        }
        if (includeSelf) {
            nodes.add(node);
        }
        return nodes;
    }

    private boolean includeSelf;

    private ChildAxis childAxis;

    private ParentAxis parentAxis;
}
