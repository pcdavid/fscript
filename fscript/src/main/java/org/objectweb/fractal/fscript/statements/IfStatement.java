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
import org.objectweb.fractal.fscript.Values;
import org.objectweb.fractal.fscript.expressions.Expression;

/**
 * Conditional execution of statements (<code>if ... then ... else</code>).
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class IfStatement extends AbstractStatement {
    private final Expression testExpression;

    private final Statement trueBranch;

    private final Statement falseBranch;

    /**
     * Creates a new <code>IfStatement</code>.
     * 
     * @param testExpression
     *            the test expression.
     * @param trueBranch
     *            the statement to execute if the test is <code>true</code>.
     * @param falseBranch
     *            the optional statement to execute if the test is <code>false</code>.
     */
    public IfStatement(Expression testExpression, Statement trueBranch,
            Statement falseBranch) {
        if (testExpression == null) {
            throw new IllegalArgumentException("Missing test expression.");
        }
        if (trueBranch == null) {
            throw new IllegalArgumentException("Missing 'then' branch.");
        }
        this.testExpression = testExpression;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    public void execute(Environment env) throws FScriptException {
        checkEnvironment(env);
        if (Values.asBoolean(testExpression.evaluate(env))) {
            trueBranch.execute(env);
        } else if (falseBranch != null) {
            falseBranch.execute(env);
        }
    }
}
