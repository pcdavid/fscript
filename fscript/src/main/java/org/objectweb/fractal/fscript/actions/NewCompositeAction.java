/*
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

import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Fractal;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.nodes.InterfaceNodeImpl;
import org.objectweb.fractal.fscript.nodes.NodeFactory;
import org.objectweb.fractal.fscript.reconfiguration.CreateCompositeReconfiguration;
import org.objectweb.fractal.fscript.reconfiguration.Reconfiguration;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class NewCompositeAction extends AbstractProcedure {
    public String getName() {
        return "new_composite";
    }

    @Override
    protected Object apply(Object[] args) throws ScriptExecutionError {
        return null;
    }

    private GenericFactory getGenericFactory() throws NoSuchInterfaceException,
            InstantiationException {
        Component bootstrap = Fractal.getBootstrapComponent();
        return (GenericFactory) bootstrap.getFcInterface("generic-factory");
    }

    private TypeFactory getTypeFactory() throws NoSuchInterfaceException,
            InstantiationException {
        Component bootstrap = Fractal.getBootstrapComponent();
        return (TypeFactory) bootstrap.getFcInterface("type-factory");
    }

    @Override
    public Object apply(Object[] args, Environment env) throws ScriptExecutionError {
        checkArity(1, args);
        Set<?> itfNodes = typedArgument(args[0], Set.class);
        InterfaceType[] itfsTypes = new InterfaceType[itfNodes.size()];
        int i = 0;
        for (Object node : itfNodes) {
            InterfaceNodeImpl itf = typedArgument(node, InterfaceNodeImpl.class);
            itfsTypes[i++] = (InterfaceType) itf.getInterface().getFcItfType();
        }
        try {
            ComponentType cType = getTypeFactory().createFcType(itfsTypes);
            Reconfiguration reconfig = new CreateCompositeReconfiguration(
                    getGenericFactory(), cType);
            NodeFactory nf = env.getNodeFactory();
            return nf.createComponentNode((Component) reconfig.apply());
        } catch (InstantiationException e) {
            fail("Error while instanciating composite: " + e.getMessage());
            return null;
        } catch (NoSuchInterfaceException e) {
            fail(e.getMessage());
            return null;
        }
    }
}
