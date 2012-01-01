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
package org.objectweb.fractal.fscript.model.jade;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.model.AbstractAxis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.fscript.model.fractal.NodeFactory;

public class DualAxis extends AbstractAxis {
    private static final String RC = "org.ow2.jasmine.jade.reflex.api.control.ReflexController";

    public DualAxis(JadeModel model) {
        super(model, "dual", "component", "component");
    }

    public boolean isPrimitive() {
        return true;
    }

    public boolean isModifiable() {
        return false;
    }

    public Set<Node> selectFrom(Node node) {
        Component comp = ((ComponentNode) node).getComponent();
        Set<Node> result = new HashSet<Node>();
        try {
            Object rc = comp.getFcInterface("reflex-controller");
            if (rc != null) {
                try {
                    Class<?> rcClass = Class.forName(RC);
                    Method cmpRefMethod = rcClass.getMethod("getCmpRef");
                    Component dual = (Component) cmpRefMethod.invoke(rc);
                    result.add(((NodeFactory) model).createComponentNode(dual));
                } catch (ClassNotFoundException cnfe) {
                    throw new AssertionError("Incompatible 'reflex-controller' implementation.");
                } catch (NoSuchMethodException e) {
                    throw new AssertionError("Incompatible 'reflex-controller' implementation.");
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.getCause());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (NoSuchInterfaceException e) {
            // No reflex-controller, thus no dual component.
        }
        return result;
    }
}
