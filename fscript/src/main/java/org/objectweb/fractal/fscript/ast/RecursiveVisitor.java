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
 * @author Pierre-Charles David
 */
public class RecursiveVisitor implements ASTVisitor {
    private final ASTVisitor delegate;

    public RecursiveVisitor(final ASTVisitor delegate) {
        this.delegate = delegate;
    }

    public void visitAssignement(Assignment assign) {
        final ASTNode valueExpression = assign.getValueExpression();
        if (valueExpression != null) {
            valueExpression.accept(delegate);
        }
    }

    public void visitBlock(Block block) {
        for (ASTNode step : block.getSteps()) {
            step.accept(delegate);
        }
    }

    public void visitCall(Call call) {
        for (ASTNode argument : call.getArguments()) {
            argument.accept(delegate);
        }
    }

    public void visitConditional(Conditional cond) {
        cond.getTestExpression().accept(delegate);
        cond.getTrueBranch().accept(delegate);
        if (cond.getFalseBranch() != null) {
            cond.getFalseBranch().accept(delegate);
        }
    }

    public void visitConjunction(Conjunction conj) {
        for (ASTNode clause : conj.getClauses()) {
            clause.accept(delegate);
        }
    }

    public void visitConstant(Constant constant) {
        // No sub-nodes.
    }

    public void visitDisjunction(Disjunction dis) {
        for (ASTNode clause : dis.getClauses()) {
            clause.accept(delegate);
        }
    }

    public void visitExplicitReturn(ExplicitReturn ret) {
        if (ret.getValueExpression() != null) {
            ret.getValueExpression().accept(delegate);
        }
    }

    public void visitLocationPath(LocationPath path) {
        path.getAnchor().accept(delegate);
        for (ASTNode step : path.getSteps()) {
            step.accept(delegate);
        }
    }

    public void visitLocationStep(LocationStep step) {
        for (ASTNode predicate : step.getPredicates()) {
            predicate.accept(delegate);
        }
    }

    public void visitLoop(Loop loop) {
        loop.getIterationExpression().accept(delegate);
        loop.getBody().accept(delegate);
    }

    public void visitProcedure(UserProcedure proc) {
        proc.getBody().accept(delegate);
    }

    public void visitVariableReference(VariableReference variable) {
        // No sub-nodes.
    }

}
