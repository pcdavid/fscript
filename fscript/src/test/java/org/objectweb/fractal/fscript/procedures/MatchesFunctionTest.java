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
import org.objectweb.fractal.fscript.procedures.MatchesFunction;

public class MatchesFunctionTest {
    private NativeProcedure matchesFunction;

    @Before
    public void setUp() {
        matchesFunction = new MatchesFunction();
    }

    private boolean matches(String str, String regexp) {
        Object result;
        try {
            result = matchesFunction.apply(Arrays.asList((Object) str, regexp), null);
            assertTrue(result instanceof Boolean);
            return (Boolean) result;
        } catch (FScriptException e) {
            fail(e.getMessage());
            return false; // Keep the compiler happy
        }
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
