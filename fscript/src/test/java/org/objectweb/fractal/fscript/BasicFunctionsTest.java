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
package org.objectweb.fractal.fscript;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for functions which do not involve Fractal.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class BasicFunctionsTest {
    private FScriptInterpreter fscript;

    @Before
    public void setUp() {
        fscript = new FScriptInterpreter();
    }

    @Test
    public void trueFunction() throws Exception {
        assertTrue(((Boolean) fscript.evaluate("true()", null)).booleanValue());
    }

    @Test
    public void falseFunction() throws Exception {
        assertFalse(((Boolean) fscript.evaluate("false()", null)).booleanValue());
    }

    @Test
    public void concatFunction() throws Exception {
        assertEquals("foobar", fscript.evaluate("concat(\"foo\", \"bar\")", null));
        assertEquals("foobarbaz", fscript.evaluate("concat(\"foo\", \"bar\", \"baz\")",
                null));
        assertEquals("foo", fscript.evaluate("concat(\"foo\", \"\")", null));
        assertEquals("foo", fscript.evaluate("concat(\"\", \"foo\")", null));
    }

    @Test
    public void differentFunction() throws Exception {
        assertEquals(Boolean.TRUE, fscript.evaluate("1 != 2", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("1 != 1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("1 != \"one\"", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("\"one\" != 1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("1 != true()", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("true() != 1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("\"one\" != \"one\"", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("true() != true()", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("false() != false()", null));
    }

    @Test
    public void endsWithFunction() throws Exception {
        assertEquals(Boolean.TRUE, fscript.evaluate("ends-with(\"foobar\", \"bar\")",
                null));
        assertEquals(Boolean.FALSE, fscript.evaluate("ends-with(\"foobar\", \"foo\")",
                null));
        assertEquals(Boolean.FALSE, fscript.evaluate("ends-with(\"\", \"bar\")", null));
    }

    @Test
    public void equalsFunction() throws Exception {
        assertEquals(Boolean.FALSE, fscript.evaluate("1 == 2", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("1 == 1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("1 == \"one\"", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("\"one\" == 1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("1 == true()", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("true() == 1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("\"one\" == \"one\"", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("true() == true()", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("false() == false()", null));
    }

    @Test
    public void greaterThanFunction() throws Exception {
        assertEquals(Boolean.TRUE, fscript.evaluate("1 > 0", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("1 > -1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("0 > -1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("0 > 1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("0 > 0", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("1 > 1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("0 > -1", null));
    }

    @Test
    public void greaterThanOrEqualFunction() throws Exception {
        assertEquals(Boolean.TRUE, fscript.evaluate("1 >= 0", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("1 >= -1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("0 >= -1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("0 >= 1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("0 >= 0", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("1 >= 1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("0 >= -1", null));
    }

    @Test
    public void lessThanFunction() throws Exception {
        assertEquals(Boolean.FALSE, fscript.evaluate("1 < 0", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("1 < -1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("0 < -1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("0 < 1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("0 < 0", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("1 < 1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("0 < -1", null));
    }

    @Test
    public void lessThanOrEqualFunction() throws Exception {
        assertEquals(Boolean.FALSE, fscript.evaluate("1 <= 0", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("1 <= -1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("0 <= -1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("0 <= 1", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("0 <= 0", null));
        assertEquals(Boolean.TRUE, fscript.evaluate("1 <= 1", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("0 <= -1", null));
    }

    @Test
    public void notFunction() throws Exception {
        assertEquals(Boolean.TRUE, fscript.evaluate("not(false())", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("not(true())", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("not(1)", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("not(\"foo\")", null));
    }

    @Test
    public void sizeFunction() throws Exception {
        assertEquals(0.0, fscript.evaluate("size(\"\")", null));
        assertEquals(3.0, fscript.evaluate("size(\"foo\")", null));
        assertEquals(1.0, fscript.evaluate("size(\"\\n\")", null));
        assertEquals(0.0, fscript.evaluate("size(0)", null));
        assertEquals(1.0, fscript.evaluate("size(1)", null));
        assertEquals(-1.0, fscript.evaluate("size(-1)", null));
        assertEquals(1.5, fscript.evaluate("size(1.5)", null));
    }

    @Test
    public void startsWithFunction() throws Exception {
        assertEquals(Boolean.TRUE, fscript.evaluate("starts-with('foobar', 'foo')", null));
        assertEquals(Boolean.FALSE, fscript
                .evaluate("starts-with('foobar', 'bar')", null));
        assertEquals(Boolean.FALSE, fscript.evaluate("starts-with('', 'bar')", null));
    }
}
