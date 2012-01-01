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
package org.objectweb.fractal.fscript;

import java.util.Map;

/**
 * This interface is used to invoke an already defined procedure by name.
 * 
 * @author Pierre-Charles David
 */
public interface Executor {
    /**
     * Invokes a procedure by name.
     * 
     * @param procName
     *            the name of the procedure to invoke.
     * @param args
     *            the invocation arguments.
     * @param bindings
     *            global variable bindings to consider during the execution.
     * @return the return value of the procedure, or <code>null</code> if it does not return a
     *         value.
     * @throws ScriptExecutionError
     *             if an error occurs during the execution of the procedure.
     * @throws IllegalArgumentException
     *             if no procedure named <code>procName</code> is available.
     */
    Object invoke(String procName, Object[] args, Map<String, Object> bindings)
            throws ScriptExecutionError;
}
