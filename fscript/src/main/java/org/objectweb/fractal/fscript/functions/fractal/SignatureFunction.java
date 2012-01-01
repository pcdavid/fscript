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
package org.objectweb.fractal.fscript.functions.fractal;

import org.objectweb.fractal.fscript.AbstractProcedure;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.nodes.InterfaceNode;

/**
 * This function returns the signature of an interface. It takes a single interface node
 * as argument and return the signature of the interace (i.e. the name of the
 * corresponding Java type).
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class SignatureFunction extends AbstractProcedure {
    public String getName() {
        return "signature";
    }

    public Object apply(Object[] args) throws ScriptExecutionError {
        checkArity(1, args);
        InterfaceNode node = typedArgument(args[0], InterfaceNode.class);
        return node.getSignature();
    }
}
