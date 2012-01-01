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

import java.util.List;

import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.procedures.AbstractProcedure;
import org.objectweb.fractal.fscript.types.NodeSetType;
import org.objectweb.fractal.fscript.types.Type;

public class AxisSelector extends AbstractProcedure {
    private final Axis axis;

    public AxisSelector(Axis axis) {
        super("axis:" + axis.getName(), true, new Type[] { axis.getInputNodeType() },
                new NodeSetType((NodeKind) axis.getOutputNodeType()));
        this.axis = axis;
    }

    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        return axis.selectFrom((Node) args.get(0));
    }
}
