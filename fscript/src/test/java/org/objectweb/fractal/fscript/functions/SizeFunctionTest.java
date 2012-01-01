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

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.Procedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;

public class SizeFunctionTest {
    private Procedure sizeFunction;
    
    @Before
    public void setUp() {
        sizeFunction = new SizeFunction();
    }
    
    @Test(expected = ScriptExecutionError.class)
    public void failWithNoArgs() throws FScriptException {
        sizeFunction.apply(null, null);
        sizeFunction.apply(new Object[0], null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void failWithTwoArg() throws FScriptException {
        sizeFunction.apply(new Object[] { "foo", "bar" }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void failWithThreeArgs() throws FScriptException {
        sizeFunction.apply(new Object[] { "foo", "bar", "baz" }, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void failWithFourArgs() throws FScriptException {
        sizeFunction.apply(new Object[] { "foo", "bar", "baz", "qux" }, null);
    }
}
