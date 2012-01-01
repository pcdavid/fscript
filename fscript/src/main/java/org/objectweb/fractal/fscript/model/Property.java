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

import net.jcip.annotations.Immutable;

import org.objectweb.fractal.fscript.types.PrimitiveType;

import com.google.common.base.Preconditions;

/**
 * Describes a property (or more precisely a property type) of an FPath node.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class Property {
    private final String name;
    private final PrimitiveType type;
    private final boolean writable;
    
    public Property(String name, PrimitiveType type, boolean writable) {
        Preconditions.checkNotNull(name, "Missing property name.");
        Preconditions.checkNotNull(type, "Missing property type.");
        this.name = name;
        this.type = type;
        this.writable = writable;
    }

    public String getName() {
        return name;
    }
    
    public PrimitiveType getType() {
        return type;
    }
    
    public boolean isWritable() {
        return writable;
    }
    
    @Override
    public String toString() {
        return name + " : " + type + (writable ? "" : " (ro)");
    }
}
