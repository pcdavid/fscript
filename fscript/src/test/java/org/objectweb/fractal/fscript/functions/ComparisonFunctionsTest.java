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

public class ComparisonFunctionsTest {
    private Procedure gt;

    private Procedure gte;

    private Procedure lt;

    private Procedure lte;

    @Before
    public void setUp() {
        gt = new GreaterThanFunction();
        gte = new GreaterThanOrEqualFunction();
        lt = new LessThanFunction();
        lte = new LessThanOrEqualFunction();
    }

    private boolean call(Procedure p, Object first, Object second) {
        try {
            Object result = p.apply(new Object[] { first, second }, null);
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

    @Test(expected = ScriptExecutionError.class)
    public void noArgGt() throws FScriptException {
        gt.apply(null, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void noArgGte() throws FScriptException {
        gte.apply(null, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void noArgLt() throws FScriptException {
        lt.apply(null, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void noArgLte() throws FScriptException {
        lte.apply(null, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void zeroArgGt() throws FScriptException {
        gt.apply(new Object[0], null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void zeroArgGte() throws FScriptException {
        gte.apply(new Object[0], null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void zeroArgLt() throws FScriptException {
        lt.apply(new Object[0], null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void zeroArgLte() throws FScriptException {
        lte.apply(new Object[0], null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void oneArgGt() throws FScriptException {
        gt.apply(new Object[] { 1.0 }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void oneArgGte() throws FScriptException {
        gte.apply(new Object[] { 1.0 }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void oneArgLt() throws FScriptException {
        lt.apply(new Object[] { 1.0 }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void oneArgLte() throws FScriptException {
        lte.apply(new Object[] { 1.0 }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void threeArgGt() throws FScriptException {
        gt.apply(new Object[] { 1.0, 2.0, 3.0 }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void threeArgGte() throws FScriptException {
        gte.apply(new Object[] { 1.0, 2.0, 3.0 }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void threeArgLt() throws FScriptException {
        lt.apply(new Object[] { 1.0, 2.0, 3.0 }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void threeArgLte() throws FScriptException {
        lte.apply(new Object[] { 1.0, 2.0, 3.0 }, null);
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
