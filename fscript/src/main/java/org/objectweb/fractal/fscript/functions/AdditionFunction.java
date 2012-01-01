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
 * This function returns the sum of all its arguments. It takes any number of arguments,
 * including zero (in which case it returns <code>0.0</code>, the neutral element for
 * addition). Each argument is converted into a number if it is not one already. If at
 * least one argument can not be converted to a number, the result is
 * <code>Double.NaN</code>.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class AdditionFunction extends AbstractProcedure {
    public String getName() {
        return "+";
    }

    public Object apply(Object[] args) {
        double result = 0.0;
        for (Object element : args) {
            result += asNumber(element);
            if (Double.isNaN(result)) {
                // No need to treat the remaining arguments as NaN is contagious.
                break;
            }
        }
        return result;
    }
}
