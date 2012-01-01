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

/**
 * A visitor for <code>ASTNode</code>s: defines a different callback for each concrete kind of
 * AST node, which will be called in turn for each node being visited.
 * 
 * @see ASTNode#accept(ASTVisitor)
 * @author Pierre-Charles David
 */
public interface ASTVisitor {
    void visitAssignement(Assignment assign);

    void visitBlock(Block block);

    void visitCall(Call call);

    void visitConditional(Conditional cond);

    void visitConjunction(Conjunction conj);

    void visitConstant(Constant constant);

    void visitDisjunction(Disjunction dis);

    void visitExplicitReturn(ExplicitReturn ret);

    void visitLocationPath(LocationPath path);

    void visitLocationStep(LocationStep step);

    void visitLoop(Loop loop);

    void visitProcedure(UserProcedure proc);

    void visitVariableReference(VariableReference variable);
}
