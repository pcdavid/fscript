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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.model.fractal;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.fscript.HelloWorldHelper;
import org.objectweb.fractal.util.Fractal;

public class InterfaceNodeTests {
    private Component component;

    private FractalModel model;

    private Map<Interface, InterfaceNode> itfs = new HashMap<Interface, InterfaceNode>();

    @Before
    public void setUp() throws Exception {
        model = new FractalModel();
        model.startFc();
        component = new HelloWorldHelper().hello;
        addNode(component, "component");
        addNode(component, "name-controller");
        addNode(component, "r");
        for (Component child : Fractal.getContentController(component).getFcSubComponents()) {
            for (Object rawItf : child.getFcInterfaces()) {
                Interface itf = (Interface) rawItf;
                itfs.put(itf, new InterfaceNode(model, itf));
            }
        }
        for (Object rawItf : Fractal.getContentController(component).getFcInternalInterfaces()) {
            Interface itf = (Interface) rawItf;
            itfs.put(itf, new InterfaceNode(model, itf));
        }
    }
    
    
    private void addNode(Component owner, String itfName) throws NoSuchInterfaceException {
        Interface itf = (Interface) owner.getFcInterface(itfName);
        InterfaceNode node = new InterfaceNode(model, itf);
        itfs.put(itf, node);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readInvalidProperty() {
        InterfaceNode node = itfs.get(component);
        node.getProperty("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeInvalidProperty() {
        InterfaceNode node = itfs.get(component);
        node.setProperty("invalid", null);
    }

    @Test
    public void readInterfaceName() throws NoSuchInterfaceException {
        for (Map.Entry<Interface, InterfaceNode> entry : itfs.entrySet()) {
            String realName = entry.getKey().getFcItfName();
            assertEquals(realName, entry.getValue().getName());
            assertEquals(realName, entry.getValue().getProperty("name"));
        }
    }
    
    @Test
    public void readInterfaceIsInternal() {
        for (Map.Entry<Interface, InterfaceNode> entry : itfs.entrySet()) {
            boolean realIsInternal = entry.getKey().isFcInternalItf();
            assertEquals(realIsInternal, entry.getValue().isInternal());
            assertEquals(realIsInternal, entry.getValue().getProperty("internal"));
        }
    }
    
    @Test
    public void readInterfaceSignature() {
        for (Map.Entry<Interface, InterfaceNode> entry : itfs.entrySet()) {
            InterfaceType itfType = (InterfaceType) entry.getKey().getFcItfType();
            String realSignature = itfType.getFcItfSignature();
            assertEquals(realSignature, entry.getValue().getSignature());
            assertEquals(realSignature, entry.getValue().getProperty("signature"));
        }
    }
    
    @Test
    public void readInterfaceIsCollection() {
        for (Map.Entry<Interface, InterfaceNode> entry : itfs.entrySet()) {
            InterfaceType itfType = (InterfaceType) entry.getKey().getFcItfType();
            boolean realIsCollection = itfType.isFcCollectionItf();
            assertEquals(realIsCollection, entry.getValue().isCollection());
            assertEquals(realIsCollection, entry.getValue().getProperty("collection"));
        }
    }
    
    @Test
    public void readInterfaceIsOptional() {
        for (Map.Entry<Interface, InterfaceNode> entry : itfs.entrySet()) {
            InterfaceType itfType = (InterfaceType) entry.getKey().getFcItfType();
            boolean realIsOptional = itfType.isFcOptionalItf();
            assertEquals(realIsOptional, entry.getValue().isOptional());
            assertEquals(realIsOptional, entry.getValue().getProperty("optional"));
        }
    }
    
    @Test
    public void readInterfaceIsClient() {
        for (Map.Entry<Interface, InterfaceNode> entry : itfs.entrySet()) {
            InterfaceType itfType = (InterfaceType) entry.getKey().getFcItfType();
            boolean realIsClient = itfType.isFcClientItf();
            assertEquals(realIsClient, entry.getValue().isClient());
            assertEquals(realIsClient, entry.getValue().getProperty("client"));
        }
    }
}