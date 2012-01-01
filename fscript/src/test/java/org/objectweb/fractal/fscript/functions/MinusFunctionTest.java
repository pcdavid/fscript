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

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.Procedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;

import static org.junit.Assert.*;

public class MinusFunctionTest {
    /**
     * 
     */
    private static final double PRECISION = 0.0;

    private Procedure minusFunction;

    @Before
    public void setUp() {
        minusFunction = new MinusFunction();
    }

    private double negate(Object obj) {
        try {
            Object result = minusFunction.apply(new Object[] { obj }, null);
            assertTrue(result instanceof Number);
            return ((Number) result).doubleValue();
        } catch (FScriptException e) {
            throw new AssertionError(e);
        }
    }

    @Test(expected = ScriptExecutionError.class)
    public void callWithNull() throws FScriptException {
        minusFunction.apply(null, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void callWithNoArgs() throws FScriptException {
        minusFunction.apply(new Object[0], null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void callWithSeveralArgs() throws FScriptException {
        minusFunction.apply(new Object[] { 1, 2 }, null);
    }

    @Test
    public void negateZero() {
        assertEquals(0.0, negate(0), PRECISION);
        assertEquals(0.0, negate(0L), PRECISION);
        assertEquals(-0.0, negate(0.0), PRECISION);
        assertEquals(0.0, negate(-0.0), PRECISION);
        assertEquals(-0.0, negate(0.0f), PRECISION);
        assertEquals(0.0, negate(-0.0f), PRECISION);
    }

    @Test
    public void negateIntegers() {
        assertEquals(-1.0, negate(1), PRECISION);
        assertEquals(1.0, negate(-1), PRECISION);
        assertEquals(-10.0, negate(10), PRECISION);
        assertEquals(10.0, negate(-10), PRECISION);
        assertEquals(-42.0, negate(42), PRECISION);
        assertEquals(42.0, negate(-42), PRECISION);
    }
    
    @Test
    public void negateDoubles() {
        assertEquals(-1.0, negate(1.0), PRECISION);
        assertEquals(1.0, negate(-1.0), PRECISION);
        assertEquals(-10.0, negate(10.0), PRECISION);
        assertEquals(10.0, negate(-10.0), PRECISION);
        assertEquals(-42.0, negate(42.0), PRECISION);
        assertEquals(42.0, negate(-42.0), PRECISION);
    }
}
