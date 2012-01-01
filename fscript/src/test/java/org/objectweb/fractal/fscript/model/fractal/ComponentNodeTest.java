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
package org.objectweb.fractal.fscript.model.fractal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.FractalTestCase;
import org.objectweb.fractal.fscript.HelloWorldHelper;
import org.objectweb.fractal.util.Fractal;

public class ComponentNodeTest extends FractalTestCase {
    private Component component;

    private ComponentNode node;

    @Before
    public void setUp() throws Exception {
        component = new HelloWorldHelper().hello;
        FractalModel model = new FractalModel();
        model.startFc();
        node = new ComponentNode(model, component);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readInvalidProperty() {
        node.getProperty("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeInvalidProperty() {
        node.setProperty("invalid", null);
    }

    @Test
    public void readComponentName() throws NoSuchInterfaceException {
        String realName = Fractal.getNameController(component).getFcName();
        assertEquals(realName, node.getName());
    }

    @Test
    public void genericReadComponentName() throws NoSuchInterfaceException {
        String realName = Fractal.getNameController(component).getFcName();
        assertEquals(realName, node.getProperty("name"));
    }

    @Test
    public void readComponentState() throws NoSuchInterfaceException {
        String realState = Fractal.getLifeCycleController(component).getFcState();
        assertEquals(realState, node.getState());
    }

    @Test
    public void genericReadComponentState() throws NoSuchInterfaceException {
        String realState = Fractal.getLifeCycleController(component).getFcState();
        assertEquals(realState, node.getProperty("state"));
    }

    @Test
    public void writeComponentName() throws NoSuchInterfaceException {
        String newName = "BonjourMonde";
        node.setName(newName);
        assertEquals(newName, node.getName());
        String realName = Fractal.getNameController(component).getFcName();
        assertEquals(newName, realName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeNullComponentName() {
        node.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void genericWriteNullComponentName() {
        node.setProperty("name", null);
    }

    @Test
    public void genericWriteComponentName() throws NoSuchInterfaceException {
        String newName = "BonjourMonde";
        node.setProperty("name", newName);
        assertEquals(newName, node.getProperty("name"));
        String realName = Fractal.getNameController(component).getFcName();
        assertEquals(newName, realName);
    }

    @Test
    public void writeComponentState() throws NoSuchInterfaceException {
        String newState = "STARTED";
        node.setState(newState);
        assertEquals(newState, node.getState());
        String realState = Fractal.getLifeCycleController(component).getFcState();
        assertEquals(newState, realState);
        //
        newState = "STOPPED";
        node.setState(newState);
        assertEquals(newState, node.getState());
        realState = Fractal.getLifeCycleController(component).getFcState();
        assertEquals(newState, realState);
    }

    @Test
    public void genericWriteComponentState() throws NoSuchInterfaceException {
        String newState = "STARTED";
        node.setProperty("state", newState);
        assertEquals(newState, node.getProperty("state"));
        String realState = Fractal.getLifeCycleController(component).getFcState();
        assertEquals(newState, realState);
        //
        newState = "STOPPED";
        node.setProperty("state", newState);
        assertEquals(newState, node.getProperty("state"));
        realState = Fractal.getLifeCycleController(component).getFcState();
        assertEquals(newState, realState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeNullComponentState() {
        node.setState(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void genericWriteNullComponentState() {
        node.setProperty("state", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeInvalidComponentState() {
        node.setState("INVALID");
    }
}
