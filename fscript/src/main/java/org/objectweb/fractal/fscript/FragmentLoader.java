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
package org.objectweb.fractal.fscript;

import java.io.Reader;

/**
 * This interface is used to wrap bare expressions or statements given by the end-user
 * (typically via {@link FScriptEngine#execute(Reader)} into full-fledged procedures
 * (functions of actions depending on the case). These procedures can then be invoked
 * using the standard, lower-level mechanism of
 * {@link FScriptEngine#invoke(String, Object...)}. They should generally be considered
 * temporary and undefined after their invocation.
 * 
 * @author Pierre-Charles David
 */
public interface FragmentLoader {
    /**
     * Wraps a source code fragment (FPath expression or FScript statement) into a
     * temporary procedure.
     * 
     * @param source
     *            the source code fragment.
     * @return the name of the procedure which has been defined to encapsulate the code
     *         fragment.
     * @throws InvalidScriptException
     *             if the code fragment is invalid and can not be turned into a procedure
     *             body.
     */
    String loadFragment(Reader source) throws InvalidScriptException;

    /**
     * Removes a temporary procedure created by {@link #loadFragment(Reader)} from the
     * library of available procedures.
     * 
     * @param tempProcName
     *            the name of a temporary procedure, as returned by
     *            {@link #loadFragment(Reader)}.
     */
    void unloadFragment(String tempProcName);
}