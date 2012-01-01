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
import static com.google.common.collect.Lists.*;

import java.util.Arrays;
import java.util.List;

import net.jcip.annotations.Immutable;

import com.google.common.base.Join;

/**
 * Represents the signature of a procedure.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class Signature {
    private final Type returnType;

    private final List<Type> parametersTypes;

    public Signature(Type returnType, List<Type> parametersTypes) {
        checkNotNull(returnType, "Missing return type.");
        checkNotNull(parametersTypes, "Missing parameter types.");
        checkContentsNotNull(parametersTypes, "Invalid parameter types: null not allowed.");
        this.returnType = returnType;
        this.parametersTypes = immutableList(newArrayList(parametersTypes));
    }
    
    public Signature(Type returnType, Type... parameterTypes) {
        this(returnType, Arrays.asList(parameterTypes));
    }

    public List<Type> getParametersTypes() {
        return parametersTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    @Override
    public String toString() {
        return "(" + Join.join(", ", parametersTypes) + ") -> " + returnType;
    }
}
