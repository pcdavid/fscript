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

import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.expressions.Expression;

/**
 * Evaluates an expression for its side-effectss and discards its return value.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class ExpressionStatement extends AbstractStatement {
    private final Expression expression;

    /**
     * Creates a new <code>ExpressionStatement</code>
     * 
     * @param expr
     *            the expression to evaluate
     */
    public ExpressionStatement(Expression expr) {
        if (expr == null) {
            throw new IllegalArgumentException("Expression can not be null.");
        }
        this.expression = expr;
    }

    public void execute(Environment env) throws FScriptException {
        checkEnvironment(env);
        expression.evaluate(env);
    }
}
