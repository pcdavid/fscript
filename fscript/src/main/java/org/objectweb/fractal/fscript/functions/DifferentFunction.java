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

import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;

/**
 * This function takes exactly two arguments and returns <code>false</code> if they are
 * equal (in the sense of {@link Object#equals(Object)}), and <code>true</code>
 * otherwise. If one of the arguments is <code>null</code>, it returns
 * <code>true</code> if the other is non-<code>null</code>, <code>false</code>
 * otherwise.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 * @see EqualsFunction
 */
public class DifferentFunction extends AbstractProcedure {
    public String getName() {
        return "!=";
    }

    public Object apply(Object[] args) throws ScriptExecutionError {
        checkArity(2, args);
        return !equals(args[0], args[1]);
    }
}