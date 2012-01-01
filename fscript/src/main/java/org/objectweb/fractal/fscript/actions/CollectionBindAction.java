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
package org.objectweb.fractal.fscript.actions;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.InterfaceNodeImpl;
import org.objectweb.fractal.fscript.reconfiguration.BindReconfiguration;
import org.objectweb.fractal.util.Fractal;

/**
 * The <code>bind()</code> primitive action.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class CollectionBindAction extends AbstractProcedure {
    public String getName() {
        return "cbind";
    }

    public Object apply(Object[] args) throws ScriptExecutionError {
        checkArity(3, args);
        ComponentNodeImpl client = typedArgument(args[0], ComponentNodeImpl.class);
        String clItfName = typedArgument(args[1], String.class);
        InterfaceNodeImpl server = typedArgument(args[2], InterfaceNodeImpl.class);
        Interface srvItf = server.getInterface();
        Component owner = client.getComponent();
        return new BindReconfiguration(owner, nextFreeName(owner, clItfName), srvItf).apply();
    }

    private String nextFreeName(Component owner, String prefix) {
        try {
            BindingController bc = Fractal.getBindingController(owner);
            int suffix = 0;
            while (suffix < 100) { // FIXME Temporary hack to avoid infinite loop.
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
