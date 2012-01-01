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
import static org.objectweb.fractal.fscript.types.VoidType.VOID_TYPE;

import java.util.List;

import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.model.Model;
import org.objectweb.fractal.fscript.types.Signature;
import org.objectweb.fractal.rmi.registry.NamingService;

/**
 * Implements the <code>rmi_unbind()</code> action, which removes a Fractal component
 * reference from the current Fractal RMI registry. This is a custom action which can not
 * be represented in the framework of the {@link Model} APIs.
 * <p>
 * The action assumes that the current Fractal RMI registry is available through the
 * global variable <code>*rmiregistry*</code>.
 * 
 * @author Pierre-Charles David
 */
public class RmiUnbindAction extends AbstractFractalProcedure {
    public String getName() {
        return "rmi_unbind";
    }

    public boolean isPureFunction() {
        return false;
    }

    public Signature getSignature() {
        return new Signature(VOID_TYPE, STRING);
    }

    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        NamingService ns = (NamingService) ctx.getGlobal("*rmiregistry*");
        if (ns == null) {
            Diagnostic err = Diagnostic.error(UNKNOWN,
                    "rmi_bind: not connected to a Fractal RMI registry.");
            throw new ScriptExecutionError(err);
        }

        String name = (String) args.get(0);
        ns.unbind(name);
        return null;
    }
}
