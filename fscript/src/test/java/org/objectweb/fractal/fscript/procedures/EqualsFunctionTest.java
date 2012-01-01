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
import org.objectweb.fractal.fscript.procedures.EqualsFunction;

public class EqualsFunctionTest {
    private NativeProcedure equalsFunction;

    @Before
    public void setUp() {
        equalsFunction = new EqualsFunction();
    }

    private void assertMyEquals(Object first, Object second) {
        assertResult(true, first, second);
        assertResult(true, second, first);
    }

    private void assertNotMyEquals(Object first, Object second) {
        assertResult(false, first, second);
        assertResult(false, second, first);
    }

    private void assertResult(boolean expected, Object first, Object second) {
        Object result;
        try {
            result = equalsFunction.apply(Arrays.asList(first, second), null);
        } catch (FScriptException e) {
            throw new AssertionError(e);
        }
        if (result == null) {
            fail("Function " + equalsFunction.getName() + " returned null.");
        } else if (!(result instanceof Boolean)) {
            fail("Function " + equalsFunction.getName() + " returned a "
                    + result.getClass() + " instead of a boolean.");
        } else {
            assertEquals(expected, (Boolean) result);
        }
    }

    @Test
    public void twoBooleans() {
        assertNotMyEquals(true, false);
        assertMyEquals(true, true);
        assertMyEquals(false, false);
    }

    @Test
    public void twoStrings() {
        assertNotMyEquals("", "x");
        assertNotMyEquals("foo", "bar");
        assertMyEquals("", "");
        assertMyEquals("foo", "foo");
    }

    @Test
    public void twoIntegers() {
        assertNotMyEquals(0, 1);
        assertNotMyEquals(-1, 1);
        assertNotMyEquals(7, 42);
        assertMyEquals(0, 0);
        assertMyEquals(1, 1);
        assertMyEquals(-0, -0);
        assertMyEquals(0, -0);
        assertMyEquals(-1, -1);
    }

    @Test
    public void twoDoubles() {
        assertNotMyEquals(0.0, 1.0);
        assertNotMyEquals(0.0, -0.0); // Double distinguishes +0.0 from -0.0
        assertNotMyEquals(-1.0, 1.0);
        assertNotMyEquals(7.0, 42.0);
        assertMyEquals(0.0, 0.0);
        assertMyEquals(1.0, 1.0);
        assertMyEquals(-0.0, -0.0);
        assertMyEquals(-1.0, -1.0);
    }

}
