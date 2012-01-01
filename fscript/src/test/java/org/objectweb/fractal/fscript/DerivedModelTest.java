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
package org.objectweb.fractal.fscript;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;
import org.objectweb.fractal.fscript.simulation.DerivedModel;
import org.objectweb.fractal.fscript.simulation.DerivedNode;
import org.objectweb.fractal.util.Fractal;

public class DerivedModelTest extends FractalTestCase {
    
    private static FractalModel fractal;
    private static DerivedModel model;
    private ComancheHelper helper;
    
    @BeforeClass
    public static void staticSetUp() throws Exception {
        fractal = new FractalModel();
        fractal.startFc();
        model = new DerivedModel();
        model.bindFc("base-model", fractal);
        model.startFc();
    }

    @Before
    public void setUp() throws Exception {
        helper = new ComancheHelper();
    }
    
    @Test
    public void findComancheChildren() {
        Node comanche = fractal.createComponentNode(helper.comanche);
        Set<Node> kids = fractal.getAxis("child").selectFrom(comanche);
        assertEquals(2, kids.size());
    }
    
    @Test
    public void findDerivedComancheChildren() {
        Node comanche = new DerivedNode(fractal.createComponentNode(helper.comanche), null);
        Set<Node> kids = model.getAxis("child").selectFrom(comanche);
        assertEquals(2, kids.size());
    }
    
    @Test
    public void changeComancheName() throws NoSuchInterfaceException {
        Node comanche = fractal.createComponentNode(helper.comanche);
        comanche.setProperty("name", "foo");
        assertEquals("foo", comanche.getProperty("name"));
        assertEquals("foo", Fractal.getNameController(helper.comanche).getFcName());
    }
    
    @Test
    public void changeDerivedComancheName() throws NoSuchInterfaceException {
        Node comanche = new DerivedNode(fractal.createComponentNode(helper.comanche), null);
        comanche.setProperty("name", "foo");
        assertEquals("foo", comanche.getProperty("name"));
        assertEquals("comanche.Comanche", Fractal.getNameController(helper.comanche).getFcName());
    }
    
    @Test
    public void addSubComponent() throws Exception {
        HelloWorldHelper hwh = new HelloWorldHelper();
        Node hello = fractal.createComponentNode(hwh.hello);
        Node comanche = fractal.createComponentNode(helper.comanche);
        fractal.getAxis("child").connect(comanche, hello);
        Set<Node> kids = fractal.getAxis("child").selectFrom(comanche);
        assertEquals(3, kids.size());
        Component[] children = Fractal.getContentController(helper.comanche).getFcSubComponents();
        assertEquals(3, children.length);
    }
    
    @Test
    public void addDerivedSubComponent() throws Exception {
        HelloWorldHelper hwh = new HelloWorldHelper();
        Node hello = new DerivedNode(fractal.createComponentNode(hwh.hello), null);
        Node comanche = new DerivedNode(fractal.createComponentNode(helper.comanche), null);
        model.getAxis("child").connect(comanche, hello);
        Set<Node> kids = model.getAxis("child").selectFrom(comanche);
        assertEquals(3, kids.size());
        Component[] children = Fractal.getContentController(helper.comanche).getFcSubComponents();
        assertEquals(2, children.length);
    }
}
