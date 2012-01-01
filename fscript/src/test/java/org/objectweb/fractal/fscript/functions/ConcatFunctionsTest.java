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

public class ConcatFunctionsTest {
    private Procedure concatFunction;
    
    @Before
    public void setUp() {
        concatFunction = new ConcatFunction();
    }
    
    private String concat(Object... args) throws FScriptException {
        Object result = concatFunction.apply(args, null);
        assertTrue(result instanceof String);
        return (String) result;
    }
    
    @Test
    public void concatNoArgs() throws FScriptException {
        assertEquals("", concatFunction.apply(null, null));
        assertEquals("", concatFunction.apply(new Object[0], null));
    }
    
    @Test
    public void concatStrings() throws FScriptException {
        assertEquals("foo", concat("foo"));
        assertEquals("foo", concat("foo", ""));
        assertEquals("foo", concat("", "foo"));
        assertEquals("foo", concat("", "foo", ""));
        assertEquals("foobar", concat("foo", "bar"));
        assertEquals("foobar", concat("foo", "", "bar"));
        assertEquals("foobar", concat("", "foo", "", "bar", ""));
        assertEquals("foobarbaz", concat("foo", "bar", "baz"));
    }
    
    @Test
    public void concatStringsAndNulls() throws FScriptException {
        assertEquals("foo", concat(null, "foo"));
        assertEquals("foo", concat("foo", null));
        assertEquals("foobar", concat("foo", null, "bar"));
        assertEquals("foobar", concat(null, "foo", null, null, "bar", null));
    }
}
