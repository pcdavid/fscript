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
package org.objectweb.fractal.fscript.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.fractal.fscript.types.Type;
import org.objectweb.fractal.fscript.types.UnionType;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimaps;

public class BasicModel implements Model, LifeCycleController {
    protected final Map<String, NodeKind> nodeKinds = new HashMap<String, NodeKind>();

    protected final Map<String, Axis> axes = new HashMap<String, Axis>();

    protected final Map<String, NativeProcedure> natives = new HashMap<String, NativeProcedure>();

    private boolean initialized = false;

    private boolean started = false;

    public String getFcState() {
        return started ? "STARTED" : "STOPPED";
    }

    public void startFc() {
        if (!initialized) {
            initialize();
            initialized = true;
        }
        this.started = true;
    }

    public void stopFc() {
        this.started = false;
    }

    protected void initialize() {
        createNodeKinds();
        createAxes();
        createPrimitiveAccessors();
        createAxesProcedures();
        createAdditionalProcedures();
    }

    protected void createNodeKinds() {
        // None by default.
    }

    protected void createAxes() {
        // None by default.
    }

    protected void createAdditionalProcedures() {
        // None by default.
    }

    /**
     * Helper method to be used by sub-classes to contribute a node-kind.
     */
    protected void addKind(String name, Property... properties) {
        nodeKinds.put(name, new NodeKind(name, properties));
    }

    /**
     * Helper method to be used by sub-classes to contribute an axis.
     */
    protected void addAxis(Axis axis) {
        axes.put(axis.getName(), axis);
    }

    /**
     * Helper method used to contribute a native procedure.
     */
    protected void addProcedure(NativeProcedure proc) {
        natives.put(proc.getName(), proc);
    }

    public Set<Axis> getAxes() {
        return Collections.unmodifiableSet(new HashSet<Axis>(axes.values()));
    }

    public Axis getAxis(String name) {
        return axes.get(name);
    }

    public NodeKind getNodeKind(String name) {
        return nodeKinds.get(name);
    }

    public Set<NodeKind> getNodeKinds() {
        return Collections.unmodifiableSet(new HashSet<NodeKind>(nodeKinds.values()));
    }

    public Set<String> getAvailableProcedures() {
        return Collections.unmodifiableSet(natives.keySet());
    }

    public NativeProcedure getNativeProcedure(String name) {
        return natives.get(name);
    }

    public boolean hasProcedure(String name) {
        return natives.containsKey(name);
    }

    /**
     * Computes and registers reader functions and writer actions for every property
     * declared in the model.
     */
    private void createPrimitiveAccessors() {
        ArrayListMultimap<String, NodeKind> readables = Multimaps.newArrayListMultimap();
        ArrayListMultimap<String, NodeKind> writables = Multimaps.newArrayListMultimap();
        for (NodeKind kind : getNodeKinds()) {
            for (Property prop : kind.getProperties()) {
                readables.put(prop.getName(), kind);
                if (prop.isWritable()) {
                    writables.put(prop.getName(), kind);
                }
            }
        }
        for (String propName : readables.keySet()) {
            List<NodeKind> kinds = readables.get(propName);
            Type nodeType = createPolymorphicType(kinds);
            Type valueType = kinds.get(0).getProperty(propName).getType();
            addProcedure(new PropertyReader(propName, nodeType, valueType));
        }
        for (String propName : writables.keySet()) {
            List<NodeKind> kinds = writables.get(propName);
            Type nodeType = createPolymorphicType(kinds);
            Type valueType = kinds.get(0).getProperty(propName).getType();
            addProcedure(new PropertyWriter(propName, nodeType, valueType));
        }
    }

    private Type createPolymorphicType(List<NodeKind> kinds) {
        if (kinds.size() == 1) {
            return kinds.get(0);
        } else {
            return new UnionType(kinds.toArray(new Type[kinds.size()]));
        }
    }

    /**
     * Computes and registers the native procedures used to modify the structure of an
     * architecture, based on the declared model information.
     */
    private void createAxesProcedures() {
        for (Axis axis : getAxes()) {
            addProcedure(new AxisSelector(axis));
            if (axis.isModifiable()) {
                addProcedure(new Connector(axis));
                addProcedure(new Disconnector(axis));
            }
        }
    }

    @Override
    public String toString() {
        return "Base model";
    }
}
