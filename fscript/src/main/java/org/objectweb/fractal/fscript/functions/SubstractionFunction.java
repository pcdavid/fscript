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
 * This function takes two or more numerical arguments and returns the result of
 * successively substracting from the first one all the arguments starting from the
 * second. If an argument is not a number, it is automatically converted first.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class SubstractionFunction extends AbstractProcedure {
    public String getName() {
        return "-";
    }

    public Object apply(Object[] args) throws ScriptExecutionError {
        int given = (args == null) ? 0 : args.length;
        if (given < 2) {
            throw arityError(getName(), "at least 2", "" + given);
        }
        double result = asNumber(args[0]);
        for (int i = 1; i < args.length; i++) {
            result -= asNumber(args[i]);
        }
        return result;
    }
}
