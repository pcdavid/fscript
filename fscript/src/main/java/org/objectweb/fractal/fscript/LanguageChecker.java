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

import org.objectweb.fractal.fscript.ast.Call;
import org.objectweb.fractal.fscript.ast.UserProcedure;

/**
 * A {@link LanguageChecker} is called during the static validation of FScript procedures,
 * just after the syntactic analysis. It is used to implement model-independent checks of
 * the structure of the procedure definitions (for example, ensure correct usage of
 * variables, or the absence of unattainable code paths).
 * 
 * @author Pierre-Charles David
 */
public interface LanguageChecker {
    /**
     * Checks a user-defined FScript procedure. This method may modify the procedure
     * definition, for example to add computed information (e.g. {@link Call} targets) or
     * rewrite some parts of the AST (e.g. for optimization purposes).
     * 
     * @param proc
     *            the AST of the procedure to check.
     * @throws InvalidScriptException
     *             if an error was detected in the procedure definition.
     */
    void check(UserProcedure proc) throws InvalidScriptException;
}
