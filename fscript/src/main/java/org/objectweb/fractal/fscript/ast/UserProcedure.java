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
import static com.google.common.collect.Lists.immutableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.objectweb.fractal.fscript.procedures.Procedure;
import org.objectweb.fractal.fscript.types.Signature;
import org.objectweb.fractal.fscript.types.Type;
import org.objectweb.fractal.fscript.types.UnionType;

import com.google.common.base.Join;
import com.google.common.base.Objects;

/**
 * Represents the definition of a new FScript procedure.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class UserProcedure extends ASTNode implements Procedure {
    private final boolean isPureFunction;

    private final String name;

    private final List<String> parameters;

    private final ASTNode body;

    /**
     * Creates a new <code>UserProcedure</code> AST node.
     * 
     * @param location
     *            the location of the procedure definition, including the whole body, in the source
     *            code. May be null.
     * @param isPureFunction
     *            indicates whether the procedure is a pure function (<code>true</code>) or an
     *            action (<code>false</code>).
     * @param name
     *            the name of the procedure.
     * @param parameters
     *            the list of formal parameters of the procedure.
     * @param body
     *            the body of the procedure.
     */
    public UserProcedure(final SourceLocation location, final boolean isPureFunction,
            final String name, final List<String> parameters, final ASTNode body) {
        super(location);
        checkNotNull(name, "Missing procedure name.");
        checkNotNull(parameters, "Missing parameters list.");
        checkContentsNotNull(parameters, "Null parameters not allowed.");
        checkNotNull(body, "Missing procedure body.");
        this.isPureFunction = isPureFunction;
        this.name = name;
        this.parameters = immutableList(parameters);
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }
    
    public boolean isPureFunction() {
        return isPureFunction;
    }
    
    public Signature getSignature() {
        Type any = UnionType.ANY_TYPE;
        List<Type> parametersTypes = new ArrayList<Type>();
        for (int i = 0; i < parameters.size(); i++) {
            parametersTypes.add(any);
        }
        return new Signature(any, parametersTypes);
    }

    public ASTNode getBody() {
        return body;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitProcedure(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(body, isPureFunction, name, parameters);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof UserProcedure) {
            final UserProcedure other = (UserProcedure) obj;
            return this.isPureFunction == other.isPureFunction && this.name.equals(other.name)
                    && Objects.deepEquals(this.parameters, other.parameters)
                    && this.body.equals(other.body);
        } else {
            return false;
        }
    }

    @Override
    protected void toString(Appendable app) throws IOException {
        app.append(isPureFunction ? "(function " : "(action ");
        app.append(name);
        app.append(" ");
        Join.join(app, " ", parameters);
        app.append(" ");
        body.toString(app);
        app.append(")");
    }
}
