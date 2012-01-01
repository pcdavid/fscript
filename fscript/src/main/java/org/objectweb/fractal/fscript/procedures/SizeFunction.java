/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2008 ARMINES
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
package org.objectweb.fractal.fscript.procedures;

import static org.objectweb.fractal.fscript.types.NodeSetType.ANY_NODE_SET_TYPE;
import static org.objectweb.fractal.fscript.types.PrimitiveType.*;

import java.util.List;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.types.Type;
import org.objectweb.fractal.fscript.types.UnionType;

/**
 * Implements the <code>size()</code> function, which returns the size of its single
 * argument.
 * <ul>
 * <li>The <em>size</em> of a <code>string</code> is its length.</li>
 * <li>The <em>size</em> of a <code>number</code> is the number itself (its
 * magnitude).</li>
 * <li>The <em>size</em> of a <code>node-set</code> it is its cardinality (its number
 * of elements).</li>
 * <li>The <em>size</em> of any other value is {@link Double#NaN}.</li>
 * </ul>
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class SizeFunction extends AbstractProcedure {
    public SizeFunction() {
        super("size", true, new Type[] { new UnionType(STRING, NUMBER, ANY_NODE_SET_TYPE) }, NUMBER);
    }

    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        Object arg = args.get(0);
        if (arg instanceof Number) {
            return ((Number) arg).doubleValue();
        } else if (arg instanceof String) {
            return (double) ((String) arg).length();
        } else if (arg instanceof Set) {
            return (double) ((Set) arg).size();
        } else {
            return Double.NaN;
        }
    }
}
