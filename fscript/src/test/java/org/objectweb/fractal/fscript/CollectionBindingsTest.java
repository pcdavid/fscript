/*
 * Copyright (c) 2007-2008 ARMINES
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
import java.io.Reader;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.fscript.model.fractal.InterfaceNode;
import org.objectweb.fractal.fscript.model.fractal.NodeFactory;

/**
 * @author Pierre-Charles David
 */
public class CollectionBindingsTest extends FractalTestCase {
    private FScriptEngine engine;
    
    private NodeFactory nodeFactory;

    private Component fscript;

    private ScriptLoader loader;

    @Before
    public void setUp() throws Exception {
        fscript = FScript.newEngine();
        engine = FScript.getFScriptEngine(fscript);
        loader = FScript.getScriptLoader(fscript);
        nodeFactory = FScript.getNodeFactory(fscript);
    }

    private Reader fixture(String name) {
        InputStream stream = CollectionBindingsTest.class.getResourceAsStream(name);
        return new InputStreamReader(stream);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void addCommancheHandler() throws Exception {
        loader.load(fixture("add-handler.fscript"));
        Node comanche = nodeFactory.createComponentNode(new ComancheHelper().comanche);
        Node errHandler = nodeFactory.createComponentNode(newComponent("comanche.ErrorHandler"));
        engine.invoke("add-handler", comanche, errHandler);
        Object result = engine.invoke("all-handlers", comanche);
        assertTrue(result instanceof Set);
        Set<Node> itfs = (Set<Node>) result;
        assertEquals(4, itfs.size());
        boolean h0 = false, h1 = false, h2 = false;
        for (Node node : itfs) {
            InterfaceNode itf = (InterfaceNode) node;
            if (itf.getName().equals("h0")) {
                assertFalse(h0);
                h0 = true;
            } else if (itf.getName().equals("h1")) {
                assertFalse(h1);
                h1 = true;
            } else if (itf.getName().equals("h2")) {
                assertFalse(h2);
                h2 = true;
            }
        }
        assertTrue(h0 && h1 && h2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createNewCollectionInterfaceInstanceFromFScript() throws Exception,
            NoSuchInterfaceException {
        ComponentNode comanche = nodeFactory.createComponentNode(new ComancheHelper().comanche);
        engine.setGlobalVariable("c", comanche);
        Set<Node> nodes = (Set) engine.execute("$c/child::be/child::rh/child::rd");
        ComponentNode rd = (ComponentNode) nodes.iterator().next();
        Component c = rd.getComponent();
        for (Object o : c.getFcInterfaces()) {
            Interface itf = (Interface) o;
            assertFalse("h".equals(itf.getFcItfName()));
        }
        Interface h = (Interface) c.getFcInterface("h");
        assertNotNull(h);
        assertEquals("h", h.getFcItfName());
        //
        ComponentType cType = (ComponentType) c.getFcType();
        for (InterfaceType itfType : cType.getFcInterfaceTypes()) {
            if (itfType.isFcCollectionItf()) {
                assertEquals("h", itfType.getFcItfName());
            }
        }
    }
}