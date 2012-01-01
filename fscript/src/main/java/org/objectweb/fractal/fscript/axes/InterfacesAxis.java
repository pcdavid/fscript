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
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;
import org.objectweb.fractal.fscript.nodes.InterfaceNodeImpl;

/**
 * Selects all the external interfaces of a component.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class InterfacesAxis extends AbstractAxis<ComponentNodeImpl, InterfaceNodeImpl> {
    public InterfacesAxis(DefaultNodeFactory factory) {
        super(factory);
    }

    public String getName() {
        return "interface";
    }

    public Set<InterfaceNodeImpl> selectFrom(ComponentNodeImpl node) {
        Component component = node.getComponent();
        Set<InterfaceNodeImpl> nodes = createNodeSet();
        for (Object itf : component.getFcInterfaces()) {
            nodes.add(createInterfaceNode((Interface) itf));
        }
        return nodes;
    }
}
