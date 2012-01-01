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

import java.io.Reader;
import java.util.Set;

/**
 * This is the main interface for clients to interact with an FScript implementation.
 * 
 * @author Pierre-Charles David
 */
public interface FScriptEngine {
    /**
     * Returns the current value of a global variable.
     * 
     * @param name
     *            the name of the global variable to get.
     * @return the current value of the global variable requested, or <code>null</code>
     *         if there is no such variable.
     */
    Object getGlobalVariable(String name);

    /**
     * Sets the value of a global variable, or deletes a global variable (when set to
     * <code>null</code>).
     * 
     * @param name
     *            the name of the global variable to change or delete.
     * @param value
     *            the new value to set for the requested variable. If <code>null</code>,
     *            the variable is deleted.
     */
    void setGlobalVariable(String name, Object value);

    /**
     * Returns the names of all the currently defined global variables.
     * 
     * @return the names of all the currently defined global variables.
     */
    Set<String> getGlobals();

    /**
     * Execute a code fragment: either an FPath expression or a single FScript statement.
     * 
     * @param source
     *            the code fragment to execute.
     * @return the value of the code fragment, if successfully executed.
     * @throws FScriptException
     *             if an error occurred during the execution of the code fragment.
     */
    Object execute(Reader source) throws FScriptException;

    /**
     * Execute a code fragment: either an FPath expression or a single FScript statement.
     * 
     * @param source
     *            the code fragment to execute.
     * @return the value of the code fragment, if successfully executed.
     * @throws FScriptException
     *             if an error occurred during the execution of the code fragment.
     */
    Object execute(String source) throws FScriptException;

    /**
     * Invoke a named FScript procedure. The procedure must be available in the library
     * (i.e. either it is predefined, or it has already been
     * {@linkplain ScriptLoader#load(Reader) loaded}).
     * 
     * @param procName
     *            the name of the procedure to invoke.
     * @param args
     *            the arguments to pass to the procedure. Their number and type must match
     *            the procedure's signature.
     * @return the value returned by the procedure, if the execution succeeded.
     * @throws FScriptException
     *             if an error occurred during the execution of the procedure.
     */
    Object invoke(String procName, Object... args) throws FScriptException;
}
