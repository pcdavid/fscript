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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.console;

import org.objectweb.fractal.fscript.ast.SourceLocation;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.diagnostics.DiagnosticListener;

import com.google.common.base.Preconditions;

/**
 * A {@link DiagnosticListener} which reports diagnostic notifications directly to the
 * user session.
 * 
 * @author Pierre-Charles David
 */
class SessionDiagnosticListener implements DiagnosticListener {
    private final Session session;

    public SessionDiagnosticListener(Session session) {
        this.session = session;
    }

    public void report(Diagnostic diag) {
        Preconditions.checkNotNull(diag, "diagnostic");
        switch (diag.getSeverity()) {
        case INFORMATION:
            reportInformation(diag.getLocation(), diag.getMessage());
            break;
        case WARNING:
            reportWarning(diag.getLocation(), diag.getMessage());
            break;
        case ERROR:
            reportError(diag.getLocation(), diag.getMessage());
            break;
        default:
            throw new AssertionError("Unhandled severity: " + diag.getSeverity());
        }
    }

    public void reportInformation(SourceLocation location, String message) {
        session.showMessage("At " + location + ": " + message);
    }

    public void reportWarning(SourceLocation location, String message) {
        session.showWarning("At " + location + ": " + message);
    }

    public void reportError(SourceLocation location, String message) {
        session.showError("At " + location + ": " + message);
    }
}
