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

import static com.google.common.base.Preconditions.checkNotNull;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.fscript.model.AbstractNode;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.util.Fractal;

/**
 * A {@link Node} which represents an interface of a Fractal component. Every kinds of
 * interface (internal of external, client or server, etc.) is represented by instances of
 * this class.
 * 
 * @author Pierre-Charles David
 */
public class InterfaceNode extends AbstractNode {
    /**
     * The actual interface this node represents.
     */
    private final Interface itf;

    /**
     * The type of the underlying interface. The <code>interface</code> node-kind
     * includes both the intrinsic interface properties (like its name) and the properties
     * defined in the standard {@link InterfaceType}, so this is stored explicitly here
     * to avoid repeated {@link Interface#getFcItfType()} calls.
     */
    private final InterfaceType itfType;

    /**
     * Creates a new interface node.
     * 
     * @param model
     *            the Fractal model the node is part of.
     * @param itf
     *            the interface the new node will represent.
     */
    protected InterfaceNode(FractalModel model, Interface itf) {
        super(model.getNodeKind("interface"));
        checkNotNull(itf);
        this.itf = itf;
        this.itfType = (InterfaceType) itf.getFcItfType();
    }

    /**
     * Returns the Fractal component interface this node represents.
     * 
     * @return the interface this node represents.
     */
    public Interface getInterface() {
        return itf;
    }

    public Object getProperty(String name) {
        if ("name".equals(name)) {
            return getName();
        } else if ("internal".equals(name)) {
            return isInternal();
        } else if ("signature".equals(name)) {
            return getSignature();
        } else if ("collection".equals(name)) {
            return isCollection();
        } else if ("client".equals(name)) {
            return isClient();
        } else if ("optional".equals(name)) {
            return isOptional();
        } else {
            throw new IllegalArgumentException("Invalid property name '" + name + "'.");
        }
    }

    public void setProperty(String propertyName, Object newValue) {
        checkSetRequest(propertyName, newValue);
        throw new AssertionError("Should ne be reached.");
    }

    /**
     * Returns the name of the interface represented by this node.
     * 
     * @return the name of this interface.
     */
    public String getName() {
        return itf.getFcItfName();
    }

    /**
     * Checks whether the interface represented by this node is an internal or external
     * interface.
     * 
     * @return <code>true</code> iff this interface is internal.
     */
    public boolean isInternal() {
        return itf.isFcInternalItf();
    }

    /**
     * Returns the signature of the interface represented by this node, i.e. the fully
     * qualified name of the corresponding Java interface.
     * 
     * @return the signature of this interface.
     */
    public String getSignature() {
        return itfType.getFcItfSignature();
    }

    /**
     * Checks whether the interface represented by this node is a collection or a single
     * interface.
     * 
     * @return <code>true</code> iff this interface is a collection interface.
     */
    public boolean isCollection() {
        return itfType.isFcCollectionItf();
    }

    /**
     * Checks whether the interface represented by this node is a client or a server
     * interface.
     * 
     * @return <code>true</code> iff this interface is a client interface.
     */
    public boolean isClient() {
        return itfType.isFcClientItf();
    }

    /**
     * Checks whether the interface represented by this node is an optional or a mandatory
     * interface.
     * 
     * @return <code>true</code> iff this interface is an optional interface.
     */
    public boolean isOptional() {
        return itfType.isFcOptionalItf();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InterfaceNode) {
            InterfaceNode other = (InterfaceNode) obj;
            return this.itf.equals(other.itf);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return itf.hashCode();
    }

    @Override
    public String toString() {
        Component owner = itf.getFcItfOwner();
        String ownerName = "<unnamed>";
        try {
            ownerName = Fractal.getNameController(owner).getFcName();
        } catch (NoSuchInterfaceException _) {
            // Ignore.
        }
        return "#<interface: " + ownerName + "." + getName() + ">";
    }
}
