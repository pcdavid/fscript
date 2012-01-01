/*
 * Copyright (c) 2007 ARMINES
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
package org.objectweb.fractal.fscript.functions.fractal;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.Diagnostic;
import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.nodes.Node;
import org.objectweb.fractal.rmi.registry.NamingService;

public class RmiLookupFunction extends AbstractProcedure {
    public String getName() {
        return "rmi_lookup";
    }

    @Override
    public Object apply(Object[] args, Environment env) throws FScriptException {
        checkArity(1, args);
        NamingService ns = (NamingService) env.getVariable("*rmiregistry*");
        if (ns == null) {
            throw new ScriptExecutionError(Diagnostic
                    .error("rmi_lookup: not connected to a Fractal RMI registry."));
        } else {
            Set<Node> result = new HashSet<Node>();
            Component c = ns.lookup(typedArgument(args[0], String.class));
            if (c != null) {
                result.add(env.getNodeFactory().createComponentNode(c));
            }
            return result;
        }
    }

    @Override
    protected Object apply(Object[] args) throws ScriptExecutionError {
        throw new AssertionError();
    }
}
