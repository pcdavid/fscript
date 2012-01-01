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
 * This functions takes two string arguments and returns <code>true</code> if the second
 * one is a prefix of the first, <code>false</code> otherwise. If one argument is not a
 * string, it is first converted automatically (<code>null</code> is converted into an
 * empty string).
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 * @see EndsWithFunction
 */
public class StartsWithFunction extends AbstractProcedure {
    public String getName() {
        return "starts-with";
    }

    public Object apply(Object[] args) throws ScriptExecutionError {
        checkArity(2, args);
        String str = asString(args[0]);
        String suffix = asString(args[1]);
        return str.startsWith(suffix);
    }
}
