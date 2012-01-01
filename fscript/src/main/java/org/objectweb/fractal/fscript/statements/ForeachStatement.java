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

import java.util.Set;

import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.expressions.Expression;
import org.objectweb.fractal.fscript.nodes.Node;

/**
 * Executes a statement repeatedly with a local variable iterating on a node set.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class ForeachStatement extends AbstractStatement {
    private final String variableName;

    private final Expression iterationExpression;

    private final Statement statement;

    /**
     * Creates a new <code>ForeachStatement</code>.
     * 
     * @param name
     *            the name of the iteration variable.
     * @param expression
     *            the expression denoting the node set on which to iterate.
     * @param statement
     *            the statement to execute on each iteration.
     */
    public ForeachStatement(String name, Expression expression, Statement statement) {
        if (name == null) {
            throw new IllegalArgumentException("Missing iteration variable name.");
        }
        if (expression == null) {
            throw new IllegalArgumentException("Missing iteration expression.");
        }
        if (statement == null) {
            throw new IllegalArgumentException("Missing iteration body.");
        }
        this.variableName = name;
        this.iterationExpression = expression;
        this.statement = statement;
    }

    public void execute(Environment env) throws FScriptException {
        checkEnvironment(env);
        Set<Node> nodes = (Set<Node>) iterationExpression.evaluate(env);
        for (Node node : nodes) {
            env.setVariable(variableName, node);
            statement.execute(env);
            if (env.getReturnValue() != null) {
                return;
            }
        }
    }
}
