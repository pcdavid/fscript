/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2007 ARMINES
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

import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.nodes.InterfaceNode;

/**
 * This function checks wheter two interfaces are compatible, i.e. can be bound. It takes
 * exactly two interface nodes as argument. It returns <code>true</code> iff a binding
 * from the first argument to the second would be possible. The check takes into account
 * the interfaces roles (client or server), cardinality, visibility (internal or
 * external), and signature (underlying Java types). It does not consider other
 * architectural constaints, in particular locality.
 * 
 * TODO Clarify exactly what is tested or not.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class CompatibleFunction extends AbstractProcedure {
    public String getName() {
        return "compatible";
    }

    @SuppressWarnings("unchecked")
    public Object apply(Object[] args) throws ScriptExecutionError {
        checkArity(2, args);
        InterfaceNode client = typedArgument(args[0], InterfaceNode.class);
        InterfaceNode server = typedArgument(args[1], InterfaceNode.class);
        if (! (client.isClient() && !server.isClient())) {
            return false;
        }
        // TODO Implement additional checks.
        try {
            Class clientType = Class.forName(client.getSignature());
            Class serverType = Class.forName(server.getSignature());
            return clientType.isAssignableFrom(serverType);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
