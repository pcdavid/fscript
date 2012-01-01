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

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.procedures.FalseFunction;
import org.objectweb.fractal.fscript.procedures.NotFunction;
import org.objectweb.fractal.fscript.procedures.TrueFunction;

public class BooleanFunctionsTest {
    private NativeProcedure trueFunction;

    private NativeProcedure falseFunction;

    private NativeProcedure notFunction;

    @Before
    public void setUp() {
        trueFunction = new TrueFunction();
        falseFunction = new FalseFunction();
        notFunction = new NotFunction();
    }

    @Test
    public void callTrueWithZeroArgs() throws FScriptException {
        Object result = trueFunction.apply(Collections.emptyList(), null);
        assertTrue(result instanceof Boolean);
        assertTrue(Boolean.valueOf((Boolean) result));
    }

    @Test
    public void callFalseWithZeroArgs() throws FScriptException {
        Object result = falseFunction.apply(Collections.emptyList(), null);
        assertTrue(result instanceof Boolean);
        assertTrue(!Boolean.valueOf((Boolean) result));
    }

    @Test
    public void notTrue() throws FScriptException {
        // Argument as primitive
        Object result = notFunction.apply(Arrays.asList((Object) true), null);
        assertTrue(result instanceof Boolean);
        assertTrue(!Boolean.valueOf((Boolean) result));
        // Argument as object (Boolean)
        result = notFunction
                .apply(Collections.singletonList((Object) Boolean.TRUE), null);
        assertTrue(result instanceof Boolean);
        assertTrue(!Boolean.valueOf((Boolean) result));
    }

    @Test
    public void notFalse() throws FScriptException {
        // Argument as primitive
        Object result = notFunction
                .apply(Collections.singletonList((Object) false), null);
        assertTrue(result instanceof Boolean);
        assertTrue(Boolean.valueOf((Boolean) result));
        // Argument as object (Boolean)
        result = notFunction.apply(Collections.singletonList((Object) Boolean.FALSE),
                null);
        assertTrue(result instanceof Boolean);
        assertTrue(Boolean.valueOf((Boolean) result));
    }

}
