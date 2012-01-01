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
 * Represents of block statement, i.e. a finite sequence of FScript instructions. In FScript, blocks
 * are used only for specific parts of a program:
 * <ul>
 * <li>the body of a procedure must be a block;</li>
 * <li>the body of an iteration loop (<code>for</code>) must be a block;</li>
 * <li>the "then" part of a conditional (<code>if</code>) must be a block;</li>
 * <li>if present, the "else" part of a conditional must be a block.</li>
 * </ul>
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class Block extends CompositeNode<ASTNode> {
    /**
     * Creates a new AST node representing a block statement.
     * 
     * @param location
     *            the location of the block in the source code. May be null.
     * @param steps
     *            the AST nodes representing the instructions in the block, in order.
     */
    public Block(final SourceLocation location, final List<ASTNode> steps) {
        super(location, "block", steps);
    }

    public List<ASTNode> getSteps() {
        return operands;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitBlock(this);
    }
}
