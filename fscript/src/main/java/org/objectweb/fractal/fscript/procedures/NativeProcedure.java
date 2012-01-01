/*
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

import java.util.List;

import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.interpreter.Context;

/**
 * A native procedure can be executed directly and do not expose any internal code
 * structure.
 * 
 * @author Pierre-Charles David
 */
public interface NativeProcedure extends Procedure {
    /**
     * Apply this procedure the the specified arguments.
     * 
     * @param args
     *            the arguments of the procedure call.
     * @param ctx
     *            the execution context in which to execute the procedure.
     * @return the value returned by the procedure.
     * @throws ScriptExecutionError
     *             if any error occurred during the execution of the procedure.
     */
    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError;
}
