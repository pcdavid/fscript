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
import org.objectweb.fractal.fscript.procedures.AdditionFunction;

public class AdditionFunctionTest {
    // The precision used by <code>assertSum()</code>.
    private static final double PRECISION = 0.0000001;

    private NativeProcedure addFunction;

    @Before
    public void setUp() throws Exception {
        addFunction = new AdditionFunction();
    }

    private double add(Object[] args) {
        try {
            return (Double) addFunction.apply(Arrays.asList(args), null);
        } catch (FScriptException e) {
            fail(e.toString());
            return Double.NaN; // Keep the compiler happy.
        }
    }

    private void assertSum(double expected, Object... args) {
        assertEquals(expected, add(args), PRECISION);
    }

    @Test
    public void addPositiveIntegers() {
        assertSum(2, 1, 1);
        assertSum(1 + 2, 1, 2);
    }
}
