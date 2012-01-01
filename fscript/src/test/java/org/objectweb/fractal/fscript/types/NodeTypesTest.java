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
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript.types;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.fscript.ComancheHelper;
import org.objectweb.fractal.fscript.FractalTestCase;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;
import org.objectweb.fractal.fscript.model.fractal.InterfaceNode;


public class NodeTypesTest extends FractalTestCase {
    private static Component comanche;
    private static ComponentNode comancheNode;
    private static InterfaceNode runNode;
    private static FractalModel model;

    @BeforeClass
    public static void setUp() throws Exception {
        comanche = new ComancheHelper().comanche;
        model = new FractalModel();
        model.startFc();
        comancheNode = model.createComponentNode(comanche);
        runNode = model.createInterfaceNode((Interface) comanche.getFcInterface("r"));
    }
    
    @Test
    public void validComponentNode() {
        assertTrue(model.getNodeKind("component").checkValue(comancheNode));
    }
    
    @Test
    public void validInterfaceNode() {
        assertTrue(model.getNodeKind("interface").checkValue(runNode));
    }
}
