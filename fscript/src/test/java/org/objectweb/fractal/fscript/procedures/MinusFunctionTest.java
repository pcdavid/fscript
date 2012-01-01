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

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.procedures.MinusFunction;

public class MinusFunctionTest {
    private static final double PRECISION = 0.0;

    private NativeProcedure minusFunction;

    @Before
    public void setUp() {
        minusFunction = new MinusFunction();
    }

    private double negate(Object obj) {
        try {
            Object result = minusFunction.apply(Collections.singletonList(obj), null);
            assertTrue(result instanceof Number);
            return ((Number) result).doubleValue();
        } catch (FScriptException e) {
            throw new AssertionError(e);
        }
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
