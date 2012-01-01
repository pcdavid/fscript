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

import java.io.IOException;

import net.jcip.annotations.Immutable;


import com.google.common.base.Objects;

/**
 * Represents an explicit return statement in an FScript procedure, with an optional return value.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class ExplicitReturn extends ASTNode {
    private final ASTNode valueExpression;

    /**
     * Creates a new <code>ExplicitReturn</code> AST node.
     * 
     * @param location
     *            the location of the return instruction in the source code. May be null.
     * @param valueExpression
     *            the optional expression to evaluate to obtain the return value. May be null.
     */
    public ExplicitReturn(final SourceLocation location, final ASTNode valueExpression) {
        super(location);
        this.valueExpression = valueExpression;
    }

    public ASTNode getValueExpression() {
        return valueExpression;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitExplicitReturn(this);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(valueExpression);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ExplicitReturn) {
            final ExplicitReturn other = (ExplicitReturn) obj;
            return Objects.equal(this.valueExpression, other.valueExpression);
        } else {
            return false;
        }
    }

    @Override
    protected void toString(Appendable app) throws IOException {
        app.append("(return");
        if (valueExpression != null) {
            app.append(" ");
            valueExpression.toString(app);
        }
        app.append(")");
    }
}
