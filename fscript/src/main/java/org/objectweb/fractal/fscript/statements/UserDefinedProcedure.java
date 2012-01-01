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
package org.objectweb.fractal.fscript.statements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.Procedure;

/**
 * Represents a user-defined (i.e. non-primitive) function or action.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class UserDefinedProcedure implements Procedure {
    private final String name;

    private final boolean isAction;

    private final List<String> parametersNames;

    private final Statement body;

    /**
     * Creates a new <code>UserDefinedProcedure</code>.
     * 
     * @param name
     *            the name of the procedure
     * @param isAction
     *            <code>true</code> iff the procedure is declared as an action, i.e. a
     *            procedure which can actually change the target application.
     * @param paramNames
     *            the names of the formal parameters
     * @param body
     *            the body of the procedure
     */
    public UserDefinedProcedure(String name, boolean isAction, List<String> paramNames,
            Statement body) {
        if (name == null) {
            throw new IllegalArgumentException("Missing procedure name.");
        }
        if (body == null) {
            throw new IllegalArgumentException("Missing procedure body.");
        }
        this.name = name;
        this.isAction = isAction;
        if (paramNames != null) {
            this.parametersNames = new ArrayList<String>(paramNames);
        } else {
            this.parametersNames = Collections.emptyList();
        }
        this.body = body;
    }

    /**
     * Returns the name of the procedure.
     * 
     * @return the name of the procedure.
     */
    public String getName() {
        return name;
    }

    public boolean hasSideEffects() {
        return isAction;
    }

    public Object apply(Object[] args, Environment env) throws FScriptException {
        assert (args != null && env != null);
        Environment local = new Environment(env, null);
        int i = 0;
        for (String param : parametersNames) {
            local.setVariable(param, args[i++]);
        }
        local.setReturnValue(null);
        body.execute(local);
        return local.getReturnValue();
    }
}
