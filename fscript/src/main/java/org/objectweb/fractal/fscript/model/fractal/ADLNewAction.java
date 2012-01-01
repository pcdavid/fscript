/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2008 ARMINES
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

import static org.objectweb.fractal.fscript.diagnostics.Severity.ERROR;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jcip.annotations.Immutable;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.fractal.fscript.types.PrimitiveType;
import org.objectweb.fractal.fscript.types.Signature;

/**
 * Implements the <code>adl-new()</code> action to instantiate components using Fractal
 * ADL.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class ADLNewAction implements NativeProcedure, BindingController {
    private FractalModel model;

    public String getName() {
        return "adl-new";
    }

    public Signature getSignature() {
        return new Signature(model.getNodeKind("component"), PrimitiveType.STRING);
    }

    public boolean isPureFunction() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        String name = (String) args.get(0);
        try {
            Object newComponent = getFactory().newComponent(name, model.getInstanciationContext());
            return model.createComponentNode((Component) newComponent);
        } catch (ADLException e) {
            Diagnostic err = new Diagnostic(ERROR, "Unable to instanciate component " + name);
            throw new ScriptExecutionError(err, e);
        }
    }

    private Factory getFactory() {
        try {
            return (Factory) model.lookupFc("adl-factory");
        } catch (NoSuchInterfaceException e) {
            throw new AssertionError("Invalid FractalModel component.");
        }
    }

    @SuppressWarnings({ "unchecked", "unused" })
    private Map createInstanciationContext(List<Object> args) throws ScriptExecutionError {
        if (args.size() == 1) {
            return Collections.emptyMap();
        }
        int contextArgs = args.size() - 1;
        if (contextArgs % 2 != 0) {
            throw new ScriptExecutionError(new Diagnostic(ERROR, getName() + "(): "
                    + "Invalid number of context arguments (must be even)."));
        }
        Map<String, Object> ic = new HashMap<String, Object>();
        for (int i = 0; i < (contextArgs / 2); i++) {
            String propName = (String) args.get(1 + 2 * i);
            String propValue = (String) args.get(1 + 2 * i + 1);
            ic.put(propName, propValue);
        }
        return ic;
    }

    public String[] listFc() {
        return new String[] { "fractal-model" };
    }

    public void bindFc(String itfName, Object srvItf) throws NoSuchInterfaceException {
        if ("fractal-model".equals(itfName)) {
            this.model = (FractalModel) srvItf;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public Object lookupFc(String itfName) throws NoSuchInterfaceException {
        if ("fractal-model".equals(itfName)) {
            return this.model;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public void unbindFc(String itfName) throws NoSuchInterfaceException {
        if ("fractal-model".equals(itfName)) {
            this.model = null;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }
}
