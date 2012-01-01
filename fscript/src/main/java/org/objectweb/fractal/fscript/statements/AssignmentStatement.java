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
 * Assigns a value to a variable.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class AssignmentStatement extends AbstractStatement {
    private final boolean isDeclaration;

    private final String variableName;

    private final Expression valueExpression;

    /**
     * Creates a new AssignementStatement.
     * 
     * @param name
     *            the name of the variable.
     * @param expression
     *            the expression whose value to assign.
     */
    public AssignmentStatement(boolean isDeclaration, String name, Expression expression) {
        if (name == null) {
            throw new IllegalArgumentException("Variable name must not be null.");
        }
        if (expression == null) {
            throw new IllegalArgumentException(
                    "Value expression for assignment must not be null.");
        }
        this.isDeclaration = isDeclaration;
        variableName = name;
        valueExpression = expression;
    }

    /**
     * Creates a new AssignementStatement.
     * 
     * @param name
     *            the name of the variable.
     * @param expression
     *            the expression whose value to assign.
     */
    public AssignmentStatement(String name, Expression expression) {
        this(false, name, expression);
    }

    public boolean isDeclaration() {
        return isDeclaration;
    }

    public void execute(Environment env) throws FScriptException {
        checkEnvironment(env);
        env.setVariable(variableName, valueExpression.evaluate(env));
    }
}
