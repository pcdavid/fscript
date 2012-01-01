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
package org.objectweb.fractal.fscript.nodes;

import java.lang.reflect.Method;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;

/**
 * Default implementation of <code>MethodNode</code>.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class MethodNodeImpl implements NodeImpl, MethodNode {
    /**
     * The interface owning the method.
     */
    private final Interface itf;

    /**
     * The method itself.
     */
    private final Method method;

    /**
     * Creates an new <code>MethodNodeImpl</code>.
     * 
     * @param itf
     *            the interface owning the method.
     * @param method
     *            the method itself.
     */
    public MethodNodeImpl(Interface itf, Method method) {
        if (itf == null || method == null) {
            throw new IllegalArgumentException("Parameters can not be null.");
        }
        this.itf = itf;
        this.method = method;
    }

    public Component getComponent() {
        return itf.getFcItfOwner();
    }

    public Object getValue() {
        return getMethod();
    }

    /**
     * Returns the language-level method wrapped by this node.
     * 
     * @return the method represented by this node.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Returns the Fractal interface this method belongs to.
     * 
     * @return the the Fractal interface this method belongs to.
     */
    public Interface getInterface() {
        return itf;
    }

    public String getName() {
        return method.getName();
    }

    @Override
    public String toString() {
        return "#<method: " + NameHelper.getName(itf.getFcItfOwner()) + "."
                + itf.getFcItfName() + "#" + getName() + ">";
    }
}
