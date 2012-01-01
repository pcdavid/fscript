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

import static com.google.common.base.Preconditions.*;

import java.io.IOException;

import net.jcip.annotations.Immutable;


/**
 * Represents a reference to an FPath/FScript variable.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class VariableReference extends ASTNode {
    private final String variableName;

    /**
     * Creates a new <code>VariableReference</code> AST node.
     * 
     * @param location
     *            the location of the variable reference in the source code. May be null.
     * @param variableName
     *            the name of the referenced variable.
     */
    public VariableReference(final SourceLocation location, final String variableName) {
        super(location);
        checkNotNull(variableName, "Missing variable name.");
        checkArgument(variableName.length() != 0, "Invalid variable name.");
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitVariableReference(this);
    }

    @Override
    public int hashCode() {
        return variableName.hashCode() + 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof VariableReference) {
            return ((VariableReference) obj).variableName.equals(this.variableName);
        } else {
            return false;
        }
    }

    @Override
    public void toString(Appendable app) throws IOException {
        app.append("(variable-ref ").append(variableName).append(")");
    }
}
