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
package org.objectweb.fractal.fscript.simulation;

import org.objectweb.fractal.fscript.model.GenericNode;
import org.objectweb.fractal.fscript.model.ModelListener;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.Property;

/**
 * A node which gets its (initial) properties values from another base node, but stores
 * all the modifications locally.
 * 
 * @author Pierre-Charles David
 */
public class DerivedNode extends GenericNode {
    /**
     * The base node from which to derive the node-kind and the properties initial values.
     */
    private final Node base;
    
    /**
     * The listener to notify of any change to the node's properties.
     */
    private final ModelListener listener;

    public DerivedNode(Node base, ModelListener listener) {
        super(base.getKind());
        this.base = base;
        this.listener = listener;
        fillInitialValues();
    }
    
    Node getBaseNode() {
        return base;
    }
    
    @Override
    public void setProperty(String name, Object value) {
        Object oldValue = getProperty(name);
        super.setProperty(name, value);
        if (listener != null) {
            listener.propertyChanged(this, name, oldValue, value);
        }
    }

    private void fillInitialValues() {
        for (Property prop : getKind().getProperties()) {
            String name = prop.getName();
            properties.put(name, base.getProperty(name));
        }
    }
}
