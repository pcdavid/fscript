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
package org.objectweb.fractal.fscript.model.fractal;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.fscript.model.Node;

/**
 * A <em>factory</em> for Fractal-specific {@linkplain Node nodes}. This interface can
 * be used by clients to wrap objects from the standard Fractal API in {@link Node}s
 * appropriate for FPath to manipulate in a uniform way.
 * 
 * @author Pierre-Charles David
 */
public interface NodeFactory {
    /**
     * Creates a {@link ComponentNode} to represent a Fractal component.
     * 
     * @param comp
     *            a Fractal component.
     * @return a {@link ComponentNode} representing the <code>comp</code> component.
     */
    ComponentNode createComponentNode(Component comp);

    /**
     * Creates an {@link InterfaceNode} to represent an interface of a Fractal component.
     * 
     * @param itf
     *            an interface of a Fractal component.
     * @return an {@link InterfaceNode} representing the <code>itf</code> interface.
     */
    InterfaceNode createInterfaceNode(Interface itf);

    /**
     * Creates an {@link AttributeNode} to represent an attribute of a Fractal component.
     * 
     * @param comp
     *            a Fractal component.
     * @param attr
     *            the name of the attribute to represent.
     * @return an {@link AttributeNode} representing the attribute named <code>name</code>
     *         of the component <code>comp</code>.
     * @throws IllegalArgumentException
     *             if the component does not have a configuration attribute of the given
     *             name.
     */
    AttributeNode createAttributeNode(Component comp, String attr);
}