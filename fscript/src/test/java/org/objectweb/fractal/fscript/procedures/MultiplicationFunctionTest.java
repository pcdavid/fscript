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
import org.objectweb.fractal.fscript.procedures.MultiplicationFunction;

public class MultiplicationFunctionTest {
    // The precision used by <code>assertProduct()</code>.
    private static final double PRECISION = 0.0000001;

    private NativeProcedure multiplicationFunction;

    @Before
    public void setUp() {
        multiplicationFunction = new MultiplicationFunction();
    }

    private double mult(Object[] args) {
        try {
            return (Double) multiplicationFunction.apply(Arrays.asList(args), null);
        } catch (FScriptException e) {
            fail(e.toString());
            return Double.NaN; // Keep the compiler happy.
        }
    }

    private void assertProduct(double expected, Object... args) {
        assertEquals(expected, mult(args), PRECISION);
    }

    @Test
    public void multPositiveIntegers() {
        assertProduct(2, 1, 2);
        assertProduct(4, 2, 2);
    }
}
