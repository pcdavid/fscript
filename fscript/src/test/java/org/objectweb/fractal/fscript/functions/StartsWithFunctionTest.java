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

public class StartsWithFunctionTest {
    private Procedure startsWithFunction;

    @Before
    public void setUp() {
        startsWithFunction = new StartsWithFunction();
    }

    private boolean startsWith(Object str, Object prefix) {
        Object result;
        try {
            result = startsWithFunction.apply(new Object[] { str, prefix }, null);
            assertTrue(result instanceof Boolean);
            return ((Boolean) result).booleanValue();
        } catch (FScriptException e) {
            throw new AssertionError(e);
        }
    }

    @Test(expected = ScriptExecutionError.class)
    public void noArgs() throws FScriptException {
        startsWithFunction.apply(null, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void zeroArgs() throws FScriptException {
        startsWithFunction.apply(new Object[0], null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void oneArg() throws FScriptException {
        startsWithFunction.apply(new Object[] { "foo" }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void threeArgs() throws FScriptException {
        startsWithFunction.apply(new Object[] { "foo", "bar", "baz" }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void manyArgs() throws FScriptException {
        startsWithFunction.apply(
                new Object[] { "foo", "bar", "baz", "qux", "quux", "quuux" }, null);
    }

    @Test
    public void nullString() {
        assertTrue(startsWith(null, ""));
        assertTrue(startsWith(null, null));
        assertFalse(startsWith(null, "foo"));
    }

    @Test
    public void nullPrefix() {
        assertTrue(startsWith("foo", null));
    }

    @Test
    public void emptyPrefix() {
        assertTrue(startsWith("foo", ""));
        assertTrue(startsWith("", ""));
    }

    @Test
    public void strings() {
        String[] values = { "foo", "ends-with", "FScript", "42",
                "This is a complete sentence." };
        for (String str : values) {
            for (int i = 0; i < str.length(); i++) {
                String prefix = str.substring(0, i);
                assertTrue(startsWith(str, prefix));
            }
            assertFalse(startsWith(str, " " + str));
            assertFalse(startsWith(str, "x" + str));
            assertFalse(startsWith(str, str + str));
        }
    }
    
    @Test
    public void caseSensitiveness() {
        assertFalse(startsWith("foo", "F"));
        assertFalse(startsWith("foo", "FO"));
        assertFalse(startsWith("foo", "fOo"));
    }
}
