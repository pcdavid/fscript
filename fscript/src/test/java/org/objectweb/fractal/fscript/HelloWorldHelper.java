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
package org.objectweb.fractal.fscript;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.util.Fractal;

public class HelloWorldHelper {
    public Component hello;

    public Component client;

    public Component server;

    public HelloWorldHelper() throws Exception {
        PrintStream err = System.err;
        try {
            // Avoid cluttering test output with messages from HelloWorld components.
            System.setErr(new PrintStream(new ByteArrayOutputStream()));
            FractalTestCase.initializeFractal();
            Component bootstrap = Fractal.getBootstrapComponent();
            TypeFactory typeFactory = Fractal.getTypeFactory(bootstrap);
            GenericFactory genericFactory = Fractal.getGenericFactory(bootstrap);
            // BEGIN GENERATED CODE
            InterfaceType IT0 = typeFactory.createFcItfType("r", "java.lang.Runnable", false, false, false);
            InterfaceType IT1 = typeFactory.createFcItfType("s", "Service", true, false, false);
            ComponentType CT0 = typeFactory.createFcType(new InterfaceType [] { IT0, IT1 });
            Component C0 = genericFactory.newFcInstance(CT0, "primitive", "ClientImpl");
            try { Fractal.getNameController(C0).setFcName("client"); } catch (NoSuchInterfaceException ignored) { }
            InterfaceType IT2 = typeFactory.createFcItfType("s", "Service", false, false, false);
            InterfaceType IT3 = typeFactory.createFcItfType("attribute-controller", "ServiceAttributes", false, false, false);
            ComponentType CT1 = typeFactory.createFcType(new InterfaceType [] { IT2, IT3 });
            Component C1 = genericFactory.newFcInstance(CT1, "primitive", "ServerImpl");
            try { Fractal.getNameController(C1).setFcName("server"); } catch (NoSuchInterfaceException ignored) { }
            AttributesHelper attrs = new AttributesHelper(C1);
            attrs.setAttribute("header", "-> ");
            attrs.setAttribute("count", 1);
            InterfaceType IT4 = typeFactory.createFcItfType("r", "java.lang.Runnable", false, false, false);
            ComponentType CT2 = typeFactory.createFcType(new InterfaceType [] { IT4 });
            Component C2 = genericFactory.newFcInstance(CT2, "composite", null);
            try { Fractal.getNameController(C2).setFcName("HelloWorld"); } catch (NoSuchInterfaceException ignored) { }
            Fractal.getContentController(C2).addFcSubComponent(C0);
            Fractal.getContentController(C2).addFcSubComponent(C1);
            Fractal.getBindingController(C2).bindFc("r", C0.getFcInterface("r"));
            Fractal.getBindingController(C0).bindFc("s", C1.getFcInterface("s"));
            // END GENERATED CODE
            hello = C2;
            client = C0;
            server = C1;
        } finally {
            System.setErr(err);
        }
    }
}
