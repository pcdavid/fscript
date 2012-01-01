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
package org.objectweb.fractal.fscript;

import java.util.Collections;
import java.util.List;

/**
 * Generic exception for all the errors which can occur in FScript.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class FScriptException extends Exception {
    private final DiagnosticCollector diagnostics;

    public FScriptException(String message) {
        super(message);
        diagnostics = null;
    }

    public FScriptException(String message, Throwable cause) {
        super(message, cause);
        diagnostics = null;
    }

    public FScriptException(Throwable cause) {
        super(cause);
        diagnostics = null;
    }

    public FScriptException(Diagnostic diagnostic) {
        super(diagnostic.toString());
        this.diagnostics = new DiagnosticCollector();
        this.diagnostics.report(diagnostic);
        assert isFatal(diagnostic);
    }

    public FScriptException(DiagnosticCollector diagnostics) {
        super("Invalid script.");
        this.diagnostics = diagnostics;
        assert atLeastOneFatal(diagnostics);
    }

    public List<Diagnostic> getDiagnostics() {
        if (diagnostics == null) {
            return Collections.emptyList();
        } else {
            return diagnostics.getDiagnostics();
        }
    }

    private boolean isFatal(Diagnostic diagnostic) {
        return diagnostic.getSeverity().compareTo(Severity.ERROR) >= 0;
    }
    
    private boolean atLeastOneFatal(DiagnosticCollector dc) {
        for (Diagnostic diag : dc.getDiagnostics()) {
            if (isFatal(diag)) {
                return true;
            }
        }
        return false;
    }
}
