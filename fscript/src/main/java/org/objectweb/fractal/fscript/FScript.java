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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.FactoryFactory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.NodeFactory;
import org.objectweb.fractal.util.Fractal;

import com.google.common.collect.Iterables;

/**
 * This utility class provides convenience methods to create, use and manage FScript
 * interpreters.
 * 
 * @author Pierre-Charles David
 */
public final class FScript {
    /**
     * The name of the Fractal ADL definition which describes the standard FScript engine
     * configuration.
     */
    public static final String DEFAULT_CONFIGURATION = "org.objectweb.fractal.fscript.FScript";

    /**
     * Locates the {@link NodeFactory} interface of a standard FScript component.
     * 
     * @param fscript
     *            an FScript implementation component.
     * @return the engine's <code>NodeFactory</code> interface
     * @throws IllegalArgumentException
     *             of the supplied engine does not provide the requested interface.
     */
    public static NodeFactory getNodeFactory(Component fscript) {
        try {
            return (NodeFactory) fscript.getFcInterface("node-factory");
        } catch (NoSuchInterfaceException e) {
            throw missingInterfaceError("node-factory");
        }
    }

    /**
     * Locates the {@link ScriptLoader} interface of an FScript component.
     * 
     * @param fscript
     *            an FScript implementation component.
     * @return the engine's <code>ScriptLoader</code> interface
     * @throws IllegalArgumentException
     *             of the supplied engine does not provide the requested interface.
     */
    public static ScriptLoader getScriptLoader(Component fscript) {
        try {
            return (ScriptLoader) fscript.getFcInterface("loader");
        } catch (NoSuchInterfaceException e) {
            throw missingInterfaceError("loader");
        }
    }

    /**
     * Locates the {@link FScriptEngine} interface of an FScript component.
     * 
     * @param fscript
     *            an FScript implementation component.
     * @return the engine's <code>FScriptEngine</code> interface
     * @throws IllegalArgumentException
     *             of the supplied engine does not provide the requested interface.
     */
    public static FScriptEngine getFScriptEngine(Component fscript) {
        try {
            return (FScriptEngine) fscript.getFcInterface("engine");
        } catch (NoSuchInterfaceException e) {
            throw missingInterfaceError("engine");
        }
    }

    /**
     * Gets a single <code>FractalNode</code> out of an FScript value:
     * <ul>
     * <li>If <code>arg</code> is a <code>FractalNode</code>, returns it.</li>
     * <li>If <code>arg</code> is a non-empty nodeset (i.e. a
     * <code>FractalNode[]</code>), returns one element of the set.</li>
     * <li>Otherwise, returns <code>null</code>.</li>
     * </ul>
     * 
     * @param arg
     *            an FScript value
     * @return a single <code>FractalNode</code> from <code>arg</code>
     */
    public static Node getSingleNode(Object arg) {
        if (arg instanceof Node) {
            return (Node) arg;
        } else if (arg instanceof Iterable<?>) {
            return (Node) Iterables.getOnlyElement((Iterable<?>) arg, null);
        } else {
            return null;
        }
    }

    private static IllegalArgumentException missingInterfaceError(String itfName) {
        String msg = "Invalid FScript engine component: missing '" + itfName + "' interface.";
        return new IllegalArgumentException(msg);
    }

    public static Component newEngine() throws Exception {
        return newEngine(Fractal.getBootstrapComponent());
    }

