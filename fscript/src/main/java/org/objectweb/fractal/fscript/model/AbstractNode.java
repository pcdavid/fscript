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

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractNode implements Node {
    protected final NodeKind kind;

    public AbstractNode(NodeKind kind) {
        checkNotNull(kind, "Missing node kind.");
        this.kind = kind;
    }

    public NodeKind getKind() {
        return kind;
    }

    /**
     * Concrete subclasses should call this method to validate a request to change one of
     * this node's properties.
     */
    protected void checkSetRequest(String propertyName, Object newValue) {
        Property prop = kind.getProperty(propertyName);
        if (prop == null) {
            throw new IllegalArgumentException("Invalid property name '" + propertyName + "'.");
        }
        if (!prop.getType().checkValue(newValue)) {
            throw new IllegalArgumentException("Incompatible value for property " + prop + ".");
        }
        if (!prop.isWritable()) {
            throw new UnsupportedOperationException("Property " + prop.getName() + " is read-only.");
        }
    }

    public String inspect() {
        StringBuilder sb = new StringBuilder("node ");
        sb.append(kind.getName()).append(" {\n");
        for (Property prop : kind.getProperties()) {
            sb.append("  ").append(prop.getName());
            sb.append(" : ").append(prop.getType());
            sb.append(prop.isWritable() ? " (rw)" : " (ro)");
            Object value = getProperty(prop.getName());
            if (value != null) {
                sb.append(" = ").append(value);
            } else {
                sb.append(" (not set)");
            }
            sb.append(";\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return kind.getName() + "-node";
    }
}
