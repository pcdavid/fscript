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
package org.objectweb.fractal.fscript.actions;

import java.util.Map;

import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.nodes.NodeFactory;
import org.objectweb.fractal.fscript.reconfiguration.InstanciateReconfiguration;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class InstanciateAction extends AbstractProcedure {
    public String getName() {
        return "new";
    }

    @Override
    protected Object apply(Object[] args) throws ScriptExecutionError {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object apply(Object[] args, Environment env) throws ScriptExecutionError {
        if (args == null || args.length == 0) {
            throw arityError(getName(), "at least one", "zero");
        }
        String name = asString(args[0]);
        Map ctx = env.getContext();
        if (args.length > 1) {
            if ((args.length % 2) == 0) {
                fail("Invalid number of context arguments (must be pair).");
            }
            for (int i = 0; i < (args.length / 2); i++) {
                String propName = asString(args[1 + 2 * i]);
                String propValue = asString(args[1 + 2 * i + 1]);
                ctx.put(propName, propValue);
            }
        }
        Factory cf = env.getFactory();
        NodeFactory nf = env.getNodeFactory();
        Object newComponent = new InstanciateReconfiguration(cf, name, ctx).apply();
        return nf.createComponentNode((Component) newComponent);
    }
}
