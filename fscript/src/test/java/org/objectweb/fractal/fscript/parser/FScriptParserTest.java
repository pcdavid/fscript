/*
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
package org.objectweb.fractal.fscript.parser;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.FScriptEngine;
import org.objectweb.fractal.fscript.FullScriptTest;
import org.objectweb.fractal.fscript.InvalidScriptException;
import org.objectweb.fractal.fscript.ast.UserProcedure;

public class FScriptParserTest {
    private IFScriptParser parser;

    @Before
    public void setUp() {
        parser = new FScriptParserImpl();
    }

    private Reader fixture(final String name) {
        final InputStream stream = FullScriptTest.class.getResourceAsStream(name);
        return new InputStreamReader(stream);
    }

    @Test
    public void parseActionDefinition() throws InvalidScriptException {
        List<UserProcedure> defs = parser.parseDefinitions(fixture("replace-scheduler.fscript"));
        assertNotNull(defs);
        assertEquals(1, defs.size());
        UserProcedure def = defs.get(0);
        assertNotNull(def);
        assertEquals("replace-scheduler", def.getName());
        List<String> params = def.getParameters();
        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("comanche", params.get(0));
    }

    @Test
    public void parseStandarLibrary() throws InvalidScriptException {
        InputStream stdlib = FScriptEngine.class.getResourceAsStream("stdlib.fscript");
        List<UserProcedure> defs = parser.parseDefinitions(new InputStreamReader(stdlib));
        assertNotNull(defs);
    }

    private static final String ABS = "function abs(n) "
            + " { if ($n > 0) { return $n; } else { return -$n; } }";

    @Test
    public void parseAbsFunction() throws InvalidScriptException {
        List<UserProcedure> defs = parser.parseDefinitions(new StringReader(ABS));
        assertNotNull(defs);
        assertEquals(1, defs.size());
        UserProcedure abs = defs.get(0);
        assertNotNull(abs);
        assertEquals("abs", abs.getName());
        assertNotNull(abs.getParameters());
        assertEquals(1, abs.getParameters().size());
        assertEquals("n", abs.getParameters().get(0));
    }

    private static final String EMPTY = "action empty(composite) {\n"
            + "  for child : $composite/child::* {\n" + "    remove($composite, $child);\n"
            + "  }\n" + "}";

    @Test
    public void parserEmptyAction() throws InvalidScriptException {
        List<UserProcedure> defs = parser.parseDefinitions(new StringReader(EMPTY));
        assertNotNull(defs);
        assertEquals(1, defs.size());
    }

}
