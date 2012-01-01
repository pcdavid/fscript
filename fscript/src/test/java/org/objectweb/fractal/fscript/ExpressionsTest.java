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

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.fractal.api.Component;

/**
 * This test cases tests expressions which do not involve Fractal components and nodes.
 * 
 * @author Pierre-Charles David
 */
public class ExpressionsTest extends FractalTestCase {
    private static FScriptEngine engine;

    @BeforeClass
    public static void setUp() throws Exception {
        Component fscript = FScript.newEngine();
        engine = FScript.getFScriptEngine(fscript);
    }

    private Object eval(String expr, Map<String, Object> locals) throws FScriptException {
        for (String var : engine.getGlobals()) {
            engine.setGlobalVariable(var, null);
        }
        if (locals != null) {
            for (String var : locals.keySet()) {
                engine.setGlobalVariable(var, locals.get(var));
            }
        }
        return engine.execute(expr);
    }

    public void assertNumericResult(double expected, String expr) throws FScriptException {
        assertEquals(new Double(expected), engine.execute(expr));
    }

    @Test
    public void litteralStrings() throws Exception {
        assertEquals("foo", engine.execute("\"foo\""));
        assertEquals("", engine.execute("\"\""));
        assertEquals(" ", engine.execute("\" \""));
        assertEquals("\"", engine.execute("\"\\\"\""));
        assertEquals("'", engine.execute("\"'\""));
        assertEquals("42", engine.execute("\"42\""));
        assertEquals("\n", engine.execute("\"\\n\""));
        assertEquals("\r", engine.execute("\"\\r\""));
        assertEquals("\t", engine.execute("\"\\t\""));
    }

    @Test
    public void lLitteralNumbers() throws Exception {
        assertNumericResult(1, "1");
        assertNumericResult(1, "1.0");
        assertNumericResult(0.0, "0");
        assertNumericResult(0.0, "0.0");
        assertNumericResult(-0.0, "-0.0");
        assertNumericResult(3.14159, "3.14159");
        assertNumericResult(10000, "10000");
        assertNumericResult(42, "00042");
    }

    @Test
    public void litteralAddition() throws Exception {
        assertNumericResult(0, "0+0");
        assertNumericResult(1.0, "1+0");
        assertNumericResult(2.0, "1+1");
        assertNumericResult(3.0, "1+1+1");
        assertNumericResult(1.5, "1+0.5");
    }

    @Test
    public void litteralSubstraction() throws Exception {
        assertNumericResult(0.0, "0-0");
        assertNumericResult(1.0, "1-0.0");
        assertNumericResult(0.0, "1-1");
        assertNumericResult(-1, "1-1-1");
        assertNumericResult(0.5, "1-0.5");
    }

    @Test
    public void litteralMultiplication() throws Exception {
        assertNumericResult(1.0, "1*1");
        assertNumericResult(2.0, "1*2");
        assertNumericResult(1.0, "2*0.5");
        assertNumericResult(42.0, "42*1");
        assertNumericResult(0.0, "42*0");
        assertNumericResult(24.0, "1*2*3*4");
    }

    @Test
    public void litteralDivision() throws Exception {
        assertNumericResult(1.0, "2 div 2");
        assertNumericResult(0.0, "0 div 2");
        assertNumericResult(2.0, "2 div 1");
        try {
            engine.execute("1 div 0");
            fail("Division by zero not detected.");
        } catch (FScriptException e) {
            // e => Transaction rolledback
            assertTrue(e.getCause() instanceof ScriptExecutionError); // cause: Error at
                                                                        // x:y,x:y
            assertTrue(e.getCause().getCause().getMessage().indexOf("Attempted division by zero") != -1); // cause:
            // DivByZero
            // OK, error catched.
        }
        try {
            engine.execute("0 div 0");
            fail("Division by zero not detected.");
        } catch (FScriptException e) {
            // e => Transaction rolledback
            assertTrue(e.getCause() instanceof ScriptExecutionError); // cause: Error at
                                                                        // x:y,x:y
            assertTrue(e.getCause().getCause().getMessage().indexOf("Attempted division by zero") != -1); // cause:
            // DivByZero
            // OK, error catched.
        }
    }

    @Test
    public void arithmeticPriorities() throws Exception {
        assertNumericResult(7.0, "1+2*3");
        assertNumericResult(7.0, "2*3+1");
        assertNumericResult(2.5, "1+3 div 2");
        assertNumericResult(2.5, "3 div 2+1");
        assertNumericResult(-5.0, "1-2*3");
        assertNumericResult(5.0, "2*3-1");
        assertNumericResult(0.5, "3 div 2-1");
    }

    @Test
    public void definedVariableLookup() throws Exception {
        Map<String, Object> local = new HashMap<String, Object>();
        local.put("foo", "fooValue");
        assertEquals("fooValue", eval("$foo", local));
        assertNull(eval("$foo", null));
        assertNull(eval("$foo", new HashMap<String, Object>()));
    }

    @Test
    public void undefinedVariableLookup() throws Exception {
        Map<String, Object> local = new HashMap<String, Object>();
        local.put("foo", "fooValue");
        assertNull(eval("$bar", local));
        assertNull(eval("$bar", null));
        assertNull(eval("$bar", new HashMap<String, Object>()));
    }
}
