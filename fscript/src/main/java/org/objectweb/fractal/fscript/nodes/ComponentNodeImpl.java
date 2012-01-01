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
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.api.control.NameController;
import org.objectweb.fractal.fscript.AttributesHelper;
import org.objectweb.fractal.util.Fractal;

/**
 * Default implementation of {@link ComponentNode}, which simply wraps a
 * {@link Component}.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class ComponentNodeImpl implements NodeImpl, ComponentNode {
    /**
     * The default state assumed for components which do not expose a
     * <code>lifecycle-controller</code>.
     */
    private static final String DEFAULT_STATE = "STOPPED";

    /**
     * The actual component this node represents.
     */
    private final Component component;
    
    private AttributesHelper attrHelper;

    /**
     * Creates a new <code>ComponentNodeImpl</code>.
     * 
     * @param value
     *            the actual component the new node will represent.
     */
    public ComponentNodeImpl(Component value) {
        if (value == null) {
            throw new IllegalArgumentException("Component can not be null.");
        }
        this.component = value;
    }
    
    public AttributesHelper getAttributesHelper() {
        synchronized (this) {
            if (attrHelper == null) {
                attrHelper = new AttributesHelper(component);
            }
        }        
        return attrHelper;
    }

    public String getName() {
        try {
            NameController nc = Fractal.getNameController(component);
            return nc.getFcName();
        } catch (NoSuchInterfaceException e) {
            return "";
        }
    }

    public String getState() {
        try {
            LifeCycleController lcc = Fractal.getLifeCycleController(component);
            return lcc.getFcState();
        } catch (NoSuchInterfaceException e) {
            return DEFAULT_STATE;
        }
    }

    public Object getValue() {
        return component;
    }

    public Component getComponent() {
        return this.component;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComponentNodeImpl) {
            ComponentNodeImpl other = (ComponentNodeImpl) obj;
            return (this.component.equals(other.getComponent()));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return component.hashCode() * 3;
    }

    @Override
    public String toString() {
        return "#<component: " + NameHelper.getName(component) + ">";
    }
}
