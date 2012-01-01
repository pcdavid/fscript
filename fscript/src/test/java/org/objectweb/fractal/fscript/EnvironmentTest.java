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

public class EnvironmentTest {
    private FScriptInterpreter fscript;

    private Environment env;

    @Before
    public void setUp() throws Exception {
        fscript = new FScriptInterpreter();
        env = fscript.getEnvironment();
    }

    @Test
    public void nonExistingVariable() throws Exception {
        assertNull(env.getVariable("foo"));
    }

    @Test
    public void settingVariable() throws Exception {
        env.setVariable("foo", "fooValue");
        assertEquals("fooValue", env.getVariable("foo"));
    }

    @Test
    public void returnValue() throws Exception {
        env.setReturnValue("result");
        assertEquals("result", env.getReturnValue());
    }

    @Test
    public void standardProcedure() throws Exception {
        assertNotNull(env.getProcedure("+"));
        assertNotNull(env.getProcedure("stop"));
        assertNotNull(env.getProcedure("bind"));
        assertNull(env.getProcedure("foo"));
    }

    @Test
    public void standardAxes() throws Exception {
        assertNotNull(env.getAxis("child"));
        assertNotNull(env.getAxis("child-or-self"));
        assertNotNull(env.getAxis("sibling"));
        assertNotNull(env.getAxis("ancestor-or-self"));
        assertNull(env.getAxis("foo"));
    }

    @Test
    public void shadowing() throws Exception {
        Environment local = new Environment(env, null);
        env.setVariable("foo", "fooValue");
        assertEquals("fooValue", env.getVariable("foo"));
        assertEquals("fooValue", local.getVariable("foo"));
        local.setVariable("foo", "localFooValue");
        assertEquals("fooValue", env.getVariable("foo"));
        assertEquals("localFooValue", local.getVariable("foo"));
        local.setVariable("bar", "barValue");
        assertEquals("barValue", local.getVariable("bar"));
        assertNull(env.getVariable("bar"));
    }
}
