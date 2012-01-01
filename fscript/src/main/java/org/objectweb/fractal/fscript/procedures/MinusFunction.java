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

import static org.objectweb.fractal.fscript.types.PrimitiveType.NUMBER;

import java.util.List;

import net.jcip.annotations.Immutable;

import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.types.Type;

/**
 * Implements the <code>-</code> (minus) unary operator to take the opposite of a
 * number.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class MinusFunction extends AbstractProcedure {
    public MinusFunction() {
        super("minus", true, new Type[] { NUMBER }, NUMBER);
    }

    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        return -((Number) args.get(0)).doubleValue();
    }
}
