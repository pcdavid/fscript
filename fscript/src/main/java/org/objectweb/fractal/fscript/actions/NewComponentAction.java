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
import org.objectweb.fractal.fscript.Diagnostic;
import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.nodes.InterfaceNode;
import org.objectweb.fractal.fscript.nodes.InterfaceNodeImpl;
import org.objectweb.fractal.fscript.nodes.NodeFactory;
import org.objectweb.fractal.fscript.reconfiguration.NewComponentReconfiguration;
import org.objectweb.fractal.fscript.reconfiguration.Reconfiguration;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class NewComponentAction extends AbstractProcedure {
    public String getName() {
        return "new_component";
    }

    @Override
    protected Object apply(Object[] args) throws ScriptExecutionError {
        return null;
    }

    private GenericFactory getDefaultGenericFactory() throws NoSuchInterfaceException,
            InstantiationException {
        Component bootstrap = Fractal.getBootstrapComponent();
        return (GenericFactory) bootstrap.getFcInterface("generic-factory");
    }

    private TypeFactory getDefaultTypeFactory() throws NoSuchInterfaceException,
            InstantiationException {
        Component bootstrap = Fractal.getBootstrapComponent();
        return (TypeFactory) bootstrap.getFcInterface("type-factory");
    }

    @Override
    public Object apply(Object[] args, Environment env) throws ScriptExecutionError {
        int given = (args != null) ? args.length : 0;
        if (given != 3 && given != 4) {
            fail("Expected 3 or 4 arguments, but got " + given + ".");
        }

        try {
            int i = 0;
            // Get the GenericFactory
            GenericFactory gf = null;
            if (given == 3) {
                gf = getDefaultGenericFactory();
            } else {
                InterfaceNode gfItf = typedArgument(args[i++], InterfaceNode.class);
                gf = (GenericFactory) ((InterfaceNodeImpl) gfItf).getInterface();
            }
            // Get the interfaces and build the interface type
            Set itfSet = typedArgument(args[i++], Set.class);
            ComponentType cType = createComponentType(itfSet, getDefaultTypeFactory());
            // Get the membrane and controller descriptors
            String ctrlDesc = typedArgument(args[i++], String.class);
            String contDesc = typedArgument(args[i++], String.class);
            //
            Reconfiguration r = new NewComponentReconfiguration(gf, cType, ctrlDesc,
                    contDesc);
            NodeFactory nf = env.getNodeFactory();
            return nf.createComponentNode((Component) r.apply());
        } catch (NoSuchInterfaceException nsie) {
            throw new ScriptExecutionError(Diagnostic.error(nsie.getMessage()));
        } catch (InstantiationException e) {
            throw new ScriptExecutionError(Diagnostic.error(e.getMessage()));
        }
    }

    private ComponentType createComponentType(Set<?> nodes, TypeFactory tf)
            throws ScriptExecutionError {
        InterfaceType[] itfsTypes = new InterfaceType[nodes.size()];
        int i = 0;
        for (Object node : nodes) {
            InterfaceNodeImpl itf = typedArgument(node, InterfaceNodeImpl.class);
            itfsTypes[i++] = (InterfaceType) itf.getInterface().getFcItfType();
        }
        try {
            return tf.createFcType(itfsTypes);
        } catch (InstantiationException e) {
            fail("Error while instanciating composite: " + e.getMessage());
            return null;
        }
    }
}
