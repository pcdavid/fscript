/*
 * Copyright (c) 2008 ARMINES
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

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.util.Fractal;

public class ComancheBuilderTest extends FractalTestCase {

    private FScriptEngine engine;

    private ScriptLoader loader;

    @Before
    public void setUp() throws Exception {
        Component fscript = FScript.newEngine();
        engine = FScript.getFScriptEngine(fscript);
        loader = FScript.getScriptLoader(fscript);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void buildComanche() throws Exception {
        InputStream script = FScriptEngine.class.getResourceAsStream("comanche.fscript");
        loader.load(new InputStreamReader(script));
        Object result = engine.invoke("build-comanche");
        assertNotNull(result);
        assertTrue(result instanceof ComponentNode);
        ComponentNode node = (ComponentNode) result;
        Component comanche = node.getComponent();
        String name = Fractal.getNameController(comanche).getFcName();
        assertEquals("comanche.Comanche", name);
    }
}
