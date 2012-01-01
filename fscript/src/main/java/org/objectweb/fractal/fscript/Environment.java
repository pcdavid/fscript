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
package org.objectweb.fractal.fscript;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.adl.FactoryFactory;
import org.objectweb.fractal.fscript.axes.Axis;
import org.objectweb.fractal.fscript.nodes.Node;
import org.objectweb.fractal.fscript.nodes.NodeFactory;

/**
 * Represents the dynamic environment in which an FScript program is executed.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class Environment {
    private FScriptInterpreter fscript;

    private Environment parent;

    private Map<String, Object> variables;

    private Object returnValue;

    /**
     * Creates a new top-level environment for the specified interpreter.
     */
    public Environment(FScriptInterpreter fscript) {
        this.fscript = fscript;
        this.parent = null;
        this.variables = new HashMap<String, Object>();
    }

    /**
     * Creates a new derived environment.
     * 
     * @param parent
     *            the parent environment.
     * @param locals
     *            local variables in the new environment (name => value).
     */
    public Environment(Environment parent, Map<String, Object> locals) {
        this.fscript = parent.fscript;
        this.parent = parent;
        this.variables = new HashMap<String, Object>();
        if (locals != null) {
            variables.putAll(locals);
        }
    }

    public Factory getFactory() {
        try {
            return FactoryFactory.getFactory(FactoryFactory.FRACTAL_BACKEND);
        } catch (ADLException e) {
            return null;
        }
    }

    public Procedure getProcedure(String procName) {
        return fscript.getProcedure(procName);
    }

    public Axis getAxis(String axisName) {
        return fscript.getAxis(axisName);
    }
    
    public NodeFactory getNodeFactory() {
        return fscript;
    }

    public void setVariable(String variableName, Object object) {
        variables.put(variableName, object);
    }

    public Object getVariable(String variableName) {
        Object value = variables.get(variableName);
        if (value == null && parent != null) {
            value = parent.getVariable(variableName);
        }
        return value;
    }

    public void setCurrentNode(Node node) {
        setVariable("*current*", node);
    }

    public Node getCurrentNode() {
        return (Node) getVariable("*current*");
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Map getContext() {
        return fscript.getContext();
    }
}
