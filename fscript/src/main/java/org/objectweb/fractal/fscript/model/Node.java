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
package org.objectweb.fractal.fscript.model;

import java.util.NoSuchElementException;

/**
 * Represents an architectural element as a node in a graph. This base interface offers a
 * generic API to introspect and manipulate the architecture elements, independent of the
 * the implementation or even of the concrete component model (e.g. Fractal, or something
 * else). Note that only the elements' properties are available through this interface.
 * The relations between architectural elements, which model the actual structure of the
 * target system, are represented using {@link Axis axes}.
 * 
 * @see NodeKind
 * @see Axis
 * @author Pierre-Charles David
 */
public interface Node {
    /**
     * Return the kind of architectural element this node is an instance of.
     * 
     * @return a descriptor for the kind of element this node models.
     */
    NodeKind getKind();

    /**
     * Returns the current value of one of this node's properties.
     * 
     * @param name
     *            the name of the property to access.
     * @return the current value of the named property for this node.
     * @throws NoSuchElementException
     *             if this node does not have a property of the given name.
     */
    Object getProperty(String name);

    /**
     * Changes the value of one of this node's properties.
     * 
     * @param name
     *            the name of the property to change.
     * @param value
     *            the new value to set for the property.
     * @throws NoSuchElementException
     *             if this node does not have a property of the given name.
     * @throws IllegalArgumentException
     *             if the requested value is invalid for the named property.
     * @throws UnsupportedOperationException
     *             if the specified property exists, but is not modifiable.
     * @throws IllegalStateException
     *             if the property can not be changed at this moment.
     * @throws RuntimeException
     *             if the underlying operation, which applies the change to the modeled
     *             element, fails. The initial error is available through
     *             <code>getCause()/<code>.
     */
    void setProperty(String name, Object value);
}