    public static Component newEngine(Component bootstrap) throws Exception {
        TypeFactory typeFactory = Fractal.getTypeFactory(bootstrap);
        GenericFactory genericFactory = Fractal.getGenericFactory(bootstrap);

        // The code below is automatically generated using the static Fractal ADL backend.
        // See the generatedBuildCode() method below.

        try {
            // START OF GENERATED CODE
            InterfaceType IT0 = typeFactory.createFcItfType("script-loader",
                    "org.objectweb.fractal.fscript.ScriptLoader", false, false, false);
            InterfaceType IT1 = typeFactory.createFcItfType("fragment-loader",
                    "org.objectweb.fractal.fscript.FragmentLoader", false, false, false);
            InterfaceType IT2 = typeFactory.createFcItfType("parser",
                    "org.objectweb.fractal.fscript.parser.IFScriptParser", true, false, false);
            InterfaceType IT3 = typeFactory.createFcItfType("language-checker",
                    "org.objectweb.fractal.fscript.LanguageChecker", true, true, false);
            InterfaceType IT4 = typeFactory.createFcItfType("model-checker",
                    "org.objectweb.fractal.fscript.model.ModelChecker", true, true, false);
            InterfaceType IT5 = typeFactory.createFcItfType("library",
                    "org.objectweb.fractal.fscript.Library", true, false, false);
            InterfaceType IT6 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT0 = typeFactory.createFcType(new InterfaceType[] { IT0, IT1, IT2, IT3,
                    IT4, IT5, IT6 });
            Component C0 = genericFactory.newFcInstance(CT0, "primitive",
                    "org.objectweb.fractal.fscript.FrontendDriver");
            try {
                Fractal.getNameController(C0).setFcName("driver");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT7 = typeFactory.createFcItfType("fpath-parser",
                    "org.objectweb.fractal.fscript.parser.IFPathParser", false, false, false);
            InterfaceType IT8 = typeFactory.createFcItfType("fscript-parser",
                    "org.objectweb.fractal.fscript.parser.IFScriptParser", false, false, false);
            ComponentType CT1 = typeFactory.createFcType(new InterfaceType[] { IT7, IT8 });
            Component C1 = genericFactory.newFcInstance(CT1, "primitive",
                    "org.objectweb.fractal.fscript.parser.FScriptParserImpl");
            try {
                Fractal.getNameController(C1).setFcName("parser");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getLifeCycleController(C1).startFc();
            InterfaceType IT9 = typeFactory.createFcItfType("language-checker",
                    "org.objectweb.fractal.fscript.LanguageChecker", false, false, false);
            InterfaceType IT10 = typeFactory.createFcItfType("library",
                    "org.objectweb.fractal.fscript.Library", true, false, false);
            InterfaceType IT11 = typeFactory.createFcItfType("diagnostics",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticListener", true, false,
                    false);
            InterfaceType IT12 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT2 = typeFactory.createFcType(new InterfaceType[] { IT9, IT10, IT11,
                    IT12 });
            Component C2 = genericFactory.newFcInstance(CT2, "primitive",
                    "org.objectweb.fractal.fscript.LanguageCheckerImpl");
            try {
                Fractal.getNameController(C2).setFcName("language-checker");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT13 = typeFactory.createFcItfType("model-checker",
                    "org.objectweb.fractal.fscript.model.ModelChecker", false, false, false);
            InterfaceType IT14 = typeFactory.createFcItfType("model",
                    "org.objectweb.fractal.fscript.model.Model", true, false, false);
            InterfaceType IT15 = typeFactory.createFcItfType("library",
                    "org.objectweb.fractal.fscript.Library", true, false, false);
            InterfaceType IT16 = typeFactory.createFcItfType("diagnostics",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticListener", true, false,
                    false);
            InterfaceType IT17 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT3 = typeFactory.createFcType(new InterfaceType[] { IT13, IT14, IT15,
                    IT16, IT17 });
            Component C3 = genericFactory.newFcInstance(CT3, "primitive",
                    "org.objectweb.fractal.fscript.model.ModelCheckerImpl");
            try {
                Fractal.getNameController(C3).setFcName("model-checker");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT18 = typeFactory.createFcItfType("script-loader",
                    "org.objectweb.fractal.fscript.ScriptLoader", false, false, false);
            InterfaceType IT19 = typeFactory.createFcItfType("fragment-loader",
                    "org.objectweb.fractal.fscript.FragmentLoader", false, false, false);
            InterfaceType IT20 = typeFactory.createFcItfType("model",
                    "org.objectweb.fractal.fscript.model.Model", true, false, false);
            InterfaceType IT21 = typeFactory.createFcItfType("library",
                    "org.objectweb.fractal.fscript.Library", true, false, false);
            InterfaceType IT22 = typeFactory.createFcItfType("diagnostics",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticListener", true, false,
                    false);
            InterfaceType IT23 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT4 = typeFactory.createFcType(new InterfaceType[] { IT18, IT19, IT20,
                    IT21, IT22, IT23 });
            Component C4 = genericFactory.newFcInstance(CT4, "composite", null);
            try {
                Fractal.getNameController(C4).setFcName("front-end");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getContentController(C4).addFcSubComponent(C0);
            Fractal.getContentController(C4).addFcSubComponent(C1);
            Fractal.getContentController(C4).addFcSubComponent(C2);
            Fractal.getContentController(C4).addFcSubComponent(C3);
            Fractal.getBindingController(C4).bindFc("script-loader",
                    C0.getFcInterface("script-loader"));
            Fractal.getBindingController(C4).bindFc("fragment-loader",
                    C0.getFcInterface("fragment-loader"));
            Fractal.getBindingController(C0).bindFc("parser", C1.getFcInterface("fscript-parser"));
            Fractal.getBindingController(C0).bindFc("language-checker",
                    C2.getFcInterface("language-checker"));
            Fractal.getBindingController(C0).bindFc("model-checker",
                    C3.getFcInterface("model-checker"));
            Fractal.getBindingController(C0).bindFc("library",
                    Fractal.getContentController(C4).getFcInternalInterface("library"));
            Fractal.getBindingController(C0).bindFc("logger",
                    Fractal.getContentController(C4).getFcInternalInterface("logger"));
            Fractal.getBindingController(C2).bindFc("library",
                    Fractal.getContentController(C4).getFcInternalInterface("library"));
            Fractal.getBindingController(C2).bindFc("diagnostics",
                    Fractal.getContentController(C4).getFcInternalInterface("diagnostics"));
            Fractal.getBindingController(C2).bindFc("logger",
                    Fractal.getContentController(C4).getFcInternalInterface("logger"));
            Fractal.getBindingController(C3).bindFc("model",
                    Fractal.getContentController(C4).getFcInternalInterface("model"));
            Fractal.getBindingController(C3).bindFc("library",
                    Fractal.getContentController(C4).getFcInternalInterface("library"));
            Fractal.getBindingController(C3).bindFc("diagnostics",
                    Fractal.getContentController(C4).getFcInternalInterface("diagnostics"));
            Fractal.getBindingController(C3).bindFc("logger",
                    Fractal.getContentController(C4).getFcInternalInterface("logger"));
            InterfaceType IT24 = typeFactory.createFcItfType("fscript-engine",
                    "org.objectweb.fractal.fscript.FScriptEngine", false, false, false);
            InterfaceType IT25 = typeFactory.createFcItfType("fragment-loader",
                    "org.objectweb.fractal.fscript.FragmentLoader", true, false, false);
            InterfaceType IT26 = typeFactory.createFcItfType("executor",
                    "org.objectweb.fractal.fscript.Executor", true, false, false);
            InterfaceType IT27 = typeFactory.createFcItfType("simulator",
                    "org.objectweb.fractal.fscript.simulation.Simulator", true, true, false);
            InterfaceType IT28 = typeFactory.createFcItfType("transaction-manager",
                    "javax.transaction.UserTransaction", true, true, false);
            InterfaceType IT29 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT5 = typeFactory.createFcType(new InterfaceType[] { IT24, IT25, IT26,
                    IT27, IT28, IT29 });
            Component C5 = genericFactory.newFcInstance(CT5, "primitive",
                    "org.objectweb.fractal.fscript.BackendDriver");
            try {
                Fractal.getNameController(C5).setFcName("driver");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT30 = typeFactory.createFcItfType("executor",
                    "org.objectweb.fractal.fscript.Executor", false, false, false);
            InterfaceType IT31 = typeFactory.createFcItfType("library",
                    "org.objectweb.fractal.fscript.Library", true, false, false);
            InterfaceType IT32 = typeFactory.createFcItfType("diagnostics",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticListener", true, false,
                    false);
            InterfaceType IT33 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT6 = typeFactory.createFcType(new InterfaceType[] { IT30, IT31, IT32,
                    IT33 });
            Component C6 = genericFactory.newFcInstance(CT6, "primitive",
                    "org.objectweb.fractal.fscript.interpreter.Interpreter");
            try {
                Fractal.getNameController(C6).setFcName("interpreter");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT34 = typeFactory.createFcItfType("fscript-engine",
                    "org.objectweb.fractal.fscript.FScriptEngine", false, false, false);
            InterfaceType IT35 = typeFactory.createFcItfType("fragment-loader",
                    "org.objectweb.fractal.fscript.FragmentLoader", true, false, false);
            InterfaceType IT36 = typeFactory.createFcItfType("library",
                    "org.objectweb.fractal.fscript.Library", true, false, false);
            InterfaceType IT37 = typeFactory.createFcItfType("simulator",
                    "org.objectweb.fractal.fscript.simulation.Simulator", true, true, false);
            InterfaceType IT38 = typeFactory.createFcItfType("diagnostics",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticListener", true, false,
                    false);
            InterfaceType IT39 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT7 = typeFactory.createFcType(new InterfaceType[] { IT34, IT35, IT36,
                    IT37, IT38, IT39 });
            Component C7 = genericFactory.newFcInstance(CT7, "composite", null);
            try {
                Fractal.getNameController(C7).setFcName("back-end");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getContentController(C7).addFcSubComponent(C5);
            Fractal.getContentController(C7).addFcSubComponent(C6);
            Fractal.getBindingController(C7).bindFc("fscript-engine",
                    C5.getFcInterface("fscript-engine"));
            Fractal.getBindingController(C5).bindFc("fragment-loader",
                    Fractal.getContentController(C7).getFcInternalInterface("fragment-loader"));
            Fractal.getBindingController(C5).bindFc("executor", C6.getFcInterface("executor"));
            Fractal.getBindingController(C5).bindFc("logger",
                    Fractal.getContentController(C7).getFcInternalInterface("logger"));
            Fractal.getBindingController(C6).bindFc("library",
                    Fractal.getContentController(C7).getFcInternalInterface("library"));
            Fractal.getBindingController(C6).bindFc("diagnostics",
                    Fractal.getContentController(C7).getFcInternalInterface("diagnostics"));
            Fractal.getBindingController(C6).bindFc("logger",
                    Fractal.getContentController(C7).getFcInternalInterface("logger"));
            InterfaceType IT40 = typeFactory.createFcItfType("node-factory",
                    "org.objectweb.fractal.fscript.model.fractal.NodeFactory", false, false, false);
            InterfaceType IT41 = typeFactory.createFcItfType("model",
                    "org.objectweb.fractal.fscript.model.Model", false, false, false);
            InterfaceType IT42 = typeFactory.createFcItfType("native-library",
                    "org.objectweb.fractal.fscript.procedures.NativeLibrary", false, false, false);
            InterfaceType IT43 = typeFactory.createFcItfType("adl-factory",
                    "org.objectweb.fractal.adl.Factory", true, false, false);
            InterfaceType IT44 = typeFactory.createFcItfType("attribute-controller",
                    "org.objectweb.fractal.fscript.model.fractal.FractalModelAttributes", false,
                    false, false);
            ComponentType CT8 = typeFactory.createFcType(new InterfaceType[] { IT40, IT41, IT42,
                    IT43, IT44 });
            Component C8 = genericFactory.newFcInstance(CT8, "primitive",
                    "org.objectweb.fractal.fscript.model.fractal.FractalModel");
            try {
                Fractal.getNameController(C8).setFcName("model");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT45 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", true, false, false);
            InterfaceType IT46 = typeFactory.createFcItfType("compiler",
                    "org.objectweb.fractal.adl.Compiler", true, false, false);
            InterfaceType IT47 = typeFactory.createFcItfType("scheduler",
                    "org.objectweb.deployment.scheduling.core.api.Scheduler", true, false, false);
            InterfaceType IT48 = typeFactory.createFcItfType("factory",
                    "org.objectweb.fractal.adl.Factory", false, false, false);
            ComponentType CT9 = typeFactory.createFcType(new InterfaceType[] { IT45, IT46, IT47,
                    IT48 });
            Component C9 = genericFactory.newFcInstance(CT9, "primitive",
                    "org.objectweb.fractal.adl.BasicFactory");
            try {
                Fractal.getNameController(C9).setFcName("factory");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT49 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            ComponentType CT10 = typeFactory.createFcType(new InterfaceType[] { IT49 });
            Component C10 = genericFactory.newFcInstance(CT10, "primitive",
                    "org.objectweb.fractal.adl.xml.XMLLoader");
            try {
                Fractal.getNameController(C10).setFcName("basic-loader");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getLifeCycleController(C10).startFc();
            InterfaceType IT50 = typeFactory.createFcItfType("client-loader",
                    "org.objectweb.fractal.adl.Loader", true, false, false);
            InterfaceType IT51 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            ComponentType CT11 = typeFactory.createFcType(new InterfaceType[] { IT50, IT51 });
            Component C11 = genericFactory.newFcInstance(CT11, "primitive",
                    "org.objectweb.fractal.adl.arguments.ArgumentLoader");
            try {
                Fractal.getNameController(C11).setFcName("argument-loader");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT52 = typeFactory.createFcItfType("client-loader",
                    "org.objectweb.fractal.adl.Loader", true, false, false);
            InterfaceType IT53 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            ComponentType CT12 = typeFactory.createFcType(new InterfaceType[] { IT52, IT53 });
            Component C12 = genericFactory.newFcInstance(CT12, "primitive",
                    "org.objectweb.fractal.adl.interfaces.InterfaceLoader");
            try {
                Fractal.getNameController(C12).setFcName("interface-loader");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT54 = typeFactory.createFcItfType("client-loader",
                    "org.objectweb.fractal.adl.Loader", true, false, false);
            InterfaceType IT55 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            ComponentType CT13 = typeFactory.createFcType(new InterfaceType[] { IT54, IT55 });
            Component C13 = genericFactory.newFcInstance(CT13, "primitive",
                    "org.objectweb.fractal.adl.types.TypeLoader");
            try {
                Fractal.getNameController(C13).setFcName("type-loader");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT56 = typeFactory.createFcItfType("client-loader",
                    "org.objectweb.fractal.adl.Loader", true, false, false);
            InterfaceType IT57 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            ComponentType CT14 = typeFactory.createFcType(new InterfaceType[] { IT56, IT57 });
            Component C14 = genericFactory.newFcInstance(CT14, "primitive",
                    "org.objectweb.fractal.adl.implementations.ImplementationLoader");
            try {
                Fractal.getNameController(C14).setFcName("implementation-loader");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT58 = typeFactory.createFcItfType("client-loader",
                    "org.objectweb.fractal.adl.Loader", true, false, false);
            InterfaceType IT59 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            ComponentType CT15 = typeFactory.createFcType(new InterfaceType[] { IT58, IT59 });
            Component C15 = genericFactory.newFcInstance(CT15, "primitive",
                    "org.objectweb.fractal.adl.attributes.AttributeLoader");
            try {
                Fractal.getNameController(C15).setFcName("attribute-loader");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT60 = typeFactory.createFcItfType("client-loader",
                    "org.objectweb.fractal.adl.Loader", true, false, false);
            InterfaceType IT61 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            InterfaceType IT62 = typeFactory.createFcItfType("attribute-controller",
                    "org.objectweb.fractal.adl.components.ComponentLoaderAttributes", false, false,
                    false);
            ComponentType CT16 = typeFactory.createFcType(new InterfaceType[] { IT60, IT61, IT62 });
            Component C16 = genericFactory.newFcInstance(CT16, "primitive",
                    "org.objectweb.fractal.adl.arguments.ArgumentComponentLoader");
            try {
                Fractal.getNameController(C16).setFcName("component-loader");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT63 = typeFactory.createFcItfType("client-loader",
                    "org.objectweb.fractal.adl.Loader", true, false, false);
            InterfaceType IT64 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            ComponentType CT17 = typeFactory.createFcType(new InterfaceType[] { IT63, IT64 });
            Component C17 = genericFactory.newFcInstance(CT17, "primitive",
                    "org.objectweb.fractal.adl.bindings.TypeBindingLoader");
            try {
                Fractal.getNameController(C17).setFcName("binding-loader");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT65 = typeFactory.createFcItfType("client-loader",
                    "org.objectweb.fractal.adl.Loader", true, false, false);
            InterfaceType IT66 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            ComponentType CT18 = typeFactory.createFcType(new InterfaceType[] { IT65, IT66 });
            Component C18 = genericFactory.newFcInstance(CT18, "primitive",
                    "org.objectweb.fractal.adl.bindings.UnboundInterfaceChecker");
            try {
                Fractal.getNameController(C18).setFcName("unbound-interface-checker");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT67 = typeFactory.createFcItfType("client-loader",
                    "org.objectweb.fractal.adl.Loader", true, false, false);
            InterfaceType IT68 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            ComponentType CT19 = typeFactory.createFcType(new InterfaceType[] { IT67, IT68 });
            Component C19 = genericFactory.newFcInstance(CT19, "primitive",
                    "org.objectweb.fractal.adl.attributes.AttributeLoader");
            try {
                Fractal.getNameController(C19).setFcName("post-attribute-loader");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT69 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.adl.Loader", false, false, false);
            ComponentType CT20 = typeFactory.createFcType(new InterfaceType[] { IT69 });
            Component C20 = genericFactory.newFcInstance(CT20, "composite", null);
            try {
                Fractal.getNameController(C20).setFcName("loader");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getContentController(C20).addFcSubComponent(C10);
            Fractal.getContentController(C20).addFcSubComponent(C11);
            Fractal.getContentController(C20).addFcSubComponent(C12);
            Fractal.getContentController(C20).addFcSubComponent(C13);
            Fractal.getContentController(C20).addFcSubComponent(C14);
            Fractal.getContentController(C20).addFcSubComponent(C15);
            Fractal.getContentController(C20).addFcSubComponent(C16);
            Fractal.getContentController(C20).addFcSubComponent(C17);
            Fractal.getContentController(C20).addFcSubComponent(C18);
            Fractal.getContentController(C20).addFcSubComponent(C19);
            Fractal.getBindingController(C20).bindFc("loader", C19.getFcInterface("loader"));
            Fractal.getBindingController(C19).bindFc("client-loader", C18.getFcInterface("loader"));
            Fractal.getBindingController(C18).bindFc("client-loader", C17.getFcInterface("loader"));
            Fractal.getBindingController(C17).bindFc("client-loader", C16.getFcInterface("loader"));
            Fractal.getBindingController(C16).bindFc("client-loader", C15.getFcInterface("loader"));
            Fractal.getBindingController(C15).bindFc("client-loader", C14.getFcInterface("loader"));
            Fractal.getBindingController(C14).bindFc("client-loader", C13.getFcInterface("loader"));
            Fractal.getBindingController(C13).bindFc("client-loader", C12.getFcInterface("loader"));
            Fractal.getBindingController(C12).bindFc("client-loader", C11.getFcInterface("loader"));
            Fractal.getBindingController(C11).bindFc("client-loader", C10.getFcInterface("loader"));
            InterfaceType IT70 = typeFactory.createFcItfType("primitive-compilers",
                    "org.objectweb.fractal.adl.components.PrimitiveCompiler", true, true, true);
            InterfaceType IT71 = typeFactory.createFcItfType("compiler",
                    "org.objectweb.fractal.adl.Compiler", false, false, false);
            ComponentType CT21 = typeFactory.createFcType(new InterfaceType[] { IT70, IT71 });
            Component C21 = genericFactory.newFcInstance(CT21, "primitive",
                    "org.objectweb.fractal.adl.components.ComponentCompiler");
            try {
                Fractal.getNameController(C21).setFcName("main-compiler");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT72 = typeFactory.createFcItfType("builder",
                    "org.objectweb.fractal.adl.types.TypeBuilder", true, false, false);
            InterfaceType IT73 = typeFactory.createFcItfType("compiler",
                    "org.objectweb.fractal.adl.components.PrimitiveCompiler", false, false, false);
            ComponentType CT22 = typeFactory.createFcType(new InterfaceType[] { IT72, IT73 });
            Component C22 = genericFactory.newFcInstance(CT22, "primitive",
                    "org.objectweb.fractal.adl.types.TypeCompiler");
            try {
                Fractal.getNameController(C22).setFcName("type-compiler");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT74 = typeFactory.createFcItfType("builder",
                    "org.objectweb.fractal.adl.implementations.ImplementationBuilder", true, false,
                    false);
            InterfaceType IT75 = typeFactory.createFcItfType("compiler",
                    "org.objectweb.fractal.adl.components.PrimitiveCompiler", false, false, false);
            ComponentType CT23 = typeFactory.createFcType(new InterfaceType[] { IT74, IT75 });
            Component C23 = genericFactory.newFcInstance(CT23, "primitive",
                    "org.objectweb.fractal.adl.nodes.VirtualNodeImplementationCompiler");
            try {
                Fractal.getNameController(C23).setFcName("implementation-compiler");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT76 = typeFactory.createFcItfType("builder",
                    "org.objectweb.fractal.adl.components.ComponentBuilder", true, false, false);
            InterfaceType IT77 = typeFactory.createFcItfType("compiler",
                    "org.objectweb.fractal.adl.components.PrimitiveCompiler", false, false, false);
            ComponentType CT24 = typeFactory.createFcType(new InterfaceType[] { IT76, IT77 });
            Component C24 = genericFactory.newFcInstance(CT24, "primitive",
                    "org.objectweb.fractal.adl.components.PrimitiveComponentCompiler");
            try {
                Fractal.getNameController(C24).setFcName("component-compiler");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT78 = typeFactory.createFcItfType("builder",
                    "org.objectweb.fractal.adl.bindings.BindingBuilder", true, false, false);
            InterfaceType IT79 = typeFactory.createFcItfType("compiler",
                    "org.objectweb.fractal.adl.components.PrimitiveCompiler", false, false, false);
            ComponentType CT25 = typeFactory.createFcType(new InterfaceType[] { IT78, IT79 });
            Component C25 = genericFactory.newFcInstance(CT25, "primitive",
                    "org.objectweb.fractal.adl.bindings.BindingCompiler");
            try {
                Fractal.getNameController(C25).setFcName("binding-compiler");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT80 = typeFactory.createFcItfType("builder",
                    "org.objectweb.fractal.adl.attributes.AttributeBuilder", true, false, false);
            InterfaceType IT81 = typeFactory.createFcItfType("compiler",
                    "org.objectweb.fractal.adl.components.PrimitiveCompiler", false, false, false);
            ComponentType CT26 = typeFactory.createFcType(new InterfaceType[] { IT80, IT81 });
            Component C26 = genericFactory.newFcInstance(CT26, "primitive",
                    "org.objectweb.fractal.adl.attributes.AttributeCompiler");
            try {
                Fractal.getNameController(C26).setFcName("attribute-compiler");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT82 = typeFactory.createFcItfType("type-builder",
                    "org.objectweb.fractal.adl.types.TypeBuilder", true, false, false);
            InterfaceType IT83 = typeFactory.createFcItfType("implementation-builder",
                    "org.objectweb.fractal.adl.implementations.ImplementationBuilder", true, false,
                    false);
            InterfaceType IT84 = typeFactory.createFcItfType("component-builder",
                    "org.objectweb.fractal.adl.components.ComponentBuilder", true, false, false);
            InterfaceType IT85 = typeFactory.createFcItfType("binding-builder",
                    "org.objectweb.fractal.adl.bindings.BindingBuilder", true, false, false);
            InterfaceType IT86 = typeFactory.createFcItfType("attribute-builder",
                    "org.objectweb.fractal.adl.attributes.AttributeBuilder", true, false, false);
            InterfaceType IT87 = typeFactory.createFcItfType("compiler",
                    "org.objectweb.fractal.adl.Compiler", false, false, false);
            ComponentType CT27 = typeFactory.createFcType(new InterfaceType[] { IT82, IT83, IT84,
                    IT85, IT86, IT87 });
            Component C27 = genericFactory.newFcInstance(CT27, "composite", null);
            try {
                Fractal.getNameController(C27).setFcName("compiler");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getContentController(C27).addFcSubComponent(C21);
            Fractal.getContentController(C27).addFcSubComponent(C22);
            Fractal.getContentController(C27).addFcSubComponent(C23);
            Fractal.getContentController(C27).addFcSubComponent(C24);
            Fractal.getContentController(C27).addFcSubComponent(C25);
            Fractal.getContentController(C27).addFcSubComponent(C26);
            Fractal.getBindingController(C27).bindFc("compiler", C21.getFcInterface("compiler"));
            Fractal.getBindingController(C21).bindFc("primitive-compilers0",
                    C22.getFcInterface("compiler"));
            Fractal.getBindingController(C21).bindFc("primitive-compilers1",
                    C23.getFcInterface("compiler"));
            Fractal.getBindingController(C21).bindFc("primitive-compilers2",
                    C24.getFcInterface("compiler"));
            Fractal.getBindingController(C21).bindFc("primitive-compilers3",
                    C25.getFcInterface("compiler"));
            Fractal.getBindingController(C21).bindFc("primitive-compilers4",
                    C26.getFcInterface("compiler"));
            Fractal.getBindingController(C22).bindFc("builder",
                    Fractal.getContentController(C27).getFcInternalInterface("type-builder"));
            Fractal.getBindingController(C23).bindFc(
                    "builder",
                    Fractal.getContentController(C27).getFcInternalInterface(
                            "implementation-builder"));
            Fractal.getBindingController(C24).bindFc("builder",
                    Fractal.getContentController(C27).getFcInternalInterface("component-builder"));
            Fractal.getBindingController(C25).bindFc("builder",
                    Fractal.getContentController(C27).getFcInternalInterface("binding-builder"));
            Fractal.getBindingController(C26).bindFc("builder",
                    Fractal.getContentController(C27).getFcInternalInterface("attribute-builder"));
            InterfaceType IT88 = typeFactory.createFcItfType("type-builder",
                    "org.objectweb.fractal.adl.types.TypeBuilder", false, false, false);
            ComponentType CT28 = typeFactory.createFcType(new InterfaceType[] { IT88 });
            Component C28 = genericFactory.newFcInstance(CT28, "primitive",
                    "org.objectweb.fractal.adl.types.FractalTypeBuilder");
            try {
                Fractal.getNameController(C28).setFcName("type-builder");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getLifeCycleController(C28).startFc();
            InterfaceType IT89 = typeFactory.createFcItfType("implementation-builder",
                    "org.objectweb.fractal.adl.implementations.ImplementationBuilder", false,
                    false, false);
            ComponentType CT29 = typeFactory.createFcType(new InterfaceType[] { IT89 });
            Component C29 = genericFactory.newFcInstance(CT29, "primitive",
                    "org.objectweb.fractal.adl.implementations.FractalImplementationBuilder");
            try {
                Fractal.getNameController(C29).setFcName("implementation-builder");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getLifeCycleController(C29).startFc();
            InterfaceType IT90 = typeFactory.createFcItfType("component-builder",
                    "org.objectweb.fractal.adl.components.ComponentBuilder", false, false, false);
            ComponentType CT30 = typeFactory.createFcType(new InterfaceType[] { IT90 });
            Component C30 = genericFactory.newFcInstance(CT30, "primitive",
                    "org.objectweb.fractal.adl.components.FractalComponentBuilder");
            try {
                Fractal.getNameController(C30).setFcName("component-builder");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getLifeCycleController(C30).startFc();
            InterfaceType IT91 = typeFactory.createFcItfType("binding-builder",
                    "org.objectweb.fractal.adl.bindings.BindingBuilder", false, false, false);
            ComponentType CT31 = typeFactory.createFcType(new InterfaceType[] { IT91 });
            Component C31 = genericFactory.newFcInstance(CT31, "primitive",
                    "org.objectweb.fractal.adl.bindings.FractalBindingBuilder");
            try {
                Fractal.getNameController(C31).setFcName("binding-builder");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getLifeCycleController(C31).startFc();
            InterfaceType IT92 = typeFactory.createFcItfType("attribute-builder",
                    "org.objectweb.fractal.adl.attributes.AttributeBuilder", false, false, false);
            ComponentType CT32 = typeFactory.createFcType(new InterfaceType[] { IT92 });
            Component C32 = genericFactory.newFcInstance(CT32, "primitive",
                    "org.objectweb.fractal.adl.attributes.FractalAttributeBuilder");
            try {
                Fractal.getNameController(C32).setFcName("attribute-builder");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getLifeCycleController(C32).startFc();
            InterfaceType IT93 = typeFactory.createFcItfType("type-builder",
                    "org.objectweb.fractal.adl.types.TypeBuilder", false, false, false);
            InterfaceType IT94 = typeFactory.createFcItfType("implementation-builder",
                    "org.objectweb.fractal.adl.implementations.ImplementationBuilder", false,
                    false, false);
            InterfaceType IT95 = typeFactory.createFcItfType("component-builder",
                    "org.objectweb.fractal.adl.components.ComponentBuilder", false, false, false);
            InterfaceType IT96 = typeFactory.createFcItfType("binding-builder",
                    "org.objectweb.fractal.adl.bindings.BindingBuilder", false, false, false);
            InterfaceType IT97 = typeFactory.createFcItfType("attribute-builder",
                    "org.objectweb.fractal.adl.attributes.AttributeBuilder", false, false, false);
            ComponentType CT33 = typeFactory.createFcType(new InterfaceType[] { IT93, IT94, IT95,
                    IT96, IT97 });
            Component C33 = genericFactory.newFcInstance(CT33, "composite", null);
            try {
                Fractal.getNameController(C33).setFcName("backend");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getContentController(C33).addFcSubComponent(C28);
            Fractal.getContentController(C33).addFcSubComponent(C29);
            Fractal.getContentController(C33).addFcSubComponent(C30);
            Fractal.getContentController(C33).addFcSubComponent(C31);
            Fractal.getContentController(C33).addFcSubComponent(C32);
            Fractal.getBindingController(C33).bindFc("type-builder",
                    C28.getFcInterface("type-builder"));
            Fractal.getBindingController(C33).bindFc("implementation-builder",
                    C29.getFcInterface("implementation-builder"));
            Fractal.getBindingController(C33).bindFc("component-builder",
                    C30.getFcInterface("component-builder"));
            Fractal.getBindingController(C33).bindFc("binding-builder",
                    C31.getFcInterface("binding-builder"));
            Fractal.getBindingController(C33).bindFc("attribute-builder",
                    C32.getFcInterface("attribute-builder"));
            InterfaceType IT98 = typeFactory.createFcItfType("scheduler",
                    "org.objectweb.deployment.scheduling.core.api.Scheduler", false, false, false);
            ComponentType CT34 = typeFactory.createFcType(new InterfaceType[] { IT98 });
            Component C34 = genericFactory.newFcInstance(CT34, "primitive",
                    "org.objectweb.deployment.scheduling.core.lib.BasicScheduler");
            try {
                Fractal.getNameController(C34).setFcName("scheduler");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getLifeCycleController(C34).startFc();
            InterfaceType IT99 = typeFactory.createFcItfType("factory",
                    "org.objectweb.fractal.adl.Factory", false, false, false);
            ComponentType CT35 = typeFactory.createFcType(new InterfaceType[] { IT99 });
            Component C35 = genericFactory.newFcInstance(CT35, "composite", null);
            try {
                Fractal.getNameController(C35).setFcName("adl-factory");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getContentController(C35).addFcSubComponent(C9);
            Fractal.getContentController(C35).addFcSubComponent(C20);
            Fractal.getContentController(C35).addFcSubComponent(C27);
            Fractal.getContentController(C35).addFcSubComponent(C33);
            Fractal.getContentController(C35).addFcSubComponent(C34);
            Fractal.getBindingController(C35).bindFc("factory", C9.getFcInterface("factory"));
            Fractal.getBindingController(C9).bindFc("loader", C20.getFcInterface("loader"));
            Fractal.getBindingController(C9).bindFc("compiler", C27.getFcInterface("compiler"));
            Fractal.getBindingController(C9).bindFc("scheduler", C34.getFcInterface("scheduler"));
            Fractal.getBindingController(C27).bindFc("type-builder",
                    C33.getFcInterface("type-builder"));
            Fractal.getBindingController(C27).bindFc("implementation-builder",
                    C33.getFcInterface("implementation-builder"));
            Fractal.getBindingController(C27).bindFc("component-builder",
                    C33.getFcInterface("component-builder"));
            Fractal.getBindingController(C27).bindFc("binding-builder",
                    C33.getFcInterface("binding-builder"));
            Fractal.getBindingController(C27).bindFc("attribute-builder",
                    C33.getFcInterface("attribute-builder"));
            InterfaceType IT100 = typeFactory.createFcItfType("library",
                    "org.objectweb.fractal.fscript.Library", false, false, false);
            InterfaceType IT101 = typeFactory.createFcItfType("model-library",
                    "org.objectweb.fractal.fscript.procedures.NativeLibrary", true, false, false);
            InterfaceType IT102 = typeFactory.createFcItfType("diagnostics",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticListener", true, false,
                    false);
            InterfaceType IT103 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT36 = typeFactory.createFcType(new InterfaceType[] { IT100, IT101,
                    IT102, IT103 });
            Component C36 = genericFactory.newFcInstance(CT36, "primitive",
                    "org.objectweb.fractal.fscript.LibraryImpl");
            try {
                Fractal.getNameController(C36).setFcName("library");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT104 = typeFactory.createFcItfType("simulator",
                    "org.objectweb.fractal.fscript.simulation.Simulator", false, false, false);
            InterfaceType IT105 = typeFactory.createFcItfType("library",
                    "org.objectweb.fractal.fscript.Library", true, false, false);
            InterfaceType IT106 = typeFactory.createFcItfType("derived-node-mapper",
                    "org.objectweb.fractal.fscript.simulation.DerivedNodeMapper", true, false,
                    false);
            InterfaceType IT107 = typeFactory.createFcItfType("tracer",
                    "org.objectweb.fractal.fscript.simulation.ModelTracer", true, false, false);
            InterfaceType IT108 = typeFactory.createFcItfType("diagnostics",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticListener", true, false,
                    false);
            InterfaceType IT109 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT37 = typeFactory.createFcType(new InterfaceType[] { IT104, IT105,
                    IT106, IT107, IT108, IT109 });
            Component C37 = genericFactory.newFcInstance(CT37, "primitive",
                    "org.objectweb.fractal.fscript.simulation.SimulationDriver");
            try {
                Fractal.getNameController(C37).setFcName("driver");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT110 = typeFactory.createFcItfType("model",
                    "org.objectweb.fractal.fscript.model.Model", false, false, false);
            InterfaceType IT111 = typeFactory.createFcItfType("native-library",
                    "org.objectweb.fractal.fscript.procedures.NativeLibrary", false, false, false);
            InterfaceType IT112 = typeFactory.createFcItfType("tracer",
                    "org.objectweb.fractal.fscript.simulation.ModelTracer", false, false, false);
            InterfaceType IT113 = typeFactory.createFcItfType("derived-node-mapper",
                    "org.objectweb.fractal.fscript.simulation.DerivedNodeMapper", false, false,
                    false);
            InterfaceType IT114 = typeFactory.createFcItfType("base-model",
                    "org.objectweb.fractal.fscript.model.Model", true, false, false);
            ComponentType CT38 = typeFactory.createFcType(new InterfaceType[] { IT110, IT111,
                    IT112, IT113, IT114 });
            Component C38 = genericFactory.newFcInstance(CT38, "primitive",
                    "org.objectweb.fractal.fscript.simulation.DerivedModel");
            try {
                Fractal.getNameController(C38).setFcName("simulation-model");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT115 = typeFactory.createFcItfType("library",
                    "org.objectweb.fractal.fscript.Library", false, false, false);
            InterfaceType IT116 = typeFactory.createFcItfType("base-library",
                    "org.objectweb.fractal.fscript.Library", true, false, false);
            InterfaceType IT117 = typeFactory.createFcItfType("model-library",
                    "org.objectweb.fractal.fscript.procedures.NativeLibrary", true, false, false);
            InterfaceType IT118 = typeFactory.createFcItfType("diagnostics",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticListener", true, false,
                    false);
            InterfaceType IT119 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT39 = typeFactory.createFcType(new InterfaceType[] { IT115, IT116,
                    IT117, IT118, IT119 });
            Component C39 = genericFactory.newFcInstance(CT39, "primitive",
                    "org.objectweb.fractal.fscript.simulation.SimulationLibrary");
            try {
                Fractal.getNameController(C39).setFcName("simulation-lib");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT120 = typeFactory.createFcItfType("simulator",
                    "org.objectweb.fractal.fscript.simulation.Simulator", false, false, false);
            InterfaceType IT121 = typeFactory.createFcItfType("library",
                    "org.objectweb.fractal.fscript.Library", true, false, false);
            InterfaceType IT122 = typeFactory.createFcItfType("model",
                    "org.objectweb.fractal.fscript.model.Model", true, false, false);
            InterfaceType IT123 = typeFactory.createFcItfType("diagnostics",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticListener", true, false,
                    false);
            InterfaceType IT124 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, false, false);
            ComponentType CT40 = typeFactory.createFcType(new InterfaceType[] { IT120, IT121,
                    IT122, IT123, IT124 });
            Component C40 = genericFactory.newFcInstance(CT40, "composite", null);
            try {
                Fractal.getNameController(C40).setFcName("simulator");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getContentController(C40).addFcSubComponent(C37);
            Fractal.getContentController(C40).addFcSubComponent(C38);
            Fractal.getContentController(C40).addFcSubComponent(C39);
            Fractal.getBindingController(C40).bindFc("simulator", C37.getFcInterface("simulator"));
            Fractal.getBindingController(C37).bindFc("derived-node-mapper",
                    C38.getFcInterface("derived-node-mapper"));
            Fractal.getBindingController(C37).bindFc("tracer", C38.getFcInterface("tracer"));
            Fractal.getBindingController(C37).bindFc("library", C39.getFcInterface("library"));
            Fractal.getBindingController(C37).bindFc("diagnostics",
                    Fractal.getContentController(C40).getFcInternalInterface("diagnostics"));
            Fractal.getBindingController(C37).bindFc("logger",
                    Fractal.getContentController(C40).getFcInternalInterface("logger"));
            Fractal.getBindingController(C38).bindFc("base-model",
                    Fractal.getContentController(C40).getFcInternalInterface("model"));
            Fractal.getBindingController(C39).bindFc("base-library",
                    Fractal.getContentController(C40).getFcInternalInterface("library"));
            Fractal.getBindingController(C39).bindFc("model-library",
                    C38.getFcInterface("native-library"));
            Fractal.getBindingController(C39).bindFc("diagnostics",
                    Fractal.getContentController(C40).getFcInternalInterface("diagnostics"));
            Fractal.getBindingController(C39).bindFc("logger",
                    Fractal.getContentController(C40).getFcInternalInterface("logger"));
            InterfaceType IT125 = typeFactory.createFcItfType("diagnostics-listener",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticListener", false, false,
                    false);
            InterfaceType IT126 = typeFactory.createFcItfType("diagnostics-log",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticLog", false, false, false);
            InterfaceType IT127 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", true, true, false);
            ComponentType CT41 = typeFactory
                    .createFcType(new InterfaceType[] { IT125, IT126, IT127 });
            Component C41 = genericFactory.newFcInstance(CT41, "primitive",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticCollector");
            try {
                Fractal.getNameController(C41).setFcName("diagnostics");
            } catch (NoSuchInterfaceException ignored) {
            }
            InterfaceType IT128 = typeFactory.createFcItfType("logger",
                    "org.objectweb.util.monolog.api.Logger", false, false, false);
            ComponentType CT42 = typeFactory.createFcType(new InterfaceType[] { IT128 });
            Component C42 = genericFactory.newFcInstance(CT42, "primitive",
                    "org.objectweb.fractal.fscript.LoggerImpl");
            try {
                Fractal.getNameController(C42).setFcName("logger");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getLifeCycleController(C42).startFc();
            InterfaceType IT129 = typeFactory.createFcItfType("node-factory",
                    "org.objectweb.fractal.fscript.model.fractal.NodeFactory", false, false, false);
            InterfaceType IT130 = typeFactory.createFcItfType("loader",
                    "org.objectweb.fractal.fscript.ScriptLoader", false, false, false);
            InterfaceType IT131 = typeFactory.createFcItfType("engine",
                    "org.objectweb.fractal.fscript.FScriptEngine", false, false, false);
            InterfaceType IT132 = typeFactory.createFcItfType("diagnostics",
                    "org.objectweb.fractal.fscript.diagnostics.DiagnosticLog", false, false, false);
            ComponentType CT43 = typeFactory.createFcType(new InterfaceType[] { IT129, IT130,
                    IT131, IT132 });
            Component C43 = genericFactory.newFcInstance(CT43, "composite", null);
            try {
                Fractal.getNameController(C43).setFcName("org.objectweb.fractal.fscript.FScript");
            } catch (NoSuchInterfaceException ignored) {
            }
            Fractal.getContentController(C43).addFcSubComponent(C4);
            Fractal.getContentController(C43).addFcSubComponent(C7);
            Fractal.getContentController(C43).addFcSubComponent(C8);
            Fractal.getContentController(C43).addFcSubComponent(C35);
            Fractal.getContentController(C43).addFcSubComponent(C36);
            Fractal.getContentController(C43).addFcSubComponent(C40);
            Fractal.getContentController(C43).addFcSubComponent(C41);
            Fractal.getContentController(C43).addFcSubComponent(C42);
            Fractal.getBindingController(C43).bindFc("node-factory",
                    C8.getFcInterface("node-factory"));
            Fractal.getBindingController(C43).bindFc("loader", C4.getFcInterface("script-loader"));
            Fractal.getBindingController(C43).bindFc("engine", C7.getFcInterface("fscript-engine"));
            Fractal.getBindingController(C43).bindFc("diagnostics",
                    C41.getFcInterface("diagnostics-log"));
            Fractal.getBindingController(C4).bindFc("model", C8.getFcInterface("model"));
            Fractal.getBindingController(C4).bindFc("library", C36.getFcInterface("library"));
            Fractal.getBindingController(C4).bindFc("diagnostics",
                    C41.getFcInterface("diagnostics-listener"));
            Fractal.getBindingController(C4).bindFc("logger", C42.getFcInterface("logger"));
            Fractal.getBindingController(C7).bindFc("fragment-loader",
                    C4.getFcInterface("fragment-loader"));
            Fractal.getBindingController(C7).bindFc("library", C36.getFcInterface("library"));
            Fractal.getBindingController(C7).bindFc("diagnostics",
                    C41.getFcInterface("diagnostics-listener"));
            Fractal.getBindingController(C7).bindFc("logger", C42.getFcInterface("logger"));
            Fractal.getBindingController(C8).bindFc("adl-factory", C35.getFcInterface("factory"));
            Fractal.getBindingController(C36).bindFc("model-library",
                    C8.getFcInterface("native-library"));
            Fractal.getBindingController(C36).bindFc("diagnostics",
                    C41.getFcInterface("diagnostics-listener"));
            Fractal.getBindingController(C36).bindFc("logger", C42.getFcInterface("logger"));
            Fractal.getBindingController(C40).bindFc("model", C8.getFcInterface("model"));
            Fractal.getBindingController(C40).bindFc("library", C36.getFcInterface("library"));
            Fractal.getBindingController(C40).bindFc("diagnostics",
                    C41.getFcInterface("diagnostics-listener"));
            Fractal.getBindingController(C40).bindFc("logger", C42.getFcInterface("logger"));
            Fractal.getBindingController(C41).bindFc("logger", C42.getFcInterface("logger"));
            Fractal.getLifeCycleController(C0).startFc();
            Fractal.getLifeCycleController(C2).startFc();
            Fractal.getLifeCycleController(C3).startFc();
            Fractal.getLifeCycleController(C4).startFc();
            Fractal.getLifeCycleController(C5).startFc();
            Fractal.getLifeCycleController(C6).startFc();
            Fractal.getLifeCycleController(C7).startFc();
            Fractal.getLifeCycleController(C8).startFc();
            Fractal.getLifeCycleController(C9).startFc();
            Fractal.getLifeCycleController(C11).startFc();
            Fractal.getLifeCycleController(C12).startFc();
            Fractal.getLifeCycleController(C13).startFc();
            Fractal.getLifeCycleController(C14).startFc();
            Fractal.getLifeCycleController(C15).startFc();
            Fractal.getLifeCycleController(C16).startFc();
            Fractal.getLifeCycleController(C17).startFc();
            Fractal.getLifeCycleController(C18).startFc();
            Fractal.getLifeCycleController(C19).startFc();
            Fractal.getLifeCycleController(C20).startFc();
            Fractal.getLifeCycleController(C21).startFc();
            Fractal.getLifeCycleController(C22).startFc();
            Fractal.getLifeCycleController(C23).startFc();
            Fractal.getLifeCycleController(C24).startFc();
            Fractal.getLifeCycleController(C25).startFc();
            Fractal.getLifeCycleController(C26).startFc();
            Fractal.getLifeCycleController(C27).startFc();
            Fractal.getLifeCycleController(C33).startFc();
            Fractal.getLifeCycleController(C35).startFc();
            Fractal.getLifeCycleController(C36).startFc();
            Fractal.getLifeCycleController(C37).startFc();
            Fractal.getLifeCycleController(C38).startFc();
            Fractal.getLifeCycleController(C39).startFc();
            Fractal.getLifeCycleController(C40).startFc();
            Fractal.getLifeCycleController(C41).startFc();
            Fractal.getLifeCycleController(C43).startFc();

            // END OF GENERATED CODE

            loadStandardLibrary(C43);
            return C43;
        } catch (IllegalLifeCycleException e) {
            throw new AssertionError("Internal error: unable to start default FScript engine.");
        } catch (NoSuchInterfaceException e) {
            throw new AssertionError("Internal error: inconsistent default FScript engine.");
        } catch (InvalidScriptException e) {
            throw new AssertionError("Internal error: unable to load standard library.");
        }
    }

    /**
     * Loads the FScript standard library into an already created FScript engine. Use this
     * method if you do not use {@link #newEngine()} but create your own FScript engine
     * (for example using an alternative Fractal ADL definition), to make sure all the
     * basic predefined procedures are available.
     * 
     * @param fscript
     *            the FScript engine in which to load the standard library.
     * @throws NoSuchInterfaceException
     *             if the engine's <code>ScriptLoader</code> interface could not be
     *             found.
     * @throws InvalidScriptException
     *             if an error occurred while loading the standard library.
     */
    public static void loadStandardLibrary(Component fscript) throws NoSuchInterfaceException,
            InvalidScriptException {
        ScriptLoader loader = (ScriptLoader) fscript.getFcInterface("loader");
        InputStream stdlib = FScript.class.getResourceAsStream("stdlib.fscript");
        loader.load(new InputStreamReader(stdlib));
    }

    private static void generatedBuildCode(String outputFileName) throws IOException, ADLException {
        Map<String, Object> hints = new HashMap<String, Object>();
        PrintWriter pw = new PrintWriter(outputFileName);
        hints.put("printwriter", pw);
        FactoryFactory.getFactory(FactoryFactory.STATIC_FRACTAL_BACKEND).newComponent(
                DEFAULT_CONFIGURATION, hints);
        pw.close();
    }

    public static void main(String[] args) throws Exception {
        generatedBuildCode(args[0]);
    }
}
