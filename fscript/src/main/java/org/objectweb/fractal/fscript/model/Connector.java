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
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.fractal.fscript.types.Signature;
import org.objectweb.fractal.fscript.types.VoidType;

public class Connector implements NativeProcedure {
    private final Axis axis;
    
    public Connector(Axis axis) {
        this.axis = axis;
    }

    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        Node src = (Node) args.get(0);
        Node dest = (Node) args.get(1);
        axis.connect(src, dest);
        return null;
    }

    public String getName() {
        return "add-" + axis.getName();
    }

    public Signature getSignature() {
        return new Signature(VoidType.VOID_TYPE, axis.getInputNodeType(), axis.getOutputNodeType());
    }

    public boolean isPureFunction() {
        return false;
    }
    
    @Override
    public String toString() {
        return getName() + getSignature();
    }
}
