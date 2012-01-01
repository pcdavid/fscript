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
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.util.Fractal;

public class ComancheHelper {
    public Component comanche;

    public Component frontend;

    public Component backend;

    public Component receiver;

    public Component scheduler;

    public Component analyzer;

    public Component handler;

    public Component logger;

    public Component dispatcher;

    public Component fileHandler;

    public Component errorHandler;

    public ComancheHelper() throws Exception {
        FractalTestCase.initializeFractal();
        Component bootstrap = Fractal.getBootstrapComponent();
        TypeFactory typeFactory = Fractal.getTypeFactory(bootstrap);
        GenericFactory genericFactory = Fractal.getGenericFactory(bootstrap);
        // BEGIN GENERATED CODE
        InterfaceType IT0 = typeFactory.createFcItfType("r", "java.lang.Runnable", false, false, false);
        InterfaceType IT1 = typeFactory.createFcItfType("rh", "comanche.RequestHandler", true, false, false);
        InterfaceType IT2 = typeFactory.createFcItfType("s", "comanche.Scheduler", true, false, false);
        ComponentType CT0 = typeFactory.createFcType(new InterfaceType [] { IT0, IT1, IT2 });
        Component C0 = genericFactory.newFcInstance(CT0, "primitive", "comanche.RequestReceiver");
        try { Fractal.getNameController(C0).setFcName("rr"); } catch (NoSuchInterfaceException ignored) { }
        InterfaceType IT3 = typeFactory.createFcItfType("s", "comanche.Scheduler", false, false, false);
        ComponentType CT1 = typeFactory.createFcType(new InterfaceType [] { IT3 });
        Component C1 = genericFactory.newFcInstance(CT1, "primitive", "comanche.MultiThreadScheduler");
        try { Fractal.getNameController(C1).setFcName("s"); } catch (NoSuchInterfaceException ignored) { }
        Fractal.getLifeCycleController(C1).startFc();
        InterfaceType IT4 = typeFactory.createFcItfType("r", "java.lang.Runnable", false, false, false);
        InterfaceType IT5 = typeFactory.createFcItfType("rh", "comanche.RequestHandler", true, false, false);
        ComponentType CT2 = typeFactory.createFcType(new InterfaceType [] { IT4, IT5 });
        Component C2 = genericFactory.newFcInstance(CT2, "composite", null);
        try { Fractal.getNameController(C2).setFcName("fe"); } catch (NoSuchInterfaceException ignored) { }
        Fractal.getContentController(C2).addFcSubComponent(C0);
        Fractal.getContentController(C2).addFcSubComponent(C1);
        Fractal.getBindingController(C2).bindFc("r", C0.getFcInterface("r"));
        Fractal.getBindingController(C0).bindFc("rh", Fractal.getContentController(C2).getFcInternalInterface("rh"));
        Fractal.getBindingController(C0).bindFc("s", C1.getFcInterface("s"));
        InterfaceType IT6 = typeFactory.createFcItfType("a", "comanche.RequestHandler", false, false, false);
        InterfaceType IT7 = typeFactory.createFcItfType("l", "comanche.Logger", true, false, false);
        InterfaceType IT8 = typeFactory.createFcItfType("rh", "comanche.RequestHandler", true, false, false);
        ComponentType CT3 = typeFactory.createFcType(new InterfaceType [] { IT6, IT7, IT8 });
        Component C3 = genericFactory.newFcInstance(CT3, "primitive", "comanche.RequestAnalyzer");
        try { Fractal.getNameController(C3).setFcName("ra"); } catch (NoSuchInterfaceException ignored) { }
        InterfaceType IT9 = typeFactory.createFcItfType("rh", "comanche.RequestHandler", false, false, false);
        InterfaceType IT10 = typeFactory.createFcItfType("h", "comanche.RequestHandler", true, false, true);
        ComponentType CT4 = typeFactory.createFcType(new InterfaceType [] { IT9, IT10 });
        Component C4 = genericFactory.newFcInstance(CT4, "primitive", "comanche.RequestDispatcher");
        try { Fractal.getNameController(C4).setFcName("rd"); } catch (NoSuchInterfaceException ignored) { }
        InterfaceType IT11 = typeFactory.createFcItfType("rh", "comanche.RequestHandler", false, false, false);
        ComponentType CT5 = typeFactory.createFcType(new InterfaceType [] { IT11 });
        Component C5 = genericFactory.newFcInstance(CT5, "primitive", "comanche.FileRequestHandler");
        try { Fractal.getNameController(C5).setFcName("frh"); } catch (NoSuchInterfaceException ignored) { }
        Fractal.getLifeCycleController(C5).startFc();
        InterfaceType IT12 = typeFactory.createFcItfType("rh", "comanche.RequestHandler", false, false, false);
        ComponentType CT6 = typeFactory.createFcType(new InterfaceType [] { IT12 });
        Component C6 = genericFactory.newFcInstance(CT6, "primitive", "comanche.ErrorRequestHandler");
        try { Fractal.getNameController(C6).setFcName("erh"); } catch (NoSuchInterfaceException ignored) { }
        Fractal.getLifeCycleController(C6).startFc();
        InterfaceType IT13 = typeFactory.createFcItfType("rh", "comanche.RequestHandler", false, false, false);
        ComponentType CT7 = typeFactory.createFcType(new InterfaceType [] { IT13 });
        Component C7 = genericFactory.newFcInstance(CT7, "composite", null);
        try { Fractal.getNameController(C7).setFcName("rh"); } catch (NoSuchInterfaceException ignored) { }
        Fractal.getContentController(C7).addFcSubComponent(C4);
        Fractal.getContentController(C7).addFcSubComponent(C5);
        Fractal.getContentController(C7).addFcSubComponent(C6);
        Fractal.getBindingController(C7).bindFc("rh", C4.getFcInterface("rh"));
        Fractal.getBindingController(C4).bindFc("h0", C5.getFcInterface("rh"));
        Fractal.getBindingController(C4).bindFc("h1", C6.getFcInterface("rh"));
        InterfaceType IT14 = typeFactory.createFcItfType("l", "comanche.Logger", false, false, false);
        ComponentType CT8 = typeFactory.createFcType(new InterfaceType [] { IT14 });
        Component C8 = genericFactory.newFcInstance(CT8, "primitive", "comanche.BasicLogger");
        try { Fractal.getNameController(C8).setFcName("l"); } catch (NoSuchInterfaceException ignored) { }
        Fractal.getLifeCycleController(C8).startFc();
        InterfaceType IT15 = typeFactory.createFcItfType("rh", "comanche.RequestHandler", false, false, false);
        ComponentType CT9 = typeFactory.createFcType(new InterfaceType [] { IT15 });
        Component C9 = genericFactory.newFcInstance(CT9, "composite", null);
        try { Fractal.getNameController(C9).setFcName("be"); } catch (NoSuchInterfaceException ignored) { }
        Fractal.getContentController(C9).addFcSubComponent(C3);
        Fractal.getContentController(C9).addFcSubComponent(C7);
        Fractal.getContentController(C9).addFcSubComponent(C8);
        Fractal.getBindingController(C9).bindFc("rh", C3.getFcInterface("a"));
        Fractal.getBindingController(C3).bindFc("l", C8.getFcInterface("l"));
        Fractal.getBindingController(C3).bindFc("rh", C7.getFcInterface("rh"));
        InterfaceType IT16 = typeFactory.createFcItfType("r", "java.lang.Runnable", false, false, false);
        ComponentType CT10 = typeFactory.createFcType(new InterfaceType [] { IT16 });
        Component C10 = genericFactory.newFcInstance(CT10, "composite", null);
        try { Fractal.getNameController(C10).setFcName("comanche.Comanche"); } catch (NoSuchInterfaceException ignored) { }
        Fractal.getContentController(C10).addFcSubComponent(C2);
        Fractal.getContentController(C10).addFcSubComponent(C9);
        Fractal.getBindingController(C10).bindFc("r", C2.getFcInterface("r"));
        Fractal.getBindingController(C2).bindFc("rh", C9.getFcInterface("rh"));
        // END GENERATED CODE
        comanche = C10;
        frontend = childNamed(comanche, "fe");
        backend = childNamed(comanche, "be");
        receiver = childNamed(frontend, "rr");
        scheduler = childNamed(frontend, "s");
        analyzer = childNamed(backend, "ra");
        handler = childNamed(backend, "rh");
        logger = childNamed(backend, "l");
        dispatcher = childNamed(handler, "rd");
        fileHandler = childNamed(handler, "frh");
        errorHandler = childNamed(handler, "erh");
    }

    private Component childNamed(Component parent, String name) {
        try {
            ContentController cc = Fractal.getContentController(parent);
            for (Component child : cc.getFcSubComponents()) {
                String childName = Fractal.getNameController(child).getFcName();
                if (name.equals(childName)) {
                    return child;
                }
            }
        } catch (NoSuchInterfaceException e) {
        }
        return null;
    }
}
