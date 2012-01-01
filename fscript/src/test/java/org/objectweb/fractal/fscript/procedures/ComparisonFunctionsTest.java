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
import org.objectweb.fractal.fscript.procedures.GreaterThanFunction;
import org.objectweb.fractal.fscript.procedures.GreaterThanOrEqualFunction;
import org.objectweb.fractal.fscript.procedures.LessThanFunction;
import org.objectweb.fractal.fscript.procedures.LessThanOrEqualFunction;

public class ComparisonFunctionsTest {
    private NativeProcedure gt;

    private NativeProcedure gte;

    private NativeProcedure lt;

    private NativeProcedure lte;

    @Before
    public void setUp() {
        gt = new GreaterThanFunction();
        gte = new GreaterThanOrEqualFunction();
        lt = new LessThanFunction();
        lte = new LessThanOrEqualFunction();
    }

    private boolean call(NativeProcedure p, Object first, Object second) {
        try {
            Object result = p.apply(Arrays.asList(first, second), null);
            assertTrue(result instanceof Boolean);
            return ((Boolean) result).booleanValue();
        } catch (FScriptException e) {
            throw new AssertionError(e);
        }
    }

    private void assertOrder(Object low, Object high) {
        assertTrue(call(lte, low, high));
        assertTrue(call(gte, high, low));
    }

    private void assertStrictOrder(Object low, Object high) {
        assertFalse(call(lt, low, low));
        assertFalse(call(lt, high, high));
        //
        assertFalse(call(gt, low, low));
        assertFalse(call(gt, high, high));
        //
        assertTrue(call(lt, low, high));
        assertTrue(call(gt, high, low));
        //
        assertFalse(call(lt, high, low));
        assertFalse(call(gt, low, high));
        //
        assertTrue(call(lte, low, high));
        assertTrue(call(gte, high, low));
        //
        assertFalse(call(lte, high, low));
        assertFalse(call(gte, low, high));
    }

    @Test
    public void compareIntegers() {
        assertOrder(0, 0);
        assertOrder(-1, -1);
        assertOrder(1, 1);
        assertStrictOrder(1, 2);
        assertStrictOrder(-1, 0);
        assertStrictOrder(-1, 1);
        assertStrictOrder(0, 1);
    }

    @Test
    public void compareDoubles() {
        assertOrder(0.0, 0.0);
        assertOrder(-1.0, -1.0);
        assertOrder(1.0, 1.0);
        assertStrictOrder(1.0, 2.0);
        assertStrictOrder(-1.0, 0.0);
        assertStrictOrder(-1.0, 1.0);
        assertStrictOrder(0.0, 1.0);
    }
}
