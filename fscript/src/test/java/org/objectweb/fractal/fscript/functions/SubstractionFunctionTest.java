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

public class SubstractionFunctionTest {
    private static final double PRECISION = 0.0;

    private Procedure substractFunction;

    @Before
    public void setUp() {
        substractFunction = new SubstractionFunction();
    }

    private void assertDifference(double expected, Object... args) {
        try {
            Object result = substractFunction.apply(args, null);
            assertTrue(result instanceof Double);
            double d = ((Double) result).doubleValue();
            assertEquals(expected, d, PRECISION);
        } catch (FScriptException e) {
            throw new AssertionError(e);
        }

    }

    @Test(expected = ScriptExecutionError.class)
    public void callWithNull() throws FScriptException {
        substractFunction.apply(null, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void callWithNoArugment() throws FScriptException {
        substractFunction.apply(new Object[0], null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void callWithOneArgument() throws FScriptException {
        substractFunction.apply(new Object[] { 1 }, null);
    }

    @Test
    public void substractTwoIntegers() {
        assertDifference(1.0, 1, 0);
        assertDifference(-1.0, 0, 1);
        assertDifference(2.0, 5, 3);
        assertDifference(-2.0, 3, 5);
    }

    @Test
    public void substractManyIntegers() {
        assertDifference(0.0, 5, 3, 2);
        assertDifference(-35.0, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
    }

    @Test
    public void substractTwoDoubles() {
        assertDifference(1.0, 1.0, 0.0);
        assertDifference(-1.0, 0.0, 1.0);
        assertDifference(2.0, 5.0, 3.0);
        assertDifference(-2.0, 3.0, 5.0);
    }

    @Test
    public void substractManyDoubles() {
        assertDifference(0.0, 5.0, 3.0, 2.0);
        assertDifference(-35.0, 10.0, 9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0);
    }
}
