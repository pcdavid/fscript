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
package org.objectweb.fractal.fscript.functions;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.Procedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;

public class BooleanFunctionsTest {
    private Procedure trueFunction;

    private Procedure falseFunction;

    private Procedure notFunction;

    @Before
    public void setUp() {
        trueFunction = new TrueFunction();
        falseFunction = new FalseFunction();
        notFunction = new NotFunction();
    }

    @Test(expected = ScriptExecutionError.class)
    public void trueWithArguments() throws FScriptException {
        trueFunction.apply(new Object[] { 1 }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void falseWithArguments() throws FScriptException {
        falseFunction.apply(new Object[] { 1 }, null);
    }

    @Test
    public void callTrueWithNull() throws FScriptException {
        Object result = trueFunction.apply(null, null);
        assertTrue(result instanceof Boolean);
        assertTrue(Boolean.valueOf((Boolean) result));
    }

    @Test
    public void callTrueWithZeroArgs() throws FScriptException {
        Object result = trueFunction.apply(new Object[0], null);
        assertTrue(result instanceof Boolean);
        assertTrue(Boolean.valueOf((Boolean) result));
    }

    @Test
    public void callFalseWithNull() throws FScriptException {
        Object result = falseFunction.apply(null, null);
        assertTrue(result instanceof Boolean);
        assertTrue(!Boolean.valueOf((Boolean) result));
    }

    @Test
    public void callFalseWithZeroArgs() throws FScriptException {
        Object result = falseFunction.apply(new Object[0], null);
        assertTrue(result instanceof Boolean);
        assertTrue(!Boolean.valueOf((Boolean) result));
    }

    @Test(expected = ScriptExecutionError.class)
    public void notWithNullArgument() throws FScriptException {
        notFunction.apply(null, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void notWithNoArgument() throws FScriptException {
        notFunction.apply(new Object[0], null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void notWithSeveralArgument() throws FScriptException {
        notFunction.apply(new Object[] { true, false, false }, null);
    }

    @Test
    public void notTrue() throws FScriptException {
        // Argument as primitive
        Object result = notFunction.apply(new Object[] { true }, null);
        assertTrue(result instanceof Boolean);
        assertTrue(!Boolean.valueOf((Boolean) result));
        // Argument as object (Boolean)
        result = notFunction.apply(new Object[] { Boolean.TRUE }, null);
        assertTrue(result instanceof Boolean);
        assertTrue(!Boolean.valueOf((Boolean) result));
    }
    
    @Test
    public void notFalse() throws FScriptException {
        // Argument as primitive
        Object result = notFunction.apply(new Object[] { false }, null);
        assertTrue(result instanceof Boolean);
        assertTrue(Boolean.valueOf((Boolean) result));
        // Argument as object (Boolean)
        result = notFunction.apply(new Object[] { Boolean.FALSE }, null);
        assertTrue(result instanceof Boolean);
        assertTrue(Boolean.valueOf((Boolean) result));
    }

}
