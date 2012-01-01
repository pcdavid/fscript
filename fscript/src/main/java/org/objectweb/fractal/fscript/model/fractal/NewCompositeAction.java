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

import java.util.List;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.fractal.fscript.types.NodeSetType;
import org.objectweb.fractal.fscript.types.Signature;

/**
 * Implements the <code>new-composite()</code> action, which creates a new, empty
 * composite component from a description of its type as a node-set of interfaces.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class NewCompositeAction implements NativeProcedure, BindingController {
    /**
     * The bootstrap component to use to obatin the required {@link TypeFactory} and
     * {@link GenericFactory} interfaces.
     */
    protected Component bootstrap;

    /**
     * The Fractal model this action (and the nodes it manipulates) is part of.
     */
    protected FractalModel model;

    public boolean isPureFunction() {
        return false;
    }

    public String getName() {
        return "new-composite";
    }

    public Signature getSignature() {
        return new Signature(model.getNodeKind("component"), new NodeSetType(model
                .getNodeKind("interface")));
    }

    private GenericFactory getGenericFactory() throws NoSuchInterfaceException,
            InstantiationException {
        return (GenericFactory) bootstrap.getFcInterface("generic-factory");
    }

    private TypeFactory getTypeFactory() throws NoSuchInterfaceException, InstantiationException {
        return (TypeFactory) bootstrap.getFcInterface("type-factory");
    }

    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        Set<?> itfNodes = (Set) args.get(0);
        InterfaceType[] itfsTypes = new InterfaceType[itfNodes.size()];
        int i = 0;
        for (Object node : itfNodes) {
            InterfaceNode itf = (InterfaceNode) node;
            itfsTypes[i++] = (InterfaceType) itf.getInterface().getFcItfType();
        }
        try {
            ComponentType cType = getTypeFactory().createFcType(itfsTypes);
            Component comp = getGenericFactory().newFcInstance(cType, "composite", null);
            return model.createComponentNode(comp);
        } catch (InstantiationException e) {
            throw new ScriptExecutionError(Diagnostic.error(null,
                    "Error while instanciating composite."), e);
        } catch (NoSuchInterfaceException e) {
            throw new ScriptExecutionError(Diagnostic.error(null,
                    "Error while instanciating composite."), e);
        }
    }

    public String[] listFc() {
        return new String[] { "bootstrap", "fractal-model" };
    }

    public void bindFc(String itfName, Object srvItf) throws NoSuchInterfaceException {
        if ("bootstrap".equals(itfName)) {
            this.bootstrap = (Component) srvItf;
        } else if ("fractal-model".equals(itfName)) {
            this.model = (FractalModel) srvItf;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public Object lookupFc(String itfName) throws NoSuchInterfaceException {
        if ("bootstrap".equals(itfName)) {
            return this.bootstrap;
        } else if ("fractal-model".equals(itfName)) {
            return this.model;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public void unbindFc(String itfName) throws NoSuchInterfaceException {
        if ("bootstrap".equals(itfName)) {
            this.bootstrap = null;
        } else if ("fractal-model".equals(itfName)) {
            this.model = null;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }
}