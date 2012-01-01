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

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.AttributeNode;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.fscript.model.fractal.NodeFactory;
import org.objectweb.fractal.util.Fractal;

public class FPathTest extends FractalTestCase {
    private static FScriptEngine engine;

    private static ComancheHelper helper;

    private static NodeFactory nodeFactory;

    @BeforeClass
    public static void setUp() throws Exception {
        Component fscript = FScript.newEngine();
        engine = FScript.getFScriptEngine(fscript);
        nodeFactory = FScript.getNodeFactory(fscript);
        helper = new ComancheHelper();
        Node node = nodeFactory.createComponentNode(helper.comanche);
        engine.setGlobalVariable("root", node);
    }

    private Object eval(String expr) throws FScriptException {
        return engine.execute(expr);
    }

    @SuppressWarnings("unchecked")
    private Set<Node> nodeQuery(String query) throws FScriptException {
        Object rawResult = eval(query);
        assertTrue(rawResult instanceof Set);
        return (Set<Node>) rawResult;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void fpath1() throws Exception {
        Set<Node> nodes = nodeQuery("$root/child::*");
        assertEquals(2, nodes.size());
        for (Node element : nodes) {
            assertTrue(element instanceof ComponentNode);
            Component kid = ((ComponentNode) element).getComponent();
            assertTrue(kid == helper.frontend || kid == helper.backend);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void fpath2() throws Exception {
        Set<Node> nodes = nodeQuery("$root/child-or-self::*");
        assertEquals(3, nodes.size());
        boolean selfFound = false;
        for (Node element : nodes) {
            assertTrue(element instanceof ComponentNode);
            if (((ComponentNode) element).getComponent().equals(helper.comanche)) {
                if (selfFound) {
                    fail("Found self twice.");
                } else {
                    selfFound = true;
                }
            } else {
                Component kid = ((ComponentNode) element).getComponent();
                assertContains(helper.comanche, kid);
            }
        }
        assertTrue(selfFound);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findFrontendByName() throws Exception {
        Set<Node> nodes = nodeQuery("$root/child::fe");
        assertEquals(1, nodes.size());
        assertEquals(nodes.iterator().next(), nodeFactory.createComponentNode(helper.frontend));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findFrontendByPredicate() throws Exception {
        Set<Node> nodes = nodeQuery("$root/child::*[name(current()) == 'fe']");
        assertEquals(1, nodes.size());
        assertEquals(nodes.iterator().next(), nodeFactory.createComponentNode(helper.frontend));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findServerChildByPredicateWithDot() throws Exception {
        Set<Node> nodes = nodeQuery("$root/child::*[name(.) == 'fe']");
        assertEquals(1, nodes.size());
        assertEquals(nodes.iterator().next(), nodeFactory.createComponentNode(helper.frontend));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findSharedComponents() throws Exception {
        Set<Node> shared = nodeQuery("$root/descendant-or-self::*[size(./parent::*) > 1]");
        assertTrue(shared.isEmpty());
    }

    @Test
    public void findConfigurableComponents() throws Exception {
        Set<Node> configurable = nodeQuery("$root/descendant-or-self::*[./attribute::*]");
        assertTrue(configurable.isEmpty());
    }

    @Test
    public void findAttributesInHelloWorld() throws Exception {
        Component hello = new HelloWorldHelper().hello;
        engine.setGlobalVariable("hello", nodeFactory.createComponentNode(hello));
        Set<Node> attrs = nodeQuery("$hello/descendant-or-self::*/attribute::*");
        assertEquals(2, attrs.size());
        boolean foundHeader = false;
        boolean foundCount = false;
        for (Node n : attrs) {
            assertTrue(n instanceof AttributeNode);
            AttributeNode attr = (AttributeNode) n;
            if (attr.getName().equals("header")) {
                foundHeader = true;
            } else if (attr.getName().equals("count")) {
                foundCount = true;
            }
        }
        assertTrue(foundHeader && foundCount);
    }
    

    void assertContains(Component parent, Component kid) {
        try {
            ContentController cc = Fractal.getContentController(parent);
            Component[] allKids = cc.getFcSubComponents();
            for (Component element : allKids) {
                if (kid == element) {
                    return;
                }
            }
            fail("Composite " + parent + " does not contain " + kid + ".");
        } catch (NoSuchInterfaceException e) {
            fail("Parent argument is not composite.");
        }
    }
}
