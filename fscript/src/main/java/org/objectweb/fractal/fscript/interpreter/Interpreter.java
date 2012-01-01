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
package org.objectweb.fractal.fscript.interpreter;

import static org.objectweb.fractal.fscript.diagnostics.Severity.ERROR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.AbstractReporter;
import org.objectweb.fractal.fscript.Executor;
import org.objectweb.fractal.fscript.Library;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.ast.ASTNode;
import org.objectweb.fractal.fscript.ast.UserProcedure;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.fractal.fscript.procedures.Procedure;

public class Interpreter extends AbstractReporter implements Executor, BindingController {
    private Library library;

    private DynamicTypeChecker checker = new DynamicTypeChecker();

    public Object invoke(String procName, Object[] args, Map<String, Object> globals)
            throws ScriptExecutionError {
        return apply(procName, args, new Context(globals));
    }

    Object apply(String procName, Object[] args, Context ctxt) throws ScriptExecutionError {
        Procedure proc = library.lookup(procName);
        if (proc == null) {
            Diagnostic err = new Diagnostic(ERROR, "No such procedure (" + procName + ").");
            throw new ScriptExecutionError(err);
        }
        
        Object[] checkedArgs = checker.checkedArguments(proc, args);
        Object result = null;
        if (proc instanceof NativeProcedure) {
            NativeProcedure nativeProc = (NativeProcedure) proc;
            result = nativeProc.apply(Arrays.asList(checkedArgs), ctxt);
        } else {
            result = invokeUser((UserProcedure) proc, checkedArgs, ctxt);
        }
        return checker.checkedResult(proc, result);
    }

    private Object invokeUser(UserProcedure userProc, Object[] args, Context ctxt)
            throws ScriptExecutionError {
        Context callCtxt = new Context(ctxt);
        int i = 0;
        for (String param : userProc.getParameters()) {
            callCtxt.setLocal(param, args[i++]);
        }
        return evaluate(userProc.getBody(), callCtxt);
    }

    private Object evaluate(ASTNode script, Context ctx) throws ScriptExecutionError {
        EvaluatingVisitor evaluator = new EvaluatingVisitor(this, ctx);
        try {
            return evaluator.evaluate(script);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ScriptExecutionError) {
                Diagnostic error = new Diagnostic(ERROR, "At " + evaluator.getCurrentLocation());
                throw new ScriptExecutionError(error, cause);
            } else {
                throw new RuntimeException("Error executing script at "
                        + evaluator.getCurrentLocation(), e);
            }
        }
    }

    NativeProcedure getAxisFunction(String axisName) {
        Procedure proc = library.lookup("axis:" + axisName);
        if (proc instanceof NativeProcedure) {
            return (NativeProcedure) proc;
        } else {
            return null;
        }
    }

    @Override
    public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException {
        if ("library".equals(clientItfName)) {
            this.library = (Library) serverItf;
        } else {
            super.bindFc(clientItfName, serverItf);
        }
    }

    @Override
    public String[] listFc() {
        List<String> clients = new ArrayList<String>();
        for (String itfName : super.listFc()) {
            clients.add(itfName);
        }
        clients.add("library");
        return clients.toArray(new String[0]);
    }

    @Override
    public Object lookupFc(String clientItfName) throws NoSuchInterfaceException {
        if ("library".equals(clientItfName)) {
            return this.library;
        } else {
            return super.lookupFc(clientItfName);
        }
    }

    @Override
    public void unbindFc(String clientItfName) throws NoSuchInterfaceException {
        if ("library".equals(clientItfName)) {
            this.library = null;
        } else {
            super.unbindFc(clientItfName);
        }
    }
}
