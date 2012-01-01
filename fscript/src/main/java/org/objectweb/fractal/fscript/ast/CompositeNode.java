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
import java.util.List;


import com.google.common.base.Join;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * Abstract base class for all the kinds of AST nodes which represent an operation on a sequence of
 * operands (themselves AST nodes).
 * 
 * @author Pierre-Charles David
 * @param <T>
 *            the type of the acceptable operands.
 */
abstract class CompositeNode<T extends ASTNode> extends ASTNode {
    protected final String operation;

    protected final List<T> operands;

    /**
     * Creates a new <code>CompositeNode</code>.
     * 
     * @param location
     *            the location of the composite operation in the source. May be null.
     * @param operation
     *            a name identifying the kind of operation. Used only internally to distinguish the
     *            different concrete subclasses.
     * @param operands
     *            the sequence of operands to which the operation must be applied.
     */
    protected CompositeNode(final SourceLocation location, final String operation,
            final List<T> operands) {
        super(location);
        checkNotNull(operation, "Missing operation name.");
        checkNotNull(operands, "Missing operands.");
        checkContentsNotNull(operands, "Invalid operands: null not allowed.");
        this.operation = operation;
        this.operands = Lists.immutableList(operands);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(operation, operands);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof CompositeNode) {
            final CompositeNode<?> other = (CompositeNode<?>) obj;
            return this.operation.equals(other.operation)
                    && Objects.deepEquals(this.operands, other.operands);
        } else {
            return false;
        }
    }

    @Override
    protected void toString(Appendable app) throws IOException {
        app.append("(");
        app.append(operation);
        Join.join(app, " ", " ", operands);
        app.append(")");
    }
}
