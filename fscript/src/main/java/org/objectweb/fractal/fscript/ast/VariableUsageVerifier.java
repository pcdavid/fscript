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

import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.fscript.InvalidScriptException;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.diagnostics.DiagnosticCollector;
import org.objectweb.fractal.fscript.diagnostics.DiagnosticListener;

/**
 * @author Pierre-Charles David
 */
public class VariableUsageVerifier {
    public void verify(UserProcedure proc) throws InvalidScriptException {
        DiagnosticCollector dc = new DiagnosticCollector();
        VariableVisitor visitor = new VariableVisitor(dc);
        proc.accept(new TopDownVisitor(visitor));
        visitor.checkAllVariablesUsed();
        for (Diagnostic diag : dc.getDiagnostics()) {
            System.out.println(diag);
        }
    }

    class VariableVisitor extends ASTVisitorAdapter {
        private DiagnosticListener diags;

        private Set<String> declaredVariables = new HashSet<String>();

        private Set<String> setVariables = new HashSet<String>();

        private Set<String> usedVariables = new HashSet<String>();

        public VariableVisitor(DiagnosticListener diags) {
            this.diags = diags;
        }

        public void checkAllVariablesUsed() {
            for (String var : declaredVariables) {
                if (!usedVariables.contains(var)) {
                    String msg = "Variable " + var + " declared but never used.";
                    diags.report(Diagnostic.warning(null, msg));
                }
            }
        }

        @Override
        public void visitProcedure(UserProcedure proc) {
            for (String param : proc.getParameters()) {
                declaredVariables.add(param);
                setVariables.add(param);
            }
        }

        @Override
        public void visitAssignement(Assignment assign) {
            String var = assign.getVariableName();
            if (assign.isDeclaration()) {
                if (declaredVariables.contains(var)) {
                    String msg = "Variable " + var + " already declared.";
                    diags.report(Diagnostic.warning(null, msg));
                } else {
                    declaredVariables.add(var);
                }
            }
            if (assign.getValueExpression() != null) {
                setVariables.add(var);
            }
        }

        @Override
        public void visitVariableReference(VariableReference variable) {
            String var = variable.getVariableName();
            if (!declaredVariables.contains(var)) {
                diags.report(Diagnostic.error(null, "Variable " + var + " not declared."));
            } else if (!setVariables.contains(var)) {
                diags.report(Diagnostic.error(null, "Variable " + var + " was never set."));
            }
            usedVariables.add(var);
        }
    }
}
