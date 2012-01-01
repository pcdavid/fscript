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

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.procedures.StartsWithFunction;

public class StartsWithFunctionTest {
    private NativeProcedure startsWithFunction;

    @Before
    public void setUp() {
        startsWithFunction = new StartsWithFunction();
    }

    private boolean startsWith(Object str, Object prefix) {
        Object result;
        try {
            result = startsWithFunction.apply(asList(str, prefix), null);
            assertTrue(result instanceof Boolean);
            return ((Boolean) result).booleanValue();
        } catch (FScriptException e) {
            throw new AssertionError(e);
        }
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
