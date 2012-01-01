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
 * Represent a constant node (typically a literal in the source).
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class Constant extends ASTNode {
    private final Object value;

    /**
     * Creates a new <code>Constant</code> AST node.
     * 
     * @param location
     *            the location of the constant literal in the source code. May be null.
     * @param value
     *            the value of the constant. Must be a valid FScript value.
     */
    public Constant(final SourceLocation location, final Object value) {
        super(location);
        checkNotNull(value, "Missing value for constant.");
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitConstant(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Constant) {
            final Constant other = (Constant) obj;
            return this.value.equals(other.value);
        } else {
            return false;
        }
    }

    @Override
    public void toString(Appendable app) throws IOException {
        app.append("(constant ");
        if (value instanceof String) {
            final String quoted = ((String) value).replaceAll("\"", "\\\"");
            app.append(quoted);
        } else {
            app.append(String.valueOf(value));
        }
        app.append(")");
    }
}
