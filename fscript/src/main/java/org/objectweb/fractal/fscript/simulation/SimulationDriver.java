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

import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.interpreter.Interpreter;
import org.objectweb.fractal.fscript.model.Node;

public class SimulationDriver extends Interpreter implements Simulator, BindingController {
    private ModelTracer tracer;

    private DerivedNodeMapper nodeMapper;

    public Trace simulate(String procName, Object[] args, Map<String, Object> globals)
            throws ScriptExecutionError {
        try {
            tracer.clearTrace();
            super.invoke(procName, derivedArguments(args), derivedGlobals(globals));
            return tracer.getTrace();
        } finally {
            nodeMapper.clear();
        }
    }

    private Object[] derivedArguments(Object[] args) {
        Object[] result = new Object[args.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = derivedValue(args[i]);
        }
        return result;
    }

    private Map<String, Object> derivedGlobals(Map<String, Object> globals) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : globals.entrySet()) {
            result.put(entry.getKey(), derivedValue(entry.getValue()));
        }
        return result;
    }

    private Object derivedValue(Object value) {
        if (value instanceof Node) {
            return nodeMapper.getDerivedNodeFor((Node) value);
        } else {
            return value;
        }
    }

    @Override
    public String[] listFc() {
        String[] itfs = super.listFc();
        String[] result = new String[itfs.length + 2];
        result[0] = "tracer";
        result[1] = "derived-node-mapper";
        System.arraycopy(itfs, 0, result, 2, itfs.length);
        return result;
    }

    @Override
    public Object lookupFc(String clientItfName) throws NoSuchInterfaceException {
        if ("tracer".equals(clientItfName)) {
            return this.tracer;
        } else if ("derived-node-mapper".equals(clientItfName)) {
            return this.nodeMapper;
        } else {
            return super.lookupFc(clientItfName);
        }
    }

    @Override
    public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException {
        if ("tracer".equals(clientItfName)) {
            this.tracer = (ModelTracer) serverItf;
        } else if ("derived-node-mapper".equals(clientItfName)) {
            this.nodeMapper = (DerivedNodeMapper) serverItf;
        } else {
            super.bindFc(clientItfName, serverItf);
        }
    }

    @Override
    public void unbindFc(String clientItfName) throws NoSuchInterfaceException {
        if ("tracer".equals(clientItfName)) {
            this.tracer = null;
        } else if ("derived-node-mapper".equals(clientItfName)) {
            this.nodeMapper = null;
        } else {
            super.unbindFc(clientItfName);
        }
    }
}
