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

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.nodes.ComponentNode;
import org.objectweb.fractal.fscript.nodes.NodeImpl;

public class ControllerDescFunction extends AbstractProcedure {
    public String getName() {
        return "controller-desc";
    }

    public Object apply(Object[] args) throws ScriptExecutionError {
        checkArity(1, args);
        ComponentNode node = typedArgument(args[0], ComponentNode.class);
        Component comp = ((NodeImpl) node).getComponent();
        try {
            Object desc = comp.getFcInterface("/controllerDesc");
            return desc.toString();
        } catch (NoSuchInterfaceException nsie) {
            return "";
        }
    }
}
