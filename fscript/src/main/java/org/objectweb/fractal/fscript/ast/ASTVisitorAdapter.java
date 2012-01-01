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
 * An empty implementation of the {@link ASTVisitor} interface. This makes it more
 * convenient to implement <code>ASTVisitor</code> when only some of the methods must be
 * implemented.
 * 
 * @author Pierre-Charles David
 */
public class ASTVisitorAdapter implements ASTVisitor {
    public void visitAssignement(Assignment assign) {
        // Do nothing by default.
    }

    public void visitBlock(Block block) {
        // Do nothing by default.
    }

    public void visitCall(Call call) {
        // Do nothing by default.
    }

    public void visitConditional(Conditional cond) {
        // Do nothing by default.
    }

    public void visitConjunction(Conjunction conj) {
        // Do nothing by default.
    }

    public void visitConstant(Constant constant) {
        // Do nothing by default.
    }

    public void visitDisjunction(Disjunction dis) {
        // Do nothing by default.
    }

    public void visitExplicitReturn(ExplicitReturn ret) {
        // Do nothing by default.
    }

    public void visitLocationPath(LocationPath path) {
        // Do nothing by default.
    }

    public void visitLocationStep(LocationStep step) {
        // Do nothing by default.
    }

    public void visitLoop(Loop loop) {
        // Do nothing by default.
    }

    public void visitProcedure(UserProcedure proc) {
        // Do nothing by default.
    }

    public void visitVariableReference(VariableReference variable) {
        // Do nothing by default.
    }
}
