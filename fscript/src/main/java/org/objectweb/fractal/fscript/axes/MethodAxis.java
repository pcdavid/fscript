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

import java.lang.reflect.Method;
import java.util.Set;

import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;
import org.objectweb.fractal.fscript.nodes.InterfaceNodeImpl;
import org.objectweb.fractal.fscript.nodes.MethodNodeImpl;

public class MethodAxis extends AbstractAxis<InterfaceNodeImpl, MethodNodeImpl> {
    public MethodAxis(DefaultNodeFactory factory) {
        super(factory);
    }

    public String getName() {
        return "method";
    }

    public Set<MethodNodeImpl> selectFrom(InterfaceNodeImpl node) {
        Set<MethodNodeImpl> nodes = createNodeSet();
        for (Method meth : getAllMethods(node.getInterface())) {
            nodes.add(createMethodNode(node.getInterface(), meth));
        }
        return nodes;
    }

    private Method[] getAllMethods(Interface itf) {
        InterfaceType itfType = (InterfaceType) itf.getFcItfType();
        String signature = itfType.getFcItfSignature();
        try {
            Class klass = Class.forName(signature);
            return klass.getMethods();
        } catch (ClassNotFoundException e) {
            return new Method[0];
        }
    }
}
