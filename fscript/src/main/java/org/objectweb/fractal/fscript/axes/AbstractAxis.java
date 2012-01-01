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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.fscript.nodes.AttributeNodeImpl;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;
import org.objectweb.fractal.fscript.nodes.InterfaceNodeImpl;
import org.objectweb.fractal.fscript.nodes.MethodNodeImpl;
import org.objectweb.fractal.fscript.nodes.Node;

public abstract class AbstractAxis<Source extends Node, Dest extends Node> implements
        Axis<Source, Dest> {
    private DefaultNodeFactory factory;

    protected AbstractAxis(DefaultNodeFactory factory) {
        this.factory = factory;
    }
    
    protected Set<Dest> createNodeSet() {
        return new HashSet<Dest>();
    }

    protected AttributeNodeImpl createAttributeNode(Component c, String attributeName) {
        return factory.createAttributeNode(c, attributeName);
    }

    protected ComponentNodeImpl createComponentNode(Component c) {
        return factory.createComponentNode(c);
    }

    protected InterfaceNodeImpl createInterfaceNode(Interface itf) {
        return factory.createInterfaceNode(itf);
    }

    protected MethodNodeImpl createMethodNode(Interface itf, Method meth) {
        return factory.createMethodNode(itf, meth);
    }

    @Override
    public String toString() {
        return "#<axis: " + getName() + ">";
    }
}
