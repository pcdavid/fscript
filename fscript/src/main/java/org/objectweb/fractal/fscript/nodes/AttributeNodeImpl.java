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

import java.util.NoSuchElementException;

import javax.naming.OperationNotSupportedException;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.AttributesHelper;

/**
 * Default implementation of {@link AttributeNode}.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class AttributeNodeImpl implements NodeImpl, AttributeNode {
    /**
     * The component attributes helper.
     */
    private final AttributesHelper attributes;

    /**
     * The name of the attribute.
     */
    private final String attributeName;

    /**
     * Creates a new <code>AttributeNodeImpl</code>.
     * 
     * @param attributes
     *            an <code>AttributeHelper</code> for the component owning the
     *            attribute.
     * @param attributeName
     *            the name of the attribute.
     */
    AttributeNodeImpl(AttributesHelper attributes, String attributeName) {
        if (!attributes.hasAttribute(attributeName)) {
            throw new IllegalArgumentException("No attribute named " + attributeName
                    + ".");
        }
        this.attributes = attributes;
        this.attributeName = attributeName;
    }

    public Component getComponent() {
        return attributes.getComponent();
    }

    public String getName() {
        return attributeName;
    }

    public Class<?> getType() {
        return attributes.getAttributeType(attributeName);
    }

    public Object getValue() {
        try {
            return attributes.getAttribute(attributeName);
        } catch (NoSuchElementException e) {
            throw new AssertionError("Attribute existence tested on creation.");
        } catch (OperationNotSupportedException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof AttributeNode) {
            AttributeNodeImpl other = (AttributeNodeImpl) that;
            return (this.attributes.equals(other.getComponent()))
                    && (this.attributeName.equals(other.getName()));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return attributes.hashCode() * attributeName.hashCode();
    }

    @Override
    public String toString() {
        return "#<attribute: " + NameHelper.getName(getComponent()) + "@"
                + attributeName + ">";
    }
}
