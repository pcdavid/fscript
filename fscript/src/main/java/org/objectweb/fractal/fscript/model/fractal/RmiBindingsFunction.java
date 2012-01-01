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

import static org.objectweb.fractal.fscript.ast.SourceLocation.UNKNOWN;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.model.Model;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.types.NodeSetType;
import org.objectweb.fractal.fscript.types.Signature;
import org.objectweb.fractal.rmi.registry.NamingService;

/**
 * Implements the <code>rmi_bindings()</code> function, which returns the set of Fractal
 * components available in the current Fractal RMI registry. This is a custom function
 * which can not be represented in the framework of the {@link Model} APIs.
 * <p>
 * The function assumes that the current Fractal RMI registry is available through the
 * global variable <code>*rmiregistry*</code>.
 * 
 * @author Pierre-Charles David
 */
public class RmiBindingsFunction extends AbstractFractalProcedure {

    public String getName() {
        return "rmi_bindings";
    }

    public boolean isPureFunction() {
        return true;
    }

    public Signature getSignature() {
        return new Signature(new NodeSetType(model.getNodeKind("component")));
    }

    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        NamingService ns = (NamingService) ctx.getGlobal("*rmiregistry*");
        if (ns == null) {
            Diagnostic err = Diagnostic.error(UNKNOWN,
                    "rmi_bindings: not connected to a Fractal RMI registry.");
            throw new ScriptExecutionError(err);
        }

        Set<Node> result = new HashSet<Node>();
        for (String n : ns.list()) {
            Component c = ns.lookup(n);
            if (c != null) { // Needed to avoid race conditions
                result.add(model.createComponentNode(c));
            }
        }
        return result;
    }
}
