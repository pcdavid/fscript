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

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.type.InterfaceType;

/**
 * This is the default implementation of {@link InterfaceNode}, which simply wraps an
 * {@link Interface} object.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class InterfaceNodeImpl implements NodeImpl, InterfaceNode {
    /**
     * The actual interface this node represents.
     */
    private final Interface itf;

    /**
     * The type of the interface.
     */
    private final InterfaceType itfType;

    /**
     * Creates a new <code>InterfaceNode</code>.
     * 
     * @param itf
     *            the actual interface the new node will represent.
     */
    public InterfaceNodeImpl(Interface itf) {
        if (itf == null) {
            throw new IllegalArgumentException("Interface can not be null.");
        }
        this.itf = itf;
        try {
            this.itfType = (InterfaceType) itf.getFcItfType();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Interface " + itf
                    + " does not implement the default type system.");
        }
    }

    public String getSignature() {
        return itfType.getFcItfSignature();
    }

    public boolean isClient() {
        return itfType.isFcClientItf();
    }

    public boolean isCollection() {
        return itfType.isFcCollectionItf();
    }

    public boolean isInternal() {
        return itf.isFcInternalItf();
    }

    public boolean isOptional() {
        return itfType.isFcOptionalItf();
    }

    /**
     * Returns the actual interface this node represents.
     * 
     * @return the actual interface this node represents.
     */
    public Interface getInterface() {
        return itf;
    }

    public Object getValue() {
        return getInterface();
    }

    public Component getComponent() {
        return itf.getFcItfOwner();
    }

    public String getName() {
        return itf.getFcItfName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InterfaceNodeImpl) {
            InterfaceNodeImpl other = (InterfaceNodeImpl) obj;
            return (this.getInterface().equals(other.getInterface()));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return itf.hashCode() * 5;
    }

    @Override
    public String toString() {
        return "#<interface: " + NameHelper.getName(itf.getFcItfOwner()) + "."
                + getName() + ">";
    }
}
