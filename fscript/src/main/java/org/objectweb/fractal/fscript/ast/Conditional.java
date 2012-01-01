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
 * Represents a conditional expression (if/then/else) in FScript.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class Conditional extends ASTNode {
    private final ASTNode testExpression;

    private final ASTNode trueBranch;

    private final ASTNode falseBranch;

    /**
     * Creates a new <code>Conditional</code> AST node.
     * 
     * @param location
     *            the location of the conditional (including the branches) in the source code. May
     *            be null.
     * @param testExpression
     *            the test expression to evaluate to decide which branch of the conditional to
     *            execute.
     * @param trueBranch
     *            the code to execute if the test expression evaluates to <code>true</code>.
     * @param falseBranch
     *            the code to execute if the test expression evaluates to <code>false</code>. If
     *            <code>null</code> and if the test expression evaluates to <code>false</code>,
     *            nothing is executed.
     */
    public Conditional(final SourceLocation location, final ASTNode testExpression,
            final ASTNode trueBranch, final ASTNode falseBranch) {
        super(location);
        checkNotNull(testExpression, "Missing test for conditional.");
        checkNotNull(trueBranch, "Missing 'then' branch for conditional.");
        this.testExpression = testExpression;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    public ASTNode getTestExpression() {
        return testExpression;
    }

    public ASTNode getTrueBranch() {
        return trueBranch;
    }

    public ASTNode getFalseBranch() {
        return falseBranch;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitConditional(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(testExpression, trueBranch, falseBranch);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Conditional) {
            final Conditional other = (Conditional) obj;
            return this.testExpression.equals(other.testExpression)
                    && this.trueBranch.equals(other.trueBranch)
                    && Objects.equal(this.falseBranch, other.falseBranch);
        } else {
            return false;
        }
    }

    @Override
    protected void toString(Appendable app) throws IOException {
        app.append("(if ");
        testExpression.toString(app);
        app.append(" ");
        trueBranch.toString(app);
        if (falseBranch != null) {
            app.append(" ");
            falseBranch.toString(app);
        }
        app.append(")");
    }
}
