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
import org.objectweb.fractal.fscript.procedures.ConcatFunction;

public class ConcatFunctionsTest {
    private NativeProcedure concatFunction;

    @Before
    public void setUp() {
        concatFunction = new ConcatFunction();
    }

    private String concat(Object... args) throws FScriptException {
        Object result = concatFunction.apply(Arrays.asList(args), null);
        assertTrue(result instanceof String);
        return (String) result;
    }

    @Test
    public void concatStrings() throws FScriptException {
        assertEquals("foo", concat("", "foo"));
        assertEquals("foo", concat("", "foo", ""));
        assertEquals("foobar", concat("foo", "bar"));
        assertEquals("foo", concat("foo", ""));
    }
}
