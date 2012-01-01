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


/**
 * Common base class for all the different kinds of AST nodes in FPath and FScript. Each concrete
 * subclass represents one construct in the language.
 * 
 * @author Pierre-Charles David
 */
public abstract class ASTNode {
    private final SourceLocation sourceLocation;

    /**
     * Creates a new <code>ASTNode</code>.
     * 
     * @param location
     *            the location of the corresponding fragment in the source code. May be null if the
     *            location is unknown or if there was no actual source code (e.g. the AST is
     *            constructed programmatically).
     */
    public ASTNode(SourceLocation location) {
        this.sourceLocation = location;
    }

    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    /**
     * Part of the implementation of the <em>visitor pattern</em> for AST nodes.
     */
    public abstract void accept(ASTVisitor visitor);

    protected abstract void toString(Appendable appendable) throws IOException;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        try {
            toString(sb);
        } catch (IOException e) {
            throw new RuntimeException("Error converting ASTNode to text.", e);
        }
        return sb.toString();
    }
}
