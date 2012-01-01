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
package org.objectweb.fractal.fscript;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.fractal.api.Component;

/**
 * Tests for functions which do not involve Fractal.
 * 
 * @author Pierre-Charles David
 */
public class BasicFunctionsTest extends FractalTestCase {
    private static FScriptEngine engine;

    @BeforeClass
    public static void setUp() throws Exception {
        Component fscript = FScript.newEngine();
        engine = FScript.getFScriptEngine(fscript);
    }

    @Test
    public void trueFunction() throws Exception {
        assertTrue(((Boolean) engine.execute("true()")).booleanValue());
    }

    @Test
    public void falseFunction() throws Exception {
        assertFalse(((Boolean) engine.execute("false()")).booleanValue());
    }

    @Test
    public void concatFunction() throws Exception {
        assertEquals("foobar", engine.execute("concat(\"foo\", \"bar\")"));
        assertEquals("foobarbaz", engine.execute("concat(\"foo\", concat(\"bar\", \"baz\"))"));
        assertEquals("foo", engine.execute("concat(\"foo\", \"\")"));
        assertEquals("foo", engine.execute("concat(\"\", \"foo\")"));
    }

    @Test
    public void differentFunction() throws Exception {
        assertEquals(Boolean.TRUE, engine.execute("1 != 2"));
        assertEquals(Boolean.FALSE, engine.execute("1 != 1"));
        assertEquals(Boolean.TRUE, engine.execute("1 != \"one\""));
        assertEquals(Boolean.TRUE, engine.execute("\"one\" != 1"));
        assertEquals(Boolean.TRUE, engine.execute("1 != true()"));
        assertEquals(Boolean.TRUE, engine.execute("true() != 1"));
        assertEquals(Boolean.FALSE, engine.execute("\"one\" != \"one\""));
        assertEquals(Boolean.FALSE, engine.execute("true() != true()"));
        assertEquals(Boolean.FALSE, engine.execute("false() != false()"));
    }

    @Test
    public void endsWithFunction() throws Exception {
        assertEquals(Boolean.TRUE, engine.execute("ends-with(\"foobar\", \"bar\")"));
        assertEquals(Boolean.FALSE, engine.execute("ends-with(\"foobar\", \"foo\")"));
        assertEquals(Boolean.FALSE, engine.execute("ends-with(\"\", \"bar\")"));
    }

    @Test
    public void equalsFunction() throws Exception {
        assertEquals(Boolean.FALSE, engine.execute("1 == 2"));
        assertEquals(Boolean.TRUE, engine.execute("1 == 1"));
        assertEquals(Boolean.FALSE, engine.execute("1 == \"one\""));
        assertEquals(Boolean.FALSE, engine.execute("\"one\" == 1"));
        assertEquals(Boolean.FALSE, engine.execute("1 == true()"));
        assertEquals(Boolean.FALSE, engine.execute("true() == 1"));
        assertEquals(Boolean.TRUE, engine.execute("\"one\" == \"one\""));
        assertEquals(Boolean.TRUE, engine.execute("true() == true()"));
        assertEquals(Boolean.TRUE, engine.execute("false() == false()"));
    }

    @Test
    public void greaterThanFunction() throws Exception {
        assertEquals(Boolean.TRUE, engine.execute("1 > 0"));
        assertEquals(Boolean.TRUE, engine.execute("1 > -1"));
        assertEquals(Boolean.TRUE, engine.execute("0 > -1"));
        assertEquals(Boolean.FALSE, engine.execute("0 > 1"));
        assertEquals(Boolean.FALSE, engine.execute("0 > 0"));
        assertEquals(Boolean.FALSE, engine.execute("1 > 1"));
        assertEquals(Boolean.TRUE, engine.execute("0 > -1"));
    }

    @Test
    public void greaterThanOrEqualFunction() throws Exception {
        assertEquals(Boolean.TRUE, engine.execute("1 >= 0"));
        assertEquals(Boolean.TRUE, engine.execute("1 >= -1"));
        assertEquals(Boolean.TRUE, engine.execute("0 >= -1"));
        assertEquals(Boolean.FALSE, engine.execute("0 >= 1"));
        assertEquals(Boolean.TRUE, engine.execute("0 >= 0"));
        assertEquals(Boolean.TRUE, engine.execute("1 >= 1"));
        assertEquals(Boolean.TRUE, engine.execute("0 >= -1"));
    }

    @Test
    public void lessThanFunction() throws Exception {
        assertEquals(Boolean.FALSE, engine.execute("1 < 0"));
        assertEquals(Boolean.FALSE, engine.execute("1 < -1"));
        assertEquals(Boolean.FALSE, engine.execute("0 < -1"));
        assertEquals(Boolean.TRUE, engine.execute("0 < 1"));
        assertEquals(Boolean.FALSE, engine.execute("0 < 0"));
        assertEquals(Boolean.FALSE, engine.execute("1 < 1"));
        assertEquals(Boolean.FALSE, engine.execute("0 < -1"));
    }

    @Test
    public void lessThanOrEqualFunction() throws Exception {
        assertEquals(Boolean.FALSE, engine.execute("1 <= 0"));
        assertEquals(Boolean.FALSE, engine.execute("1 <= -1"));
        assertEquals(Boolean.FALSE, engine.execute("0 <= -1"));
        assertEquals(Boolean.TRUE, engine.execute("0 <= 1"));
        assertEquals(Boolean.TRUE, engine.execute("0 <= 0"));
        assertEquals(Boolean.TRUE, engine.execute("1 <= 1"));
        assertEquals(Boolean.FALSE, engine.execute("0 <= -1"));
    }

    @Test
    public void notFunction() throws Exception {
        assertEquals(Boolean.TRUE, engine.execute("not(false())"));
        assertEquals(Boolean.FALSE, engine.execute("not(true())"));
        assertEquals(Boolean.FALSE, engine.execute("not(1)"));
        assertEquals(Boolean.FALSE, engine.execute("not(\"foo\")"));
    }

    @Test
    public void sizeFunction() throws Exception {
        assertEquals(0.0, engine.execute("size(\"\")"));
        assertEquals(3.0, engine.execute("size(\"foo\")"));
        assertEquals(1.0, engine.execute("size(\"\\n\")"));
        assertEquals(0.0, engine.execute("size(0)"));
        assertEquals(1.0, engine.execute("size(1)"));
        assertEquals(-1.0, engine.execute("size(-1)"));
        assertEquals(1.5, engine.execute("size(1.5)"));
    }

    @Test
    public void startsWithFunction() throws Exception {
        assertEquals(Boolean.TRUE, engine.execute("starts-with('foobar', 'foo')"));
        assertEquals(Boolean.FALSE, engine.execute("starts-with('foobar', 'bar')"));
        assertEquals(Boolean.FALSE, engine.execute("starts-with('', 'bar')"));
    }
}
