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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pierre-Charles David
 */
public class VisitorMultiplexer implements ASTVisitor {
    protected final List<ASTVisitor> visitors;

    public VisitorMultiplexer() {
        this.visitors = new ArrayList<ASTVisitor>();
    }

    public VisitorMultiplexer(final List<ASTVisitor> visitors) {
        this.visitors = new ArrayList<ASTVisitor>(visitors);
    }

    public void visitAssignement(Assignment assign) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitAssignement(assign);
        }
    }

    public void visitBlock(Block block) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitBlock(block);
        }
    }

    public void visitCall(Call call) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitCall(call);
        }
    }

    public void visitConditional(Conditional cond) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitConditional(cond);
        }
    }

    public void visitConjunction(Conjunction conj) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitConjunction(conj);
        }
    }

    public void visitConstant(Constant constant) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitConstant(constant);
        }
    }

    public void visitDisjunction(Disjunction dis) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitDisjunction(dis);
        }
    }

    public void visitExplicitReturn(ExplicitReturn ret) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitExplicitReturn(ret);
        }
    }

    public void visitLocationPath(LocationPath path) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitLocationPath(path);
        }
    }

    public void visitLocationStep(LocationStep step) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitLocationStep(step);
        }
    }

    public void visitLoop(Loop loop) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitLoop(loop);
        }
    }

    public void visitProcedure(UserProcedure proc) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitProcedure(proc);
        }
    }

    public void visitVariableReference(VariableReference variable) {
        for (ASTVisitor visitor : visitors) {
            visitor.visitVariableReference(variable);
        }
    }
}
