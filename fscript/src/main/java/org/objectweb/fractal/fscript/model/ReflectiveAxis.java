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
package org.objectweb.fractal.fscript.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Set;


/**
 * Performs a reflective closure on a base axis. The input and output types of the base
 * axis must be the same. If no explicit name is given at construction time, the
 * reflective axis name is the base axis's name with <code>-or-self</code> appended.
 * 
 * @author Pierre-Charles David
 */
public class ReflectiveAxis extends AbstractAxis {
    private final Axis base;

    public ReflectiveAxis(Axis base) {
        this(base, base.getName() + "-or-self");
    }

    public ReflectiveAxis(Axis base, String name) {
        super(base.getModel(), name, base.getInputNodeType(), base.getInputNodeType());
        checkArgument(base.getInputNodeType().equals(base.getOutputNodeType()));
        this.base = base;
    }
    
    public boolean isPrimitive() {
        return false;
    }
    
    public boolean isModifiable() {
        return false;
    }

    public Set<Node> selectFrom(Node source) {
        Set<Node> result = base.selectFrom(source);
        result.add(source);
        return result;
    }
}
