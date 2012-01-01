/*
 * Copyright (c) 2006-2008 ARMINES
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
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript.ast;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import net.jcip.annotations.Immutable;


import com.google.common.base.Objects;

/**
 * Represents an FScript loop/iteration.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class Loop extends ASTNode {
    private final String iterationVariableName;

    private final ASTNode iterationExpression;

    private final ASTNode body;

    /**
     * Creates a new <code>Loop</code> AST node.
     * 
     * @param location
     *            the location of the loop, including the whole body, in the source code. May be
     *            null.
     * @param variableName
     *            the name of the iteration variable.
     * @param expression
     *            the expression to evaluate to yield the set of values to iterate on.
     * @param body
     *            the instructions to evaluate for each iteration.
     */
    public Loop(final SourceLocation location, final String variableName, final ASTNode expression,
            final ASTNode body) {
        super(location);
        checkNotNull(variableName, "Missing iteration variable.");
        checkNotNull(expression, "Missing iteration expression.");
        checkNotNull(body, "Missing loop body.");
        this.iterationVariableName = variableName;
        this.iterationExpression = expression;
        this.body = body;
    }

    public String getIterationVariableName() {
        return iterationVariableName;
    }

    public ASTNode getIterationExpression() {
        return iterationExpression;
    }

    public ASTNode getBody() {
        return body;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitLoop(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(iterationVariableName, iterationExpression, body);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Loop) {
            final Loop other = (Loop) obj;
            return this.iterationVariableName.equals(other.iterationVariableName)
                    && this.iterationExpression.equals(other.iterationExpression)
                    && this.body.equals(other.body);
        } else {
            return false;
        }
    }

    @Override
    public void toString(Appendable app) throws IOException {
        app.append("(for ");
        app.append(iterationVariableName).append(" ");
        iterationExpression.toString(app);
        app.append(" ");
        body.toString(app);
        app.append(")");
    }
}
