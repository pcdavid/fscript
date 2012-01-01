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
package org.objectweb.fractal.fscript.model.fractal;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.FractalTestCase;
import org.objectweb.fractal.fscript.HelloWorldHelper;
import org.objectweb.fractal.fscript.model.Axis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.util.Fractal;

public class ChildAxisTest extends FractalTestCase {
    private HelloWorldHelper helper;

    private Axis axis;

    private ComponentNode helloNode;

    private FractalModel model;

    @Before
    public void setUp() throws Exception {
        model = new FractalModel();
        model.startFc();
        helper = new HelloWorldHelper();
        helloNode = new ComponentNode(model, helper.hello);
        axis = model.getAxis("child");
    }

    @Test
    public void findHelloChildren() throws NoSuchInterfaceException {
        Set<Node> children = axis.selectFrom(helloNode);
        assertNotNull(children);
        assertSameComponents(new Component[] { helper.client, helper.server }, children);
    }

    @Test
    public void findPrimitiveChildren() throws NoSuchInterfaceException {
        Component source = Fractal.getContentController(helper.hello).getFcSubComponents()[0];
        Set<Node> children = axis.selectFrom(new ComponentNode(model, source));
        assertEquals(0, children.size());
    }

    @Test
    public void addChild() throws Exception {
        Component hello2 = new HelloWorldHelper().hello;
        ComponentNode helloNode2 = new ComponentNode(model, hello2);
        axis.connect(helloNode, helloNode2);
        Set<Node> childrenAfter = axis.selectFrom(helloNode);
        assertTrue(childrenAfter.contains(helloNode2));
    }

    private void assertSameComponents(Component[] expected, Set<Node> actual) {
        assertEquals(expected.length, actual.size());
        for (Node node : actual) {
            ComponentNode cnode = (ComponentNode) node;
            Component actualComp = cnode.getComponent();
            boolean found = false;
            for (int i = 0; i < expected.length; i++) {
                if (expected[i] == actualComp) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }
}
