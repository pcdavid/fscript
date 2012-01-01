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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.fscript.model.AbstractAxis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.util.Fractal;

/**
 * Implements the <code>interface</code> and <code>internal-interface</code> axes in
 * FPath. The <code>interface</code> axis connects component nodes to the (interface)
 * nodes representing their external interfaces. The <code>internal-interface</code>
 * axis connects composite nodes to the (interface) nodes representing their internal
 * interfaces. Both axes are non-modifiable.
 * <p>
 * Note that hidden "prototype" interfaces created for collection interfaces are
 * explicitly represented as a node.
 * 
 * @author Pierre-Charles David
 */
public class InterfaceAxis extends AbstractAxis {
    /**
     * Flag to tell if this axis represents the <code>interface</code> axis (<code>false</code>)
     * or the <code>internal-interface</code> axis (<code>true</code>).
     */
    private final boolean internal;

    public InterfaceAxis(FractalModel model, boolean internal) {
        super(model, internal ? "internal-interface" : "interface", "component", "interface");
        this.internal = internal;
    }

    public boolean isPrimitive() {
        return true;
    }

    public boolean isModifiable() {
        return false;
    }

    public Set<Node> selectFrom(Node source) {
        Component comp = ((ComponentNode) source).getComponent();
        ContentController cc = null;
        if (internal) {
            try {
                cc = Fractal.getContentController(comp);
            } catch (NoSuchInterfaceException e) {
                // Primitives do not have any internal interfaces.
                return Collections.emptySet();
            }
        }
        Set<Node> result = new HashSet<Node>();
        Object[] rawItfs = internal ? cc.getFcInternalInterfaces() : comp.getFcInterfaces();
        addInterfaces(rawItfs, result);
        addPrototypeInterfaces(comp, cc, result);
        return result;
    }

    private void addInterfaces(Object[] rawItfs, Set<Node> result) {
        NodeFactory nf = (NodeFactory) model;
        for (Object rawItf : rawItfs) {
            Interface itf = (Interface) rawItf;
            result.add(nf.createInterfaceNode(itf));
        }
    }

    private void addPrototypeInterfaces(Component comp, ContentController cc, Set<Node> result) {
        ComponentType type = (ComponentType) comp.getFcType();
        for (InterfaceType itfType : type.getFcInterfaceTypes()) {
            if (itfType.isFcCollectionItf()) {
                String prototypeName = "/collection/" + itfType.getFcItfName();
                result.add(getInterfaceNode(comp, cc, prototypeName));
            }
        }
    }

    private Node getInterfaceNode(Component comp, ContentController cc, String itfName) {
        try {
            Object itf = null;
            if (internal) {
                itf = cc.getFcInternalInterface(itfName);
            } else {
                itf = comp.getFcInterface(itfName);
            }
            NodeFactory nf = (NodeFactory) model;
            return nf.createInterfaceNode((Interface) itf);
        } catch (NoSuchInterfaceException e) {
            throw new AssertionError();
        }
    }
}
