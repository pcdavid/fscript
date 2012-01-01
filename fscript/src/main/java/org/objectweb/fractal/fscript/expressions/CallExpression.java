/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2007 ARMINES
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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.Procedure;

/**
 * Represents a procedure call with arguments. Arguments are evaluated in order, and the
 * results are passed to the underlying procedure.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class CallExpression extends AbstractExpression {
    /**
     * The name of the procedure to call.
     */
    private final String procName;

    /**
     * The expressions to evaluate to obtain the arguments to the call.
     */
    private List<Expression> parameters;

    public CallExpression(String procName, Expression... params) {
        this(procName, Arrays.asList(params));
    }

    /**
     * Creates a new <code>CallExpression</code>.
     * 
     * @param procName
     *            the name of the procedure to call
     * @param params
     *            the expressions denoting the actual parameters
     */
    public CallExpression(String procName, List<Expression> params) {
        if (procName == null) {
            throw new IllegalArgumentException("Missing procedure name.");
        }
        this.procName = procName;
        if (params != null) {
            this.parameters = new ArrayList<Expression>(params);
        } else {
            this.parameters = Collections.emptyList();
        }
    }

    public Object evaluate(Environment env) throws FScriptException {
        checkEnvironment(env);
        Procedure proc = env.getProcedure(procName);
        if (proc == null) {
            throw new FScriptException("Undefined procedure '" + procName + "'.");
        }
        List<Object> args = evalArguments(env);
        return proc.apply(args.toArray(), env);
    }

    /**
     * Evaluates the parameters to yield the actual call arguments.
     * 
     * @param env
     *            the evaluation environment.
     * @return the call arguments for the procedure.
     * @throws FScriptException
     *             if one of the parameters caused an <code>FScriptException</code>
     *             during its evaluation.
     */
    private List<Object> evalArguments(Environment env) throws FScriptException {
        List<Object> arguments = new ArrayList<Object>();
        for (Expression param : parameters) {
            arguments.add(param.evaluate(env));
        }
        return arguments;
    }
}
