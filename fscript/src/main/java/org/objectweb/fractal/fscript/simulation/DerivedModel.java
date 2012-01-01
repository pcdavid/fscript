/*
 * Copyright (c) 2008 ARMINES
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
package org.objectweb.fractal.fscript.simulation;

import java.util.Map;
import java.util.Set;

import net.jcip.annotations.NotThreadSafe;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.model.Axis;
import org.objectweb.fractal.fscript.model.BasicModel;
import org.objectweb.fractal.fscript.model.Model;
import org.objectweb.fractal.fscript.model.ModelListener;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.NodeKind;

import com.google.common.collect.Maps;

@NotThreadSafe
public class DerivedModel extends BasicModel implements ModelTracer, ModelListener,
        BindingController, DerivedNodeMapper {
    private Model baseModel;

    private final Map<Node, DerivedNode> derivedNodes = Maps.newHashMap();

    Model getBaseModel() {
        return baseModel;
    }

    public void clear() {
        derivedNodes.clear();
    }

    public Trace getTrace() {
        return null; // TODO
    }

    public void clearTrace() {
        // TODO
    }

    public void connectionAdded(Node source, Node dest, Axis axis) {
        // TODO Append to trace
    }

    public void connectionRemoved(Node source, Node dest, Axis axis) {
        // TODO Append to trace
    }

    public void propertyChanged(Node node, String propName, Object oldValue, Object newValue) {
        // TODO Append to trace
    }

    @Override
    public Set<NodeKind> getNodeKinds() {
        return baseModel.getNodeKinds();
    }

    @Override
    public NodeKind getNodeKind(String name) {
        return baseModel.getNodeKind(name);
    }

    /**
     * Creates derived implementations of all the axes of the base model.
     */
    @Override
    protected void createAxes() {
        for (Axis axis : baseModel.getAxes()) {
            DerivedAxis derived = new DerivedAxis(this, axis, this);
            axes.put(derived.getName(), derived);
        }
    }

    public DerivedNode getDerivedNodeFor(Node node) {
        DerivedNode result = derivedNodes.get(node);
        if (result == null) {
            result = new DerivedNode(node, this);
            derivedNodes.put(node, result);
        }
        return result;
    }

    public String[] listFc() {
        return new String[] { "base-model" };
    }

    public Object lookupFc(String clItfName) throws NoSuchInterfaceException {
        if ("base-model".equals(clItfName)) {
            return this.baseModel;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    public void bindFc(String clItfName, Object srvItf) throws NoSuchInterfaceException {
        if ("base-model".equals(clItfName)) {
            this.baseModel = (Model) srvItf;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    public void unbindFc(String clItfName) throws NoSuchInterfaceException {
        if ("base-model".equals(clItfName)) {
            this.baseModel = null;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    @Override
    public String toString() {
        return "Derived model (from " + baseModel + ")";
    }
}
