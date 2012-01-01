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
import java.util.Arrays;
import java.util.List;

import net.jcip.annotations.Immutable;


import com.google.common.base.Join;

/**
 * Represents a procedure call in an FPath expression of FScript instruction. Functions and actions
 * calls can not be distinguished syntactically in the language, and this kind of AST node is used
 * to represent both. The procedure name must be resolved into an actual procedure to find which
 * kind it is.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class Call extends CompositeNode<ASTNode> {
    private final String procedureName;

    /**
     * Creates a new <code>Call</code> AST node.
     * 
     * @param location
     *            the location of the call (including all the arguments) in the source code. May be
     *            null.
     * @param procedureName
     *            the name of the procedure to call.
     * @param arguments
     *            the expressions to evaluate to yield the actual arguments of the call, in order.
     */
    public Call(final SourceLocation location, final String procedureName,
            final List<ASTNode> arguments) {
        super(location, "call", arguments);
        checkNotNull(procedureName, "Missing procedure name.");
        checkArgument(procedureName.length() != 0, "Invalid procedure name (empty).");
        this.procedureName = procedureName;
    }
    
    public Call(final SourceLocation location, final String procedureName, ASTNode... arguments) {
        this(location, procedureName, Arrays.asList(arguments));
    }

    public String getProcedureName() {
        return procedureName;
    }

    public List<ASTNode> getArguments() {
        return operands;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitCall(this);
    }

    @Override
    public void toString(Appendable app) throws IOException {
        app.append("(call ").append(procedureName);
        Join.join(app, " ", " ", operands);
        app.append(")");
    }
}
