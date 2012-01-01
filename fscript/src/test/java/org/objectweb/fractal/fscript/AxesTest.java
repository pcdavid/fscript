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

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.axes.ComponentAxis;
import org.objectweb.fractal.fscript.nodes.ComponentNode;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;
import org.objectweb.fractal.fscript.nodes.Node;
import org.objectweb.fractal.util.Fractal;

public class AxesTest {
    private DefaultNodeFactory nf;

    @Before
    public void setUp() {
        nf = new DefaultNodeFactory();
    }

    @Test
    public void componentAxis() throws Exception {
        Component comanche = FactoryHelper.newComanche();
        ComponentNodeImpl node = nf.createComponentNode(comanche);
        Set<ComponentNodeImpl> coll = new ComponentAxis(nf).selectFrom(node);
        assertNotNull(coll);
        assertTrue(coll.size() == 1);
        Object obj = coll.iterator().next();
        assertTrue(obj instanceof ComponentNode);
        assertEquals(node, obj);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void sharedComponentSibling() throws Exception {
        Component root1 = FactoryHelper.newComanche();
        Component root2 = FactoryHelper.newComanche();
        Component shared = FactoryHelper.newComanche();
        Fractal.getContentController(root1).addFcSubComponent(shared);
        Fractal.getContentController(root2).addFcSubComponent(shared);
        Component[] parents = Fractal.getSuperController(shared).getFcSuperComponents();
        assertEquals(2, parents.length);
        FScriptInterpreter fscript = new FScriptInterpreter();
        Node n = fscript.createComponentNode(shared);
        Set<Node> siblingsOrSelf = (Set<Node>) fscript.evaluateFrom("./sibling-or-self::*", shared);
        Set<Node> siblings = (Set<Node>) fscript.evaluateFrom("./sibling::*", shared);
        assertTrue(siblingsOrSelf.contains(n));
        assertFalse(siblings.contains(n));
        assertTrue(siblingsOrSelf.size() == siblings.size() + 1);
    }
}
