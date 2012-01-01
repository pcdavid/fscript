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
package org.objectweb.fractal.fscript.types;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Iterables.any;
import static org.objectweb.fractal.fscript.model.NodeKind.ANY_NODE_KIND;
import static org.objectweb.fractal.fscript.types.NodeSetType.ANY_NODE_SET_TYPE;
import static org.objectweb.fractal.fscript.types.PrimitiveType.*;
import static org.objectweb.fractal.fscript.types.VoidType.VOID_TYPE;

import java.util.Set;

import net.jcip.annotations.Immutable;

import com.google.common.base.Join;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

/**
 * Represents the union of several alternative types.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class UnionType implements Type {
    public static final Type ANY_PRIMITIVE_TYPE = new UnionType("primitive", BOOLEAN, NUMBER,
            STRING, OBJECT);

    public static final Type ANY_TYPE = new UnionType("any", ANY_NODE_KIND, ANY_NODE_SET_TYPE,
            ANY_PRIMITIVE_TYPE, VOID_TYPE);

    private final Set<Type> alternatives;

    private final String name;

    public UnionType(String name, Set<Type> alternatives) {
        checkNotNull(alternatives);
        checkArgument(!alternatives.isEmpty(), "Empty union type.");
        checkContentsNotNull(alternatives);
        this.name = name;
        this.alternatives = Sets.immutableSet(Sets.newHashSet(alternatives));
    }

    public UnionType(Type... alternatives) {
        this(null, Sets.newHashSet(alternatives));
    }

    public UnionType(String name, Type... alternatives) {
        this(name, Sets.newHashSet(alternatives));
    }

    public boolean checkValue(final Object value) {
        return any(alternatives, new Predicate<Type>() {
            public boolean apply(Type alternative) {
                return alternative.checkValue(value);
            }
        });
    }

    @Override
    public String toString() {
        return (name != null) ? name : "(" + Join.join(" | ", alternatives) + ")";
    }
}
