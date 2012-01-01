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

import static com.google.common.base.Preconditions.*;

import java.util.NoSuchElementException;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.AttributesHelper;
import org.objectweb.fractal.fscript.model.AbstractNode;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.util.Fractal;

/**
 * A {@link Node} which represents a configuration attribute of a component.
 * 
 * @author Pierre-Charles David
 */
public class AttributeNode extends AbstractNode {
    /**
     * Helper used to access attributes in a generic way. Holds a reference to the owner
     * component.
     */
    private final AttributesHelper attrHelper;

    /**
     * The name of this attribute.
     */
    private final String attrName;

    /**
     * Creates a new <code>AttributeNode</code>.
     * 
     * @param model
     *            the Fractal model this node is part of.
     * @param attrHelper
     *            the helper to use to access the attribute value.
     * @param attrName
     *            the name of attribute.
     */
    protected AttributeNode(FractalModel model, AttributesHelper attrHelper, String attrName) {
        super(model.getNodeKind("attribute"));
        checkNotNull(attrHelper);
        checkNotNull(attrName);
        checkArgument(attrHelper.hasAttribute(attrName), "No such attribute.");
        this.attrHelper = attrHelper;
        this.attrName = attrName;
    }

    /**
     * Returns the Fractal component to which this attribute belongs.
     * 
     * @return the Fractal component to which this attribute belongs.
     */
    public Component getOwner() {
        return attrHelper.getComponent();
    }

    public Object getProperty(String name) {
        if ("name".equals(name)) {
            return getName();
        } else if ("type".equals(name)) {
            return getType();
        } else if ("value".equals(name)) {
            return getValue();
        } else if ("readable".equals(name)) {
            return isReadable();
        } else if ("writable".equals(name)) {
            return isWritable();
        } else {
            throw new IllegalArgumentException("Invalid property name '" + name + "'.");
        }
    }

    public void setProperty(String name, Object value) {
        checkSetRequest(name, value);
        if ("value".equals(name)) {
            setValue(value);
        } else {
            throw new AssertionError("Should ne be reached.");
        }
    }

    /**
     * Returns the name of this attribute.
     * 
     * @return the name of this attribute.
     */
    public String getName() {
        return attrName;
    }

    /**
     * Returns the type of this attribute, as the fully-qualified name of the
     * corresponding Java type.
     * 
     * @return the type of this attribute.
     */
    public String getType() {
        return attrHelper.getAttributeType(attrName).getName();
    }

    /**
     * Returns the current value of this attribute.
     * 
     * @return the current value of this attribute.
     * @throws UnsupportedOperationException
     *             if this attribute is not readable.
     */
    public Object getValue() {
        try {
            return attrHelper.getAttribute(attrName);
        } catch (NoSuchElementException e) {
            throw new AssertionError("Attribute " + attrName + " does not exist anymore.");
        } catch (UnsupportedOperationException e) {
            throw e;
        }
    }

    /**
     * Sets the value of this attribute.
     * 
     * @param value
     *            the new value to set for this attribute.
     * @throws UnsupportedOperationException
     *             if this attribute is not writable.
     * @throws IllegalArgumentException
     *             if the requested value is not compatible with this attribute's type.
     */
    public void setValue(Object value) {
        try {
            attrHelper.setAttribute(attrName, value);
        } catch (NoSuchElementException e) {
            throw new AssertionError("Attribute " + attrName + " does not exist anymore.");
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Checks whether this attribute is readable (i.e. its value can be read using
     * {@link #getValue()}).
     * 
     * @return <code>true</code> iff this attribute is readable.
     */
    public boolean isReadable() {
        return attrHelper.hasReadableAttribute(attrName);
    }

    /**
     * Checks whether this attribute is writable (i.e. its value can be changed using
     * {@link #setValue(Object)}).
     * 
     * @return <code>true</code> iff this attribute is writable.
     */
    public boolean isWritable() {
        return attrHelper.hasWritableAttribute(attrName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AttributeNode) {
            AttributeNode other = (AttributeNode) obj;
            return this.attrHelper.getComponent() == other.attrHelper.getComponent()
                    && this.attrName.equals(other.attrName);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return attrHelper.getComponent().hashCode() * 7 + attrName.hashCode();
    }

    @Override
    public String toString() {
        Component owner = attrHelper.getComponent();
        String ownerName = "<unnamed>";
        try {
            ownerName = Fractal.getNameController(owner).getFcName();
        } catch (NoSuchInterfaceException _) {
            // Ignore.
        }
        return "#<attribute: " + ownerName + "." + getName() + ">";
    }
}
