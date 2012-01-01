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

/**
 * This function takes any number of string arguments and concatenates them in order into
 * a single string result. If no argument is provided, the result is an empty string. If
 * an argument is not a string, it is automatically converted first. A <code>null</code>
 * argument is treated as an empty string.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class ConcatFunction extends AbstractProcedure {
    public String getName() {
        return "concat";
    }

    public Object apply(Object[] args) {
        if (args == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (Object element : args) {
            if (element != null) {
                result.append(asString(element));
            }
        }
        return result.toString();
    }
}
