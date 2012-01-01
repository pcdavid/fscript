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

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.nodes.AttributeNode;
import org.objectweb.fractal.fscript.nodes.NodeImpl;
import org.objectweb.fractal.fscript.reconfiguration.SetValueReconfiguration;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class SetValueAction extends AbstractProcedure {
    public String getName() {
        return "set-value";
    }

    public Object apply(Object[] args) throws ScriptExecutionError {
        checkArity(2, args);
        AttributeNode an = typedArgument(args[0], AttributeNode.class);
        Object value = args[1];
        Component owner = ((NodeImpl) an).getComponent();
        return new SetValueReconfiguration(owner, an.getName(), value).apply();
    }
}
