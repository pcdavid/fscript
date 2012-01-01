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

import java.util.HashMap;
import java.util.Map;

import net.jcip.annotations.NotThreadSafe;

/**
 * A completely generic implementation of the {@link Node} interface. This implementation
 * is "virtual" in the sense that it is not connected to an underlying object (e.g. a
 * Fractal component).
 * 
 * @author Pierre-Charles David
 */
@NotThreadSafe
public class GenericNode extends AbstractNode {
    private static final Object UNDEFINED = null;

    /**
     * The current values of this node's properties.
     */
    protected final Map<String, Object> properties = new HashMap<String, Object>();

    public GenericNode(NodeKind kind) {
        super(kind);
        for (Property prop : kind.getProperties()) {
            properties.put(prop.getName(), UNDEFINED);
        }
    }

    public Object getProperty(String name) {
        if (properties.containsKey(name)) {
            return properties.get(name);
        } else {
            throw new IllegalArgumentException("Invalid property name '" + name + "'.");
        }
    }

    public void setProperty(String name, Object value) {
        checkSetRequest(name, value);
        properties.put(name, value);
    }
}
