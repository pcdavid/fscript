/*
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.fscript.nodes.ComponentNode;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.InterfaceNodeImpl;
import org.objectweb.fractal.fscript.nodes.Node;
import org.objectweb.fractal.fscript.statements.Statement;
import org.objectweb.fractal.util.Fractal;

import comanche.RequestReceiver;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class NewCompositeTest {
    private FScriptInterpreter fscript;

    private ComponentNode frontEnd;

    private Map<String, Object> vars;

    @Before
    public void setUp() throws NoSuchInterfaceException {
        fscript = new FScriptInterpreter();
        Component comanche = FactoryHelper.newComanche();
        ContentController cc = Fractal.getContentController(comanche);
        for (Component kid : cc.getFcSubComponents()) {
            String name = Fractal.getNameController(kid).getFcName();
            if (name.equals("fe")) {
                frontEnd = fscript.createComponentNode(kid);
                break;
            }
        }
        vars = new HashMap<String, Object>();
        vars.put("fe", frontEnd);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createCompositeFromFrontEnd() throws FScriptException {
        Set<Node> nodes;
        // Source component: 2 service interfaces.
        nodes = (Set<Node>) fscript.evaluateFrom(
                "./interface::*[not(controller-itf(.))]", frontEnd);
        assertEquals(2, nodes.size());
        // Create wrapper.
        Statement stat = fscript
                .parseStatement("return new_composite($fe/interface::*[not(controller-itf(.))]);");
        Object result = fscript.execute(stat, vars);
        // Check it has the same service interfaces.
        assertTrue(result instanceof ComponentNode);
        nodes = (Set<Node>) fscript.evaluateFrom(
                "./interface::*[not(controller-itf(.))]", (ComponentNode) result);
        assertEquals(2, nodes.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createComponent() throws FScriptException, NoSuchInterfaceException {
        Set<Node> nodes;
        // Source component: Runnable entry point
        nodes = (Set<Node>) fscript.evaluateFrom("./interface::r", frontEnd);
        assertEquals(1, nodes.size());
        // Create wrapper.
        Object result = fscript.apply("new_component",
                new Object[] { nodes, "primitive", RequestReceiver.class.getName()});
        // Check it has the same service interfaces.
        assertTrue(result instanceof ComponentNode);
        nodes = (Set<Node>) fscript.evaluateFrom(
                "./interface::*[not(controller-itf(.))]", (ComponentNode) result);
        assertEquals(1, nodes.size());
        Node n = nodes.iterator().next();
        Interface itf = ((InterfaceNodeImpl) n).getInterface();
        assertEquals("r", itf.getFcItfName());
        assertEquals(Runnable.class.getName(), ((InterfaceType) itf.getFcItfType()).getFcItfSignature());
        assertFalse(itf.isFcInternalItf());
    }
}
