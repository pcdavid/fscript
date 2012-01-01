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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.fscript.model.fractal.InterfaceNode;
import org.objectweb.fractal.fscript.model.fractal.NodeFactory;
import org.objectweb.fractal.util.Fractal;

import comanche.RequestReceiver;

public class NewCompositeTest extends FractalTestCase {
    private FScriptEngine engine;

    private ComponentNode frontEnd;

    @Before
    public void setUp() throws Exception {
        Component fscript = FScript.newEngine();
        engine = FScript.getFScriptEngine(fscript);
        Component comanche = new ComancheHelper().comanche;
        ContentController cc = Fractal.getContentController(comanche);
        for (Component kid : cc.getFcSubComponents()) {
            String name = Fractal.getNameController(kid).getFcName();
            if (name.equals("fe")) {
                NodeFactory nf = FScript.getNodeFactory(fscript);
                frontEnd = nf.createComponentNode(kid);
                break;
            }
        }
        engine.setGlobalVariable("fe", frontEnd);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createCompositeFromFrontEnd() throws FScriptException {
        Set<Node> nodes;
        // Source component: 2 service interfaces.
        nodes = (Set<Node>) engine.execute("$fe/interface::*[not(controller-itf(.))]");
        assertEquals(2, nodes.size());
        // Create wrapper.
        Object result = engine.execute("new-composite($fe/interface::*[not(controller-itf(.))])");
        // Check it has the same service interfaces.
        assertTrue(result instanceof ComponentNode);
        engine.setGlobalVariable("c", result);
        nodes = (Set<Node>) engine.execute("$c/interface::*[not(controller-itf(.))]");
        assertEquals(2, nodes.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @Ignore
    public void createComponent() throws FScriptException, NoSuchInterfaceException {
        Set<Node> nodes;
        // Source component: Runnable entry point
        nodes = (Set<Node>) engine.execute("$fe/interface::r");
        assertEquals(1, nodes.size());
        // Create wrapper.
        Object result = engine.invoke("new-component", nodes, "primitive", RequestReceiver.class
                .getName());
        // Check it has the same service interfaces.
        assertTrue(result instanceof ComponentNode);
        engine.setGlobalVariable("c", result);
        nodes = (Set<Node>) engine.execute("$c/interface::*[not(controller-itf(.))]");
        assertEquals(1, nodes.size());
        Node n = nodes.iterator().next();
        Interface itf = ((InterfaceNode) n).getInterface();
        assertEquals("r", itf.getFcItfName());
        assertEquals(Runnable.class.getName(), ((InterfaceType) itf.getFcItfType())
                .getFcItfSignature());
        assertFalse(itf.isFcInternalItf());
    }
}