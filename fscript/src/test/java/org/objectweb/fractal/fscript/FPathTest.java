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
import static org.objectweb.fractal.fscript.FractalAssert.assertContains;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.Node;
import org.objectweb.fractal.util.Fractal;

public class FPathTest {
    private FScriptInterpreter fscript;

    private Component comanche;

    private Component frontend;

    @Before
    public void setUp() throws Exception {
        fscript = new FScriptInterpreter();
        comanche = FactoryHelper.newComanche();
        Node node = fscript.createComponentNode(comanche);
        fscript.getEnvironment().setVariable("root", node);

        Component[] kids = Fractal.getContentController(comanche).getFcSubComponents();
        for (Component kid : kids) {
            String name = Fractal.getNameController(kid).getFcName();
            if ("fe".equals(name)) {
                frontend = kid;
                break;
            }
        }
        if (frontend == null) {
            throw new AssertionError("Can't find printer component.");
        }
    }

    private Object eval(String expr) throws FScriptException {
        return fscript.evaluate(expr, null);
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
            assertTrue(element instanceof ComponentNodeImpl);
            Component kid = ((ComponentNodeImpl) element).getComponent();
            assertContains(comanche, kid);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void fpath2() throws Exception {
        Set<Node> nodes = nodeQuery("$root/child-or-self::*");
        assertEquals(3, nodes.size());
        boolean selfFound = false;
        for (Node element : nodes) {
            assertTrue(element instanceof ComponentNodeImpl);
            if (((ComponentNodeImpl) element).getComponent().equals(comanche)) {
                if (selfFound) {
                    fail("Found self twice.");
                } else {
                    selfFound = true;
                }
            } else {
                Component kid = ((ComponentNodeImpl) element).getComponent();
                assertContains(comanche, kid);
            }
        }
        assertTrue(selfFound);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findFrontendByName() throws Exception {
        Set<Node> nodes = nodeQuery("$root/child::fe");
        assertEquals(1, nodes.size());
        assertEquals(nodes.iterator().next(), fscript.createComponentNode(frontend));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findFrontendByPredicate() throws Exception {
        Set<Node> nodes = nodeQuery("$root/child::*[name(current()) == 'fe']");
        assertEquals(1, nodes.size());
        assertEquals(nodes.iterator().next(), fscript.createComponentNode(frontend));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findServerChildByPredicateWithDot() throws Exception {
        Set<Node> nodes = nodeQuery("$root/child::*[name(.) == 'fe']");
        assertEquals(1, nodes.size());
        assertEquals(nodes.iterator().next(), fscript.createComponentNode(frontend));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findSharedComponents() throws Exception {
        Set<Node> shared = nodeQuery("$root/descendant-or-self::*[size(./parent::*) > 1]");
        assertTrue(shared.isEmpty());
    }

    @Test
    public void findConfigurableComponents() throws Exception {
        Set<Node> configurable = nodeQuery("$root/descendant-or-self::*[attribute::*]");
        assertTrue(configurable.isEmpty());
    }
    
    @Test
    public void relativePath() throws FScriptException {
        Node node = fscript.createComponentNode(comanche);
        Object result = fscript.evaluateFrom("./child::*", node);
        assertTrue(result instanceof Set);
    }
}
