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
package org.objectweb.fractal.fscript.functions;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.nodes.Node;

/**
 * This function computes the intersection of its nodesets arguments. It takes exactly two
 * nodeset arguments, and returns a new nodeset which includes the nodes which appear in
 * both its arguments. All the nodes present in the arguments must be of the same kind.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 * @see IntersectionFunction
 * @see IntersectionFunction
 */
public class IntersectionFunction extends AbstractProcedure {
    public String getName() {
        return "intersection";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object apply(Object[] args) throws ScriptExecutionError {
        checkArity(2, args);
        Set<Node> result = new HashSet<Node>(typedArgument(args[0], Set.class));
        result.retainAll(typedArgument(args[1], Set.class));
        return result;
    }
}
