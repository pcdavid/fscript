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

import java.util.List;

import net.jcip.annotations.Immutable;


/**
 * Represents a disjunction of boolean clauses (i.e. <code>clause1 || clause2 || clause3 ...</code>).
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class Disjunction extends CompositeNode<ASTNode> {
    /**
     * Creates a new <code>Disjunction</code> AST node.
     * 
     * @param location
     *            the location of the disjunction (including all the clauses) in the source code.
     *            May be null.
     * @param clauses
     *            the clauses of the disjunction, in the order they appear in the source.
     */
    public Disjunction(final SourceLocation location, final List<ASTNode> clauses) {
        super(location, "or", clauses);
    }

    public List<ASTNode> getClauses() {
        return operands;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitDisjunction(this);
    }
}
