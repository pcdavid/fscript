/*
 * Copyright (c) 2007 ARMINES
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

import org.junit.Ignore;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.fscript.nodes.ComponentNode;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.InterfaceNode;
import org.objectweb.fractal.fscript.nodes.Node;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class CollectionBindingsTest {
    private Reader fixture(String name) {
        InputStream stream = CollectionBindingsTest.class.getResourceAsStream(name);
        return new InputStreamReader(stream);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void addCommancheHandler() throws FScriptException {
        FScriptInterpreter fscript = new FScriptInterpreter();
        fscript.loadDefinitions(fixture("add-handler.fscript"));
        Node comanche = fscript.createComponentNode(FactoryHelper.newComanche());
        Node errHandler = fscript.createComponentNode(FactoryHelper
                .newComponent("comanche.ErrorHandler"));
        fscript.apply("add-handler", new Object[] { comanche, errHandler });
        Object result = fscript.apply("all-handlers", new Object[] { comanche });
        assertTrue(result instanceof Set);
        Set<Node> itfs = (Set<Node>) result;
        assertEquals(3, itfs.size());
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

    @Ignore("This test was just to make sure I understand how collection interfaces work.")
    @SuppressWarnings("unchecked")
    @Test
    public void createNewCollectionInterfaceInstanceFromFScript()
            throws FScriptException, NoSuchInterfaceException {
        FScriptInterpreter fscript = new FScriptInterpreter();
        ComponentNode comanche = fscript.createComponentNode(FactoryHelper.newComanche());
        Set<Node> nodes = (Set) fscript.evaluateFrom("./child::be/child::rh/child::rd",
                comanche);
        ComponentNodeImpl rd = (ComponentNodeImpl) nodes.iterator().next();
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
