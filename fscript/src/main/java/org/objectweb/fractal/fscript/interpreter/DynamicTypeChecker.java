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
package org.objectweb.fractal.fscript.interpreter;

import static org.objectweb.fractal.fscript.diagnostics.Severity.ERROR;
import static org.objectweb.fractal.fscript.types.PrimitiveType.BOOLEAN;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.procedures.Procedure;
import org.objectweb.fractal.fscript.types.Type;

public class DynamicTypeChecker {
    public Object[] checkedArguments(Procedure proc, Object[] args) throws ScriptExecutionError {
        checkArity(proc, args);
        Object[] realArgs = new Object[args.length];
        List<Type> paramTypes = proc.getSignature().getParametersTypes();
        for (int i = 0; i < args.length; i++) {
            String msg = "Type error at argument " + i + " of " + proc.getName();
            try {
                realArgs[i] = checkedValue(paramTypes.get(i), args[i], msg);
            } catch (ScriptExecutionError e) {
                realArgs[i] = coercedValue(paramTypes.get(i), args[i], msg);
            }
        }
        return realArgs;
    }

    public Object checkedResult(Procedure proc, Object result) throws ScriptExecutionError {
        return checkedValue(proc.getSignature().getReturnType(), result,
                "Invalid return type for procedure " + proc.getName());
    }

    private void checkArity(Procedure proc, Object[] args) throws ScriptExecutionError {
        int arity = proc.getSignature().getParametersTypes().size();
        if (arity != args.length) {
            String msg = "Arity error: procedure " + proc.getName() + " expects " + arity
                    + " arguments but got " + args.length + ".";
            throw new ScriptExecutionError(new Diagnostic(ERROR, msg));
        }
    }

    private Object checkedValue(Type expectedType, Object value, String errorMessage)
            throws ScriptExecutionError {
        if (expectedType.checkValue(value)) {
            return value;
        }
        String typeName = (value == null) ? "void" : value.getClass().getName();
        String msg = errorMessage + ": expected " + expectedType + " but got " + typeName + ".";
        throw new ScriptExecutionError(new Diagnostic(ERROR, msg));
    }

    private Object coercedValue(Type type, Object value, String errorMessage)
            throws ScriptExecutionError {
        if (value instanceof Set) {
            Set nodes = (Set) value;
            try {
                Object node = nodes.iterator().next();
                if (type.checkValue(node)) {
                    return node;
                }
            } catch (NoSuchElementException nsee) {
                // Fall through to error.
            }
        } else if (value instanceof Node) {
            Set<Node> singleton = Collections.singleton((Node) value);
            if (type.checkValue(singleton)) {
                return singleton;
            }
        } else if (type == BOOLEAN) {
            return EvaluatingVisitor.asBoolean(value);
        }
        throw new ScriptExecutionError(new Diagnostic(ERROR, errorMessage));
    }
}
