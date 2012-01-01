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
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.model.AbstractAxis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.util.Fractal;

/**
 * Implements the <code>binding</code> axis in FPath. This axis connects client
 * interfaces to the server interface(s) they are bound to. The
 * {@link #connect(Node, Node)} and {@link #disconnect(Node, Node)} operations on this
 * axis are used to create and destroy bindings.
 * 
 * @author Pierre-Charles David
 */
public class BindingAxis extends AbstractAxis {
    public BindingAxis(FractalModel model) {
        super(model, "binding", "interface", "interface");
    }

    public boolean isPrimitive() {
        return true;
    }

    public boolean isModifiable() {
        return true;
    }

    @Override
    public void connect(Node source, Node dest) {
        Interface client = ((InterfaceNode) source).getInterface();
        Interface server = ((InterfaceNode) dest).getInterface();
        try {
            BindingController bc = Fractal.getBindingController(client.getFcItfOwner());
            bc.bindFc(client.getFcItfName(), server);
        } catch (NoSuchInterfaceException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalBindingException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalLifeCycleException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void disconnect(Node source, Node dest) {
        Interface client = ((InterfaceNode) source).getInterface();
        Interface server = ((InterfaceNode) dest).getInterface();
        try {
            BindingController bc = Fractal.getBindingController(client.getFcItfOwner());
            assert bc.lookupFc(client.getFcItfName()) == server;
            bc.unbindFc(client.getFcItfName());
        } catch (NoSuchInterfaceException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalBindingException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalLifeCycleException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Set<Node> selectFrom(Node source) {
        InterfaceNode itfNode = (InterfaceNode) source;

        if (!itfNode.isClient()) {
            // binding:: goes only from client to servers.
            return Collections.emptySet();
        }

        Component comp = itfNode.getInterface().getFcItfOwner();
        BindingController bc = null;
        try {
            bc = Fractal.getBindingController(comp);
        } catch (NoSuchInterfaceException e) {
            throw new AssertionError("Component with a client interface "
                    + "does not implement binding-controller.");
        }

        if (itfNode.isCollection()) {
            Set<Node> result = new HashSet<Node>();
            for (Object rawCandidate : getCandidateInstances(itfNode, comp)) {
                Interface candidate = (Interface) rawCandidate;
                if (isInstance(candidate, itfNode)) {
                    Node server = getServerInterface(bc, candidate.getFcItfName());
                    if (server != null) {
                        result.add(server);
                    }
                }
            }
            return Collections.unmodifiableSet(result);
        } else {
            Node server = getServerInterface(bc, itfNode.getName());
            if (server != null) {
                return Collections.singleton(server);
            } else {
                return Collections.emptySet();
            }
        }
    }

    private boolean isInstance(Interface candidate, InterfaceNode itfNode) {
        return candidate.getFcItfName().startsWith(itfNode.getName());
    }

    private Object[] getCandidateInstances(InterfaceNode itfNode, Component comp) {
        if (itfNode.isInternal()) {
            try {
                return Fractal.getContentController(comp).getFcInternalInterfaces();
            } catch (NoSuchInterfaceException e) {
                throw new AssertionError("Internal interface on a primitive.");
            }
        } else {
            return comp.getFcInterfaces();
        }
    }

    private Node getServerInterface(BindingController bc, String clItfName) {
        try {
            Interface serverItf = (Interface) bc.lookupFc(clItfName);
            if (serverItf != null) {
                return new InterfaceNode((FractalModel) model, serverItf);
            } else {
                return null;
            }
        } catch (NoSuchInterfaceException e) {
            throw new AssertionError("Node references non-existant interface.");
        }
    }
}
