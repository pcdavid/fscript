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
import static org.objectweb.fractal.fscript.types.PrimitiveType.STRING;

import java.util.Collections;
import java.util.List;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.model.Model;
import org.objectweb.fractal.fscript.types.NodeSetType;
import org.objectweb.fractal.fscript.types.Signature;
import org.objectweb.fractal.rmi.registry.NamingService;

/**
 * Implements the <code>rmi_lookup()</code> function, which locates a Fractal component
 * in the current Fractal RMI registry by name. This is a custom function which can not be
 * represented in the framework of the {@link Model} APIs.
 * <p>
 * The function assumes that the current Fractal RMI registry is available through the
 * global variable <code>*rmiregistry*</code>.
 * 
 * @author Pierre-Charles David
 */
public class RmiLookupFunction extends AbstractFractalProcedure {
    public String getName() {
        return "rmi_lookup";
    }

    public boolean isPureFunction() {
        return true;
    }

    public Signature getSignature() {
        return new Signature(new NodeSetType(model.getNodeKind("component")), STRING);
    }

    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        String name = (String) args.get(0);
        NamingService ns = (NamingService) ctx.getGlobal("*rmiregistry*");

        if (ns == null) {
            Diagnostic err = Diagnostic.error(UNKNOWN,
                    "rmi_lookup: not connected to a Fractal RMI registry.");
            throw new ScriptExecutionError(err);
        }
        Component c = ns.lookup(name);
        if (c != null) {
            return Collections.singleton(model.createComponentNode(c));
        } else {
            return Collections.emptySet();
        }
    }
}
