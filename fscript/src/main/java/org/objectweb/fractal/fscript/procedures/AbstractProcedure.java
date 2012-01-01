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

import static org.objectweb.fractal.fscript.diagnostics.Severity.ERROR;

import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.types.Signature;
import org.objectweb.fractal.fscript.types.Type;

/**
 * Abstract base class for native procedures: provides convenience method to check
 * arguments number and types, and to report errors.
 * 
 * @author Pierre-Charles David
 */
public abstract class AbstractProcedure implements NativeProcedure {
    private final String name;

    private final boolean isPureFunction;

    private final Signature signature;

    public AbstractProcedure(String name, boolean isPure, Type[] paramsTypes, Type returnType) {
        this(name, isPure, new Signature(returnType, paramsTypes));
    }

    public AbstractProcedure(String name, boolean isPure, Signature signature) {
        this.name = name;
        this.isPureFunction = isPure;
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public Signature getSignature() {
        return signature;
    }

    public boolean isPureFunction() {
        return isPureFunction;
    }

    protected ScriptExecutionError failure(String message) {
        return failure(message, null);
    }

    protected ScriptExecutionError failure(String message, Throwable cause) {
        return new ScriptExecutionError(new Diagnostic(ERROR, getName() + "(): " + message), cause);
    }

    @Override
    public String toString() {
        return getName() + getSignature();
    }
}
