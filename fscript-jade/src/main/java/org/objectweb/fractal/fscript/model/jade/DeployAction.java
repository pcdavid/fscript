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
package org.objectweb.fractal.fscript.model.jade;

import static org.objectweb.fractal.fscript.ast.SourceLocation.UNKNOWN;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.model.fractal.InterfaceNode;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.fractal.fscript.types.PrimitiveType;
import org.objectweb.fractal.fscript.types.Signature;
import org.objectweb.jasmine.jade.service.deployer.DeployerService;
import org.objectweb.jasmine.jade.service.deployer.DeploymentException;

public class DeployAction implements NativeProcedure, BindingController {
    private JadeModel model;

    public String getName() {
        return "deploy";
    }

    public Signature getSignature() {
        return new Signature(model.getNodeKind("component"), model
                .getNodeKind("interface"), PrimitiveType.STRING);
    }

    public boolean isPureFunction() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        InterfaceNode itf = (InterfaceNode) args.get(0);
        DeployerService dserv = (DeployerService) itf.getInterface();
        String name = (String) args.get(1);
        Map deployCtx = new HashMap<Object, Object>();
        if (args.size() > 2) {
            if ((args.size() % 2) != 0) {
                Diagnostic err = Diagnostic.error(UNKNOWN,
                        "Invalid number of context arguments (must be pair).");
                throw new ScriptExecutionError(err);
            }
            for (int i = 2; i < args.size();) {
                String propName = (String) args.get(i++);
                String propValue = (String) args.get(i++);
                deployCtx.put(propName, propValue);
            }
        }
        JadeNodeFactory nf = model;
        try {
            Component comp = dserv.deploy(name, deployCtx);
            return nf.createComponentNode(comp);
        } catch (DeploymentException e) {
            Diagnostic err = Diagnostic
                    .error(UNKNOWN, "Error while deploying component.");
            throw new ScriptExecutionError(err);
        }
    }

    public String[] listFc() {
        return new String[] { "fractal-model" };
    }

    public void bindFc(String itfName, Object srvItf) throws NoSuchInterfaceException {
        if ("jade-model".equals(itfName)) {
            this.model = (JadeModel) srvItf;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public Object lookupFc(String itfName) throws NoSuchInterfaceException {
        if ("jade-model".equals(itfName)) {
            return this.model;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public void unbindFc(String itfName) throws NoSuchInterfaceException {
        if ("jade-model".equals(itfName)) {
            this.model = null;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }
}
