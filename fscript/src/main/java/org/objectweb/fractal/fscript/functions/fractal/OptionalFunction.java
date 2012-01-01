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
 * Tests whether an interface is optional or mandatory. This procedure takes exactly one
 * argument, which must be an interface node, are returns <code>true</code> if the
 * corresponding component interface is an optional interface, and <code>false</code> if
 * it is a mandatory interface.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class OptionalFunction extends AbstractProcedure {
    public String getName() {
        return "optional";
    }

    public Object apply(Object[] args) throws ScriptExecutionError {
        checkArity(1, args);
        InterfaceNode node = typedArgument(args[0], InterfaceNode.class);
        return node.isOptional();
    }
}
