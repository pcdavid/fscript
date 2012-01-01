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
 * A diagnostic reports an error or an important information about a script or a specific
 * part of a script.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class Diagnostic {
    // Convenience factory methods for the standard severities, with or without a location
    // specified.

    public static Diagnostic info(final SourceLocation location, final String message) {
        return new Diagnostic(Severity.INFORMATION, location, message);
    }

    public static Diagnostic warning(final SourceLocation location, final String message) {
        return new Diagnostic(Severity.WARNING, location, message);
    }

    public static Diagnostic error(final SourceLocation location, final String message) {
        return new Diagnostic(Severity.ERROR, location, message);
    }

    public static Diagnostic info(final String message) {
        return new Diagnostic(Severity.INFORMATION, message);
    }

    public static Diagnostic warning(final String message) {
        return new Diagnostic(Severity.WARNING, message);
    }

    public static Diagnostic error(final String message) {
        return new Diagnostic(Severity.ERROR, message);
    }

    /**
     * The severity of the diagnostic.
     */
    private final Severity severity;

    /**
     * The part of the script the diagnostic is about. Optional.
     */
    private final SourceLocation location;

    /**
     * The message explaining the diagnostic to the user.
     */
    private final String message;

    /**
     * Creates a new diagnostic.
     * 
     * @param severity
     *            the severity of the diagnostic.
     * @param location
     *            the part of the script the diagnostic is about.
     * @param message
     *            the message explaining the diagnostic to the user.
     */
    public Diagnostic(final Severity severity, final SourceLocation location,
            final String message) {
        this.severity = severity;
        this.location = location;
        this.message = message;
    }

    /**
     * Creates a new diagnostic.
     * 
     * @param severity
     *            the severity of the diagnostic.
     * @param message
     *            the message explaining the diagnostic to the user.
     */
    public Diagnostic(final Severity severity, final String message) {
        this(severity, null, message);
    }

    /**
     * Returns the part of the script the diagnostic is about.
     * 
     * @return the part of the script the diagnostic is about.
     */
    public SourceLocation getLocation() {
        return location;
    }

    /**
     * Returns the message explaining the diagnostic to the user.
     * 
     * @return the message explaining the diagnostic to the user.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the severity of this diagnostic.
     * 
     * @return the severity of this diagnostic.
     */
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        String loc = location == null ? "" : (location.toString() + " ");
        return loc + severity.toString() + ": " + message;
    }
}
