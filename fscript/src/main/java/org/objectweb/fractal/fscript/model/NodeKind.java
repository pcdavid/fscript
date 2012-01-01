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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.fscript.types.Type;

import com.google.common.base.Preconditions;

public class NodeKind implements Type {
    public static final NodeKind ANY_NODE_KIND = new NodeKind("node") {
        @Override
        public boolean checkValue(Object value) {
            return value instanceof Node;
        }
    };

    private final String name;

    private final Map<String, Property> properties;

    public NodeKind(String name, Property... properties) {
        Preconditions.checkNotNull(name, "Missing node kind name.");
        Preconditions.checkNotNull(properties, "Missing node kind properties.");
        this.name = name;
        this.properties = new HashMap<String, Property>();
        for (Property p : properties) {
            addProperty(p);
        }
    }

    protected void addProperty(Property p) {
        if (this.properties.containsKey(p.getName())) {
            throw new IllegalArgumentException("Duplicate property name '" + p.getName() + "'.");
        }
        this.properties.put(p.getName(), p);
    }

    public String getName() {
        return name;
    }

    public Collection<Property> getProperties() {
        return Collections.unmodifiableCollection(properties.values());
    }

    public Property getProperty(String name) {
        return properties.get(name);
    }

    public boolean checkValue(Object value) {
        return (value instanceof Node) && ((Node) value).getKind() == this;
    }

    public String inspect() {
        StringBuilder sb = new StringBuilder("node-kind ");
        sb.append(getName()).append(" {\n");
        for (Property prop : getProperties()) {
            sb.append("  ").append(prop).append(";\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return getName();
    }
}
