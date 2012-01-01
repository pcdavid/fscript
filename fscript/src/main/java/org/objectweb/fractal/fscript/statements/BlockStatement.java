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
import java.util.List;

import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.FScriptException;

/**
 * Executes a sequence of statements until the end of the sequence is reached or an
 * explicit <code>return</code> is requested.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class BlockStatement extends AbstractStatement {
    private final List<Statement> steps;

    /**
     * Creates a new block statement.
     * 
     * @param statements
     *            the sequence of statements to execute.
     */
    public BlockStatement(List<Statement> statements) {
        if (statements == null || statements.isEmpty()) {
            throw new IllegalArgumentException("Missing statements for block.");
        }
        this.steps = new ArrayList<Statement>(statements);
    }

    public void execute(Environment env) throws FScriptException {
        checkEnvironment(env);
        for (Statement step : steps) {
            step.execute(env);
            if (env.getReturnValue() != null) {
                return;
            }
        }
    }
}
