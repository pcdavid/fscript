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
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.model.AbstractNode;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.util.Fractal;

import com.google.common.base.Objects;

/**
 * A {@link Node} which represents a Fractal component. Note that in FPath, component
 * nodes are distinct from interface nodes representing the <code>component</code>
 * interface.
 * 
 * @author Pierre-Charles David
 */
public class ComponentNode extends AbstractNode {
    /**
     * The Fractal component this node represents.
     */
    private final Component component;

    /**
     * Creates a new <code>ComponentNode</code>.
     * 
     * @param model
     *            the Fractal model this node is part of.
     * @param component
     *            the component the new node will represent.
     */
    protected ComponentNode(FractalModel model, Component component) {
        super(model.getNodeKind("component"));
        checkNotNull(component);
        this.component = component;
    }

    /**
     * Returns the underlying Fractal component represented by this node.
     * 
     * @return the component represented by this node.
     */
    public Component getComponent() {
        return component;
    }

    public Object getProperty(String name) {
        if ("name".equals(name)) {
            return getName();
        } else if ("state".equals(name)) {
            return getState();
        } else {
            throw new IllegalArgumentException("Invalid property name '" + name + "'.");
        }
    }

    public void setProperty(String name, Object newValue) {
        checkSetRequest(name, newValue);
        if ("name".equals(name)) {
            setName((String) newValue);
        } else if ("state".equals(name)) {
            setState((String) newValue);
        } else {
            throw new AssertionError("Should ne be reached.");
        }
    }

    /**
     * Returns the name of this component, as defined by its <code>name-controller</code>
     * interface. If the component does not provide the standard
     * <code>name-controller</code> interface or if its name is <code>null</code>,
     * the node's name is defined as the empty string.
     * 
     * @return the name of this component node.
     */
    public String getName() {
        try {
            String name = Fractal.getNameController(component).getFcName();
            return Objects.firstNonNull(name, "");
        } catch (NoSuchInterfaceException e) {
            return "";
        }
    }

    /**
     * Returns the current lifecycle state of this component, as defined by its
     * <code>lifecycle-controller</code> interface. If the component does not provide
     * the standard <code>lifecycle-controller</code>, or if its state is
     * <code>null</code>, the node's state is the empty string.
     * 
     * @return the lifecycle state of this component node.
     */
    public String getState() {
        try {
            String state = Fractal.getLifeCycleController(component).getFcState();
            return Objects.firstNonNull(state, "");
        } catch (NoSuchInterfaceException e) {
            return "";
        }
    }

    /**
     * Sets the name of this component using the standard <code>name-controller</code>
     * interface.
     * 
     * @param name
     *            the new name of the component.
     * @throws UnsupportedOperationException
     *             if the underlying component does not provide the standard
     *             <code>name-controller</code> interface.
     */
    public void setName(String name) {
        checkSetRequest("name", name);
        try {
            Fractal.getNameController(component).setFcName(name);
        } catch (NoSuchInterfaceException e) {
            throw new UnsupportedOperationException("Can not change the name of this component.");
        }
    }

    /**
     * Sets the lifecycle state of this component, using the standard
     * <code>lifecycle-controller</code> interface.
     * 
     * @param state
     *            the requested new state of the component. The only value currently
     *            supported are <code>"STARTED"</code> and <code>"STOPPED"</code>.
     * @throws IllegalArgumentException
     *             if the name of the requested state is not supported or if setting this
     *             state would cause an invalid state transition.
     * @throws NoSuchInterfaceException
     *             if the underlying component does not provide the standard
     *             <code>lifecycle-controller</code> interface.
     */
    public void setState(String state) {
        try {
            if ("STARTED".equals(state)) {
                Fractal.getLifeCycleController(component).startFc();
            } else if ("STOPPED".equals(state)) {
                Fractal.getLifeCycleController(component).stopFc();
            } else {
                throw new IllegalArgumentException("Invalid value for 'state': '" + state + "'.");
            }
        } catch (NoSuchInterfaceException nsie) {
            throw new UnsupportedOperationException("Can not change the state of this component.",
                    nsie);
        } catch (IllegalLifeCycleException ilce) {
            throw new IllegalArgumentException("Invalid state transition.", ilce);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComponentNode) {
            ComponentNode other = (ComponentNode) obj;
            return this.component == other.component;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return component.hashCode();
    }

    @Override
    public String toString() {
        return "#<component: " + getName() + ">";
    }
}
