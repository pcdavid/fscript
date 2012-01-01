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
 * Represents a variable assignment. There are two forms of assignments in FScript:
 * <ol>
 * <li><em>initial declaration</em>: <code>var myVariable = "initial value";</code></li>
 * <li><em>re-assignment</em> of a variable previously declared in the same scope:
 * <code>myVariable = "new value";</code>.</li>
 * </ol>
 * In the language, they are distinguished by the presence (or absence) of the keyword
 * <code>var</code>. In this class, they are distinguished using the <code>declaration</code>
 * boolean attribute (see {@link #isDeclaration()}).
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class Assignment extends ASTNode {
    private final boolean isDeclaration;

    private final String variableName;

    private final ASTNode value;

    /**
     * Creates a new AST node representing an assignment.
     * 
     * @param location
     *            the location of the assignment in the source code. May be null.
     * @param isDeclaration
     *            indicates whether this is a variable declaration (<code>true</code>) or a
     *            re-assignment of a previously declared variable (<code>false</code>).
     * @param variableName
     *            the name of the variable (lvalue) being assigned.
     * @param value
     *            the expression which will result in the new value to assign the the variable.
     */
    public Assignment(final SourceLocation location, final boolean isDeclaration,
            final String variableName, final ASTNode value) {
        super(location);
        checkNotNull(variableName, "Missing variable name.");
        checkNotNull(value, "Missing value expression.");
        this.isDeclaration = isDeclaration;
        this.variableName = variableName;
        this.value = value;
    }

    public boolean isDeclaration() {
        return isDeclaration;
    }

    public String getVariableName() {
        return variableName;
    }

    public ASTNode getValueExpression() {
        return value;
    }

    @Override
    public void accept(final ASTVisitor visitor) {
        visitor.visitAssignement(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isDeclaration, variableName, value);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Assignment) {
            final Assignment other = (Assignment) obj;
            return this.isDeclaration == other.isDeclaration
                    && this.variableName.equals(other.variableName)
                    && this.value.equals(other.value);
        } else {
            return false;
        }
    }

    @Override
    protected void toString(Appendable app) throws IOException {
        app.append(isDeclaration ? "(declare " : "(assign ");
        app.append(variableName).append(" ");
        value.toString(app);
        app.append(")");
    }
}
