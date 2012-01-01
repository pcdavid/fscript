/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
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
package org.objectweb.fractal.fscript.diagnostics;

import static com.google.common.base.Preconditions.checkNotNull;
import net.jcip.annotations.Immutable;

import org.objectweb.fractal.fscript.ast.SourceLocation;
import org.objectweb.util.monolog.api.Logger;

/**
 * A diagnostic reports an error or an important information about a script or a specific part of a
 * script. Diagnostics are targeted to the end-user and should be kept at the level of the FPath and
 * FScript language. Use a {@link Logger} instead to monitor the internals of the FScript
 * implementation itself.
 * 
 * @author Pierre-Charles David
 */
@Immutable
public class Diagnostic {
    /**
     * Creates a {@link Diagnostic} with severity level {@link Severity#INFORMATION}.
     */
    public static Diagnostic information(SourceLocation loc, String msg) {
        return new Diagnostic(Severity.INFORMATION, loc, msg);
    }

    public static Diagnostic information(SourceLocation loc, String fmt, Object... args) {
        String msg = String.format(fmt, args);
        return new Diagnostic(Severity.INFORMATION, loc, msg);
    }

    /**
     * Creates a {@link Diagnostic} with severity level {@link Severity#WARNING}.
     */
    public static Diagnostic warning(SourceLocation loc, String msg) {
        return new Diagnostic(Severity.WARNING, loc, msg);
    }

    public static Diagnostic warning(SourceLocation loc, String fmt, Object... args) {
        String msg = String.format(fmt, args);
        return new Diagnostic(Severity.WARNING, loc, msg);
    }

    /**
     * Creates a {@link Diagnostic} with severity level {@link Severity#ERROR}.
     */
    public static Diagnostic error(SourceLocation loc, String msg) {
        return new Diagnostic(Severity.ERROR, loc, msg);
    }

    public static Diagnostic error(SourceLocation loc, String fmt, Object... args) {
        String msg = String.format(fmt, args);
        return new Diagnostic(Severity.ERROR, loc, msg);
    }

    /**
     * The severity of the diagnostic.
     */
    private final Severity severity;

    /**
     * The part of the script the diagnostic is about.
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
    public Diagnostic(final Severity severity, final SourceLocation location, final String message) {
        checkNotNull(severity, "No severity specified.");
        checkNotNull(location, "No source location specified.");
        checkNotNull(message, "No diagnostic message specified");
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
        this(severity, SourceLocation.UNKNOWN, message);
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
        return severity + " at " + location + ": " + message;
    }
}
