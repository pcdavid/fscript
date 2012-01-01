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

import static org.junit.Assert.*;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.Procedure;

public class AdditionFunctionTest {
    // The precision used by <code>assertSum()</code>.
    private static final double PRECISION = 0.0000001;

    private Procedure addFunction;

    @Before
    public void setUp() throws Exception {
        addFunction = new AdditionFunction();
    }

    private double add(Object[] args) {
        try {
            return (Double) addFunction.apply(args, null);
        } catch (FScriptException e) {
            fail(e.toString());
            return Double.NaN; // Keep the compiler happy.
        }
    }

    private void assertSum(double expected, Object... args) {
        assertEquals(expected, add(args), PRECISION);
    }

    @Test
    public void noArguments() {
        assertSum(0.0, new Object[0]);
    }

    @Test
    public void nullArgumentGivesNaN() {
        assertSum(Double.NaN, new Object[] { null });
        assertSum(Double.NaN, 1, null);
        assertSum(Double.NaN, 1, 2, 3, null, 4, 5);
        assertSum(Double.NaN, 1, 2, 3, null, "not a number at all");
        assertSum(Double.NaN, 1, 2, 3, null, new Object());
    }

    @Test
    public void nonNumericArgumentGivesNaN() {
        assertSum(Double.NaN, new Object());
        assertSum(Double.NaN, 1, 2, 3, Object.class, 4, 5);
    }

    @Test
    public void addPositiveIntegers() {
        assertSum(2, 1, 1);
        assertSum(4, 1, 1, 1, 1);
        assertSum(1 + 2 + 3 + 4, 1, 2, 3, 4);

    }
}
