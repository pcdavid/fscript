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

/**
 * This interface is used by tools to communicate diagnostics to their users (either other
 * tools or the end user). It mimicks the interface of the same name from
 * <code>javax.tools</code> in Java 6 (which we can not depend on for now).
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public interface DiagnosticListener {
    /**
     * Invoked to report a new diagnostic.
     * 
     * @param diagnostic
     *            a diagnostic representing a problem or additional information on the
     *            script being processed.
     */
    void report(Diagnostic diagnostic);

    /**
     * Convenience method to report an {@linkplain Severity#INFORMATION information}
     * diagnostic.
     * 
     * @param location
     *            the part of the script the diagnostic is about.
     * @param message
     *            the message explaining the diagnostic to the user.
     */
    void reportInformation(final SourceLocation location, final String message);

    /**
     * Convenience method to report a {@linkplain Severity#WARNING warning} diagnostic. *
     * 
     * @param location
     *            the part of the script the diagnostic is about.
     * @param message
     *            the message explaining the diagnostic to the user.
     */
    void reportWarning(final SourceLocation location, final String message);

    /**
     * Convenience method to report an {@linkplain Severity#ERROR error} diagnostic.
     * 
     * @param location
     *            the part of the script the diagnostic is about.
     * @param message
     *            the message explaining the diagnostic to the user.
     */
    void reportError(final SourceLocation location, final String message);
}
