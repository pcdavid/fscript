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
 * This interface can be used by clients to load FScript procedures into an engine, so that they can
 * be invoked by name at a later time. A typical implementation will do some validation of the
 * script at this time and report errors and warnings, but this is not strictly required.
 * <p>
 * If at least one fatal error is detected during the loading procedure, an
 * {@link InvalidScriptException} is thrown with the corresponding error(s) and optionally other
 * non-fatal warnings attached. In this case, <em>none</em> of the procedure definitions found in
 * the source should be loaded (even those which do not have errors). In other words, the loading
 * procedure of a set of definitions must be atomic.
 * 
 * @author Pierre-Charles David
 */
public interface ScriptLoader {
    /**
     * Loads FScript procedures definitions from source code, and make them available for later
     * invocation by name.
     * 
     * @param source
     *            the source code of the procedure definitions.
     * @return the names of all the procedures successfully loaded.
     * @throws InvalidScriptException
     *             if errors were detected in the procedure definitions.
     */
    Set<String> load(Reader source) throws InvalidScriptException;

    /**
     * Loads FScript procedures definitions from source code, and make them available for later
     * invocation by name.
     * 
     * @param source
     *            the source code of the procedure definitions.
     * @return the names of all the procedures successfully loaded.
     * @throws InvalidScriptException
     *             if errors were detected in the procedure definitions.
     */
    Set<String> load(String source) throws InvalidScriptException;
}
