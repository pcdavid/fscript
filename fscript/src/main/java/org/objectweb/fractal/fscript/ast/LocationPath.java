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

import net.jcip.annotations.Immutable;


import com.google.common.base.Join;

/**
 * Represents an anchored path expression in FPath.
 * 
 * @see LocationStep
 * @author Pierre-Charles David
 */
@Immutable
public class LocationPath extends CompositeNode<ASTNode> {
    private final ASTNode anchor;

    /**
     * Creates a new <code>LocationPath</code> AST node.
     * 
     * @param location
     *            the location of the path expression, including all the steps, in the source code.
     *            May be null.
     * @param anchor
     *            the expression yielding the initial node set for the path.
     * @param steps
     *            the (non-empty) sequence of steps in the path.
     */
    public LocationPath(final SourceLocation location, final ASTNode anchor,
            final List<ASTNode> steps) {
        super(location, "path", steps);
        checkNotNull(anchor, "Missing initial expression.");
        checkArgument(operands.size() > 0, "Invalid path: no steps.");
        this.anchor = anchor;
    }

    public ASTNode getAnchor() {
        return anchor;
    }

    public List<ASTNode> getSteps() {
        return operands;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitLocationPath(this);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + anchor.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals(obj)) {
            return false;
        } else if (obj instanceof LocationPath) {
            final LocationPath other = (LocationPath) obj;
            return this.anchor.equals(other.anchor);
        } else {
            return false;
        }
    }

    @Override
    public void toString(Appendable app) throws IOException {
        app.append("(path ");
        anchor.toString(app);
        Join.join(app, " ", " ", operands);
        app.append(")");
    }
}
