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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.model.fractal;

import static org.objectweb.fractal.fscript.types.PrimitiveType.STRING;

import java.util.List;

import net.jcip.annotations.Immutable;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.ast.SourceLocation;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.model.Axis;
import org.objectweb.fractal.fscript.model.Model;
import org.objectweb.fractal.fscript.types.Signature;
import org.objectweb.fractal.util.Fractal;

/**
 * Implements the <code>cbind()</code> to create bindings from collection interfaces.
 * This operation can not currently be supported in the framework defined by the
 * {@link Model} and {@link Axis} API, and is thus supplied as an ad hoc procedure.
 * <p>
 * Note that there is an inherent race condition in the Fractal APIs where creating such a
 * binding, as it requires a check-then-act operation to first find an unused interface
 * name (using <code>lookupFc()</code>), and then use it to create the binding (using
 * <code>bindFc()</code>).
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class CollectionBindAction extends AbstractFractalProcedure {
    public String getName() {
        return "cbind";
    }

    public Signature getSignature() {
        return new Signature(STRING, model.getNodeKind("component"), STRING, model
                .getNodeKind("interface"));
    }

    public boolean isPureFunction() {
        return false;
    }

    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        ComponentNode client = (ComponentNode) args.get(0);
        String clItfName = (String) args.get(1);
        InterfaceNode server = (InterfaceNode) args.get(2);
        Interface srvItf = server.getInterface();
        Component owner = client.getComponent();
        try {
            String clientName = nextFreeName(owner, clItfName);
            Fractal.getBindingController(owner).bindFc(clientName, srvItf);
            return clientName;
        } catch (NoSuchInterfaceException e) {
            throw failure("No binding-controller interface for " + client.getComponent(), e);
        } catch (IllegalBindingException e) {
            throw failure("Illegal binding.", e);
        } catch (IllegalLifeCycleException e) {
            throw failure("Illegal lifecycle state.", e);
        }
    }

    private ScriptExecutionError failure(String msg, Exception cause) {
        return new ScriptExecutionError(Diagnostic.error(SourceLocation.UNKNOWN, msg), cause);
    }

    private String nextFreeName(Component owner, String prefix) {
        try {
            BindingController bc = Fractal.getBindingController(owner);
            int suffix = 0;
            // This arbitrary limit is required to avoid infinite loops in the (highly
            // unlikely) case where new instances of the interface are concurrently
            // created faster than we can look them up.
            while (suffix < 1000) {
                if (bc.lookupFc(prefix + suffix) == null) {
                    return prefix + suffix;
                }
                suffix++;
            }
            return null;
        } catch (NoSuchInterfaceException e1) {
            return null;
        }
    }
}
