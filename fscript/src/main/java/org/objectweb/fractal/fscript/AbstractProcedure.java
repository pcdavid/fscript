/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2007 ARMINES
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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript;

import java.util.Set;

import org.objectweb.fractal.fscript.nodes.Node;

/**
 * Abstract base class for native procedures: provides convenience method to check
 * arguments number and types, and to report errors.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public abstract class AbstractProcedure implements Procedure {
    public Object apply(Object[] args, Environment env) throws FScriptException {
        return apply(args);
    }

    protected void fail(String message) throws ScriptExecutionError {
        throw new ScriptExecutionError(Diagnostic.error(message));
    }

    protected void checkArity(int expected, Object[] args) throws ScriptExecutionError {
        int given = (args != null) ? args.length : 0;
        if (expected != given) {
            fail(arityErrorMessage(getName(), expected, given));
        }
    }

    protected ScriptExecutionError arityError(String procName, String expected,
            String given) {
        Diagnostic err = Diagnostic.error("Procedure " + procName + " expected "
                + expected + " but was given " + given + ".");
        return new ScriptExecutionError(err);
    }

    protected String arityErrorMessage(String procName, int expected, int given) {
        String expectedMsg;
        if (expected == 0) {
            expectedMsg = "no";
        } else {
            expectedMsg = "" + expected;
        }
        String givenMsg;
        if (given == 0) {
            givenMsg = " none were given.";
        } else if (given == 1) {
            givenMsg = " 1 was given.";
        } else {
            givenMsg = given + " were given.";
        }
        return "Procedure " + procName + " expects " + expectedMsg + " arguments but "
                + givenMsg;
    }

    protected void checkNoNulls(Object[] args) throws ScriptExecutionError {
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                fail("Argument " + (i + 1) + " may not be null.");
            }
        }
    }

    protected boolean equals(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        } else if (obj1 == null) {
            return obj2 == null;
        } else if (obj2 == null) {
            return false;
        } else {
            return obj1.equals(obj2);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T typedArgument(Object arg, Class<T> klass) throws ScriptExecutionError {
        if (Node.class.isAssignableFrom(klass) && (arg instanceof Set)) {
            // Auto-conversion from nodeset (given) to node (expected)
            Set<T> nodeSet = (Set<T>) arg;
            if (nodeSet.isEmpty()) {
                fail("Can not extract a node from an empty nodeset.");
                return null;
            } else {
                return nodeSet.iterator().next();
            }
        } else if (!klass.isInstance(arg)) {
            fail("Type error: expected " + klass.getName() + " but got "
                    + arg.getClass().getName() + ".");
            return null; // Keep the compiler happy.
        } else {
            return klass.cast(arg);
        }
    }

    protected double asNumber(Object value) {
        return Values.asNumber(value);
    }

    protected String asString(Object value) {
        return Values.asString(value);
    }

    protected boolean asBoolean(Object value) {
        return Values.asBoolean(value);
    }

    protected Node asNode(Object value) throws ScriptExecutionError {
        return Values.getSingleNode(value);
    }

    protected abstract Object apply(Object[] args) throws ScriptExecutionError;
}
