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

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.diagnostics.DiagnosticListener;

/**
 * This interface abstracts all the user interaction features and configuration changes available to
 * special commands.
 * 
 * @author Pierre-Charles David
 */
public interface Session {
    void showMessage(String message);

    void showResult(Object result);

    void showWarning(String warning);

    void showError(String error);

    void showError(String error, Throwable cause);

    void showTitle(String title);

    void showTable(String[][] table);

    void newline();

    DiagnosticListener getDiagnosticListener();

    void setSessionInterpreter(Component fscript);
}
