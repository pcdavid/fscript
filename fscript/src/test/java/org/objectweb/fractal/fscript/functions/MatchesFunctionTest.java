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

public class MatchesFunctionTest {
    private Procedure matchesFunction;

    @Before
    public void setUp() {
        matchesFunction = new MatchesFunction();
    }
    
    private boolean matches(String str, String regexp) {
        Object result;
        try {
            result = matchesFunction.apply(new Object[] { str, regexp }, null);
            assertTrue(result instanceof Boolean);
            return (Boolean) result;
        } catch (FScriptException e) {
            fail(e.getMessage());
            return false; // Keep the compiler happy
        }
    }

    @Test(expected = ScriptExecutionError.class)
    public void failWithNoArgs() throws FScriptException {
        matchesFunction.apply(null, null);
        matchesFunction.apply(new Object[0], null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void failWithOneArg() throws FScriptException {
        matchesFunction.apply(new Object[] { "foo" }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void failWithThreeArgs() throws FScriptException {
        matchesFunction.apply(new Object[] { "foo", "bar", "baz" }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void failWithFourArgs() throws FScriptException {
        matchesFunction.apply(new Object[] { "foo", "bar", "baz", "qux" }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void failWithFiveArgs() throws FScriptException {
        matchesFunction.apply(new Object[] { "foo", "bar", "baz", "qux", "quux" }, null);
    }
    
    @Test
    public void catchAllRegexp() {
        assertTrue(matches("", ".*"));
        assertTrue(matches("foo", ".*"));
        assertTrue(matches(" ", ".*"));
        assertTrue(matches("\t", ".*"));
        assertFalse(matches("foo\nbar\n", ".*"));
    }
    
    @Test
    public void matchNewline() {
        assertTrue(matches("foo\nbar", ".*\n.*"));
    }
    
    @Test
    public void matchesOneself() {
        String[] str = new String[] { "", "foo", "bar", "Mary had a little lamb." };
        for (String s : str) {
            assertTrue(matches(s, s));
        }
    }
    
    @Test
    public void caseSensitiveness() {
        assertFalse(matches("foo", "Foo"));
    }
    
    @Test
    public void complexRexgeps() {
        assertTrue(matches("foo", "[fg]oo"));
        assertTrue(matches("foo", "(foo|bar)"));
        assertTrue(matches("foo", "([fgh]o+|bar)"));
    }
}
