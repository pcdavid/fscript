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
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript.model.fractal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.control.IllegalContentException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.model.AbstractAxis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.util.Fractal;

/**
 * Implements the <code>child</code> axis in FPath. This axis connects composite
 * components to their direct subcomponents. The {@link #connect(Node, Node)} and
 * {@link #disconnect(Node, Node)} operations map to the addition and removal of
 * sub-components from composites.
 * 
 * @author Pierre-Charles David
 */
public class ChildAxis extends AbstractAxis {
    public ChildAxis(FractalModel model) {
        super(model, "child", "component", "component");
    }

    public boolean isPrimitive() {
        return true;
    }

    public boolean isModifiable() {
        return true;
    }

    public Set<Node> selectFrom(Node source) {
        Component parent = ((ComponentNode) source).getComponent();
        try {
            ContentController cc = Fractal.getContentController(parent);
            Set<Node> result = new HashSet<Node>();
            for (Component child : cc.getFcSubComponents()) {
                ComponentNode node = ((NodeFactory) model).createComponentNode(child);
                result.add(node);
            }
            return result;
        } catch (NoSuchInterfaceException e) {
            // Not a composite, no children.
            return Collections.emptySet();
        }
    }

    @Override
    public void connect(Node source, Node dest) {
        Component parent = ((ComponentNode) source).getComponent();
        Component child = ((ComponentNode) dest).getComponent();
        try {
            Fractal.getContentController(parent).addFcSubComponent(child);
        } catch (IllegalContentException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalLifeCycleException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchInterfaceException e) {
            throw new IllegalArgumentException("Can not add a child to a non-composite.", e);
        }
    }

    @Override
    public void disconnect(Node source, Node dest) {
        Component parent = ((ComponentNode) source).getComponent();
        Component child = ((ComponentNode) dest).getComponent();
        try {
            Fractal.getContentController(parent).removeFcSubComponent(child);
        } catch (IllegalContentException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalLifeCycleException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchInterfaceException e) {
            throw new IllegalArgumentException("Can not remove a child from a non-composite.", e);
        }
    }
}
