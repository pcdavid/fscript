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
package org.objectweb.fractal.fscript.axes;

import java.util.Collections;
import java.util.Set;

import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;
import org.objectweb.fractal.fscript.nodes.InterfaceNodeImpl;
import org.objectweb.fractal.util.Fractal;

/**
 * <p>
 * From a <code>Component</code> node, selects all the server interfaces to which the
 * initial node is connected through any of its external interfaces.
 * </p>
 * <p>
 * From a client <code>Interface</code> node (internal or external), selects the server
 * interface it is connected to, if any.
 * </p>
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class BindingAxis extends AbstractAxis<InterfaceNodeImpl, InterfaceNodeImpl> {
    public BindingAxis(DefaultNodeFactory factory) {
        super(factory);
    }

    public String getName() {
        return "binding";
    }

    public Set<InterfaceNodeImpl> selectFrom(InterfaceNodeImpl node) {
        InterfaceNodeImpl itfNode = followBinding(node);
        if (itfNode != null) {
            return Collections.singleton(itfNode);
        } else {
            return Collections.emptySet();
        }
    }

    private InterfaceNodeImpl followBinding(InterfaceNodeImpl source) {
        // FIXME Handle collection bindings?
        try {
            BindingController bc = Fractal.getBindingController(source.getComponent());
            Object itf = bc.lookupFc(source.getName());
            if (itf != null) {
                return createInterfaceNode((Interface) itf);
            } else {
                return null;
            }
        } catch (NoSuchInterfaceException e) {
            return null;
        }
    }
}
