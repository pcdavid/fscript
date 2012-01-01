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
package org.objectweb.fractal.fscript.nodes;

import java.lang.reflect.Method;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;

/**
 * This interface is used to wrap Fractal-level objects intro {@link Node}s to enable a
 * more uniform treatment in FPath.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 * @see DefaultNodeFactory
 */
public interface NodeFactory {
    /**
     * Wraps a component into a <code>ComponentNode</code>.
     * 
     * @param c
     *            a Fractal component
     * @return a <code>ComponentNode</code> representing <code>c</code>.
     */
    ComponentNode createComponentNode(Component c);

    /**
     * Wraps a configuration attribute into an <code>AttributeNode</code>.
     * 
     * @param c
     *            a Fractal component
     * @param attributeName
     *            the name of a configuration attribute of <code>c</code>.
     * @return an <code>AttributeNode</code> representing <code>c</code>.
     */
    AttributeNode createAttributeNode(Component c, String attributeName);

    /**
     * Wraps an interface into an <code>InterfaceNode</code>.
     * 
     * @param itf
     *            a component interface
     * @return a <code>InterfaceNode</code> representing <code>itf</code>.
     */
    InterfaceNode createInterfaceNode(Interface itf);

    /**
     * Wraps a method of a component interface into a <code>MethodNode</code>.
     * 
     * @param itf
     *            a component interface
     * @param meth
     *            a method on the interface <code>itf</code>
     * @return a <code>MethodNode</code> representing the method.
     */
    MethodNode createMethodNode(Interface itf, Method meth);
}
