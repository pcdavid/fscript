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

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.ScriptExecutionError;

public class DivisionFunctionsTest {
    private static final double PRECISION = 0.0;

    private NativeProcedure divisionFunction;

    @Before
    public void setUp() {
        divisionFunction = new DivisionFunction();
    }

    private double divide(Object... args) {
        Object result;
        try {
            result = divisionFunction.apply(Arrays.asList(args), null);
        } catch (FScriptException e) {
            throw new AssertionError(e);
        }
        assertTrue(result instanceof Double);
        return ((Double) result).doubleValue();
    }

    private void assertDivision(double expected, Object... args) {
        assertEquals(expected, divide(args), PRECISION);
    }


    @Test
    public void divideZero() {
        assertDivision(0.0, 0, 42);
        assertDivision(0.0, 0, -2.718281828);
        assertDivision(0.0, 0.0, 42);
        assertDivision(0.0, 0.0, -2.718281828);
    }

    @Test(expected = ScriptExecutionError.class)
    public void divideByIntegerZero() throws FScriptException {
        divisionFunction.apply(Arrays.asList((Object) 42, 0), null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void divideByDoubleZero() throws FScriptException {
        divisionFunction.apply(Arrays.asList((Object) 42, 0.0), null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void divideByNegativeDoubleZero() throws FScriptException {
        divisionFunction.apply(Arrays.asList((Object) 42, -0.0), null);
    }

    @Test
    public void divideTwoIntegers() {
        assertDivision(0.5, 1, 2);
        assertDivision(1.0, 2, 2);
        assertDivision(-1.0, -2, 2);
        assertDivision(-1.0, 2, -2);
        assertDivision(1.5, 3, 2);
    }

    @Test
    public void divideTwoDoubles() {
        assertDivision(0.5, 1.0, 2.0);
        assertDivision(1.0, 2.0, 2.0);
        assertDivision(-1.0, -2.0, 2.0);
        assertDivision(-1.0, 2.0, -2.0);
        assertDivision(1.5, 3.0, 2.0);
    }

    @Test
    public void divideIntegersWithDoubles() {
        assertDivision(0.5, 1, 2.0);
        assertDivision(1.0, 2, 2.0);
        assertDivision(-1.0, -2, 2.0);
        assertDivision(-1.0, 2, -2.0);
        assertDivision(1.5, 3, 2.0);
        assertDivision(0.5, 1.0, 2);
        assertDivision(1.0, 2.0, 2);
        assertDivision(-1.0, -2.0, 2);
        assertDivision(-1.0, 2.0, -2);
        assertDivision(1.5, 3.0, 2);
    }
}
