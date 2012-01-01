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

import java.util.Set;

import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;

/**
 * TODO Add javadoc
 * TODO Add test case
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class SizeFunction extends AbstractProcedure {
    public String getName() {
        return "size";
    }

    public Object apply(Object[] args) throws ScriptExecutionError {
        checkArity(1, args);
        // TODO Check redundancy with Values.asNumber()
        Object obj = args[0];
        double result = 0.0;
        if (obj instanceof Number) {
            result = ((Number) obj).doubleValue();
        } else if (obj instanceof String) {
            result = ((String) obj).length();
        } else if (obj instanceof Set) {
            result = ((Set<?>) obj).size();
        }
        return result;
    }
}
