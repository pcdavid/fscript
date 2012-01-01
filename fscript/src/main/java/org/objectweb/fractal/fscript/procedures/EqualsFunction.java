/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2008 ARMINES
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
package org.objectweb.fractal.fscript.procedures;

import static org.objectweb.fractal.fscript.types.PrimitiveType.BOOLEAN;
import static org.objectweb.fractal.fscript.types.UnionType.ANY_TYPE;

import java.util.List;

import net.jcip.annotations.Immutable;

import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.types.Type;

import com.google.common.base.Objects;

/**
 * Implements the <code>==</code> binary operator.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class EqualsFunction extends AbstractProcedure {
    public EqualsFunction() {
        super("==", true, new Type[] { ANY_TYPE, ANY_TYPE }, BOOLEAN);
    }

    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        return Objects.equal(args.get(0), args.get(1));
    }
}
