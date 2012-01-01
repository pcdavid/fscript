/*
 * Copyright (c) 2007-2008 ARMINES
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
package org.objectweb.fractal.fscript;

import static org.objectweb.util.monolog.api.BasicLevel.DEBUG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.ast.ASTVisitorAdapter;
import org.objectweb.fractal.fscript.ast.Call;
import org.objectweb.fractal.fscript.ast.TopDownVisitor;
import org.objectweb.fractal.fscript.ast.UserProcedure;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.fractal.fscript.procedures.Procedure;

/**
 * This component implements the model-independent static language checks on user-defined
 * procedures when they are loaded. The checks currently performed are very minimal, but
 * it should be relatively easy to add more.
 * 
 * @author Pierre-Charles David
 */
public class LanguageCheckerImpl extends AbstractReporter implements LanguageChecker,
        BindingController {
    /**
     * The library which contains the procedures already loaded.
     */
    private Library library;

    public void check(UserProcedure proc) throws InvalidScriptException {
        log(DEBUG, "LanguageChecker: checking procedure definition " + proc.getName() + ".");
        String name = proc.getName();
        Procedure previous = library.lookup(name);
        if (previous instanceof NativeProcedure) {
            String msg = "Can not redefine native procedure '" + name + "'.";
            report(Diagnostic.error(proc.getSourceLocation(), msg));
            throw new InvalidScriptException(Diagnostic.error(proc.getSourceLocation(), msg));
        } else if (previous != null) {
            String msg = "Procedure '" + name + "' redefined.";
            report(Diagnostic.information(proc.getSourceLocation(), msg));
        }
        checkDependencies(proc);
        checkUniqueArgumentsNames(proc);
    }

    /**
     * Checks that the signature of the procedure uses unique parameter names.
     */
    private void checkUniqueArgumentsNames(UserProcedure proc) throws InvalidScriptException {
        Set<String> names = new HashSet<String>();
        int i = 0;
        for (String param : proc.getParameters()) {
            if (names.contains(param)) {
                Diagnostic error = Diagnostic.error(proc.getSourceLocation(),
                        "Duplicate parameter name '%s' at position %d in procedure '%s'.", param,
                        i, proc.getName());
                report(error);
                throw new InvalidScriptException(error);
            } else {
                names.add(param);
            }
            i += 1;
        }
    }

    /**
     * Checks that the procedure only references already defined procedures, and that
     * functions do not call actions.
     */
    private void checkDependencies(UserProcedure proc) {
        for (String name : getDependencies(proc)) {
            Procedure dep = library.lookup(name);
            if (dep == null) {
                String msg = "Definition references currently unknown procedure " + name + "().";
                report(Diagnostic.warning(proc.getSourceLocation(), msg));
            } else if (proc.isPureFunction() && !dep.isPureFunction()) {
                String msg = "Procedure declared as pure function but calls action " + dep + ".";
                report(Diagnostic.error(proc.getSourceLocation(), msg));
            }
        }
    }

    /**
     * Finds the names of all the procedures called by the given {@link UserProcedure}.
     */
    private Set<String> getDependencies(UserProcedure proc) {
        final Set<String> dependencies = new HashSet<String>();
        proc.getBody().accept(new TopDownVisitor(new ASTVisitorAdapter() {
            @Override
            public void visitCall(Call call) {
                dependencies.add(call.getProcedureName());
            }
        }));
        return dependencies;
    }

    @Override
    public void bindFc(String clItfName, Object srvItf) throws NoSuchInterfaceException {
        if ("library".equals(clItfName)) {
            this.library = (Library) srvItf;
        } else {
            super.bindFc(clItfName, srvItf);
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
    public Object lookupFc(String clItfName) throws NoSuchInterfaceException {
        if ("library".equals(clItfName)) {
            return this.library;
        } else {
            return super.lookupFc(clItfName);
        }
    }

    @Override
    public void unbindFc(String clItfName) throws NoSuchInterfaceException {
        if ("library".equals(clItfName)) {
            this.library = null;
        } else {
            super.unbindFc(clItfName);
        }
    }
}
