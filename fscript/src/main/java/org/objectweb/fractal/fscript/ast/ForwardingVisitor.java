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

import net.jcip.annotations.Immutable;

/**
 * An implementation of {@link ASTVisitor} which forwards all the invocations it receives to a
 * delegate. Useful as a base class to create a proxy when only a few methods must be overridden
 * while keeping the normal behavior for the others.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class ForwardingVisitor implements ASTVisitor {
    private final ASTVisitor delegate;

    public ForwardingVisitor(final ASTVisitor delegate) {
        this.delegate = delegate;
    }

    public void visitAssignement(Assignment assign) {
        delegate.visitAssignement(assign);
    }

    public void visitBlock(Block block) {
        delegate.visitBlock(block);
    }

    public void visitCall(Call call) {
        delegate.visitCall(call);
    }

    public void visitConditional(Conditional cond) {
        delegate.visitConditional(cond);
    }

    public void visitConjunction(Conjunction conj) {
        delegate.visitConjunction(conj);
    }

    public void visitConstant(Constant constant) {
        delegate.visitConstant(constant);
    }

    public void visitDisjunction(Disjunction dis) {
        delegate.visitDisjunction(dis);
    }

    public void visitExplicitReturn(ExplicitReturn ret) {
        delegate.visitExplicitReturn(ret);
    }

    public void visitLocationPath(LocationPath path) {
        delegate.visitLocationPath(path);
    }

    public void visitLocationStep(LocationStep step) {
        delegate.visitLocationStep(step);
    }

    public void visitLoop(Loop loop) {
        delegate.visitLoop(loop);
    }

    public void visitProcedure(UserProcedure proc) {
        delegate.visitProcedure(proc);
    }

    public void visitVariableReference(VariableReference variable) {
        delegate.visitVariableReference(variable);
    }
}
