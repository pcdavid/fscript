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
import static org.objectweb.fractal.fscript.FractalAssert.assertHasInterface;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;
import org.objectweb.fractal.fscript.nodes.InterfaceNodeImpl;

public class NodesTest {
    private Component comanche;

    private DefaultNodeFactory nf;

    @Before
    public void setUp() {
        comanche = FactoryHelper.newComanche();
        nf = new DefaultNodeFactory();
    }

    @Test
    public void componentNode() throws Exception {
        ComponentNodeImpl node = nf.createComponentNode(comanche);
        assertEquals(FactoryHelper.COMANCHE_APP, node.getName());
        assertSame(node.getComponent(), comanche);
    }

    @Test
    public void interfaceNode() throws Exception {
        assertHasInterface("component", comanche);
        InterfaceNodeImpl node = nf.createInterfaceNode((Interface) comanche
                .getFcInterface("component"));
        assertEquals("component", node.getName());
        assertSame(comanche.getFcInterface("component"), ((InterfaceNodeImpl) node)
                .getInterface());
        assertSame(comanche, node.getComponent());
    }
}
