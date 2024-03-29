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

import org.objectweb.fractal.fscript.diagnostics.Diagnostic;

/**
 * This exception is thrown when at least one fatal error is detected on a script during
 * its actual execution.
 * 
 * @author Pierre-Charles David
 */
public class ScriptExecutionError extends FScriptException {
    public ScriptExecutionError(Diagnostic diagnostic) {
        super(diagnostic);
    }

    public ScriptExecutionError(Diagnostic diagnostic, Throwable cause) {
        super(diagnostic, cause);
    }
    
    @Override
    public String toString() {
        return this.getDiagnostics().get(0).toString() + (getCause() != null ? ": " + getCause() : "");
    }
}
