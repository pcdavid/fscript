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
package org.objectweb.fractal.fscript.diagnostics;

import java.util.List;

/**
 * Provides access to the history of diagnostics received by a component.
 * 
 * @author Pierre-Charles David
 */
public interface DiagnosticLog {
    /**
     * Returns all the available diagnostics, in the order they were received.
     * 
     * @return the diagnostics reported so far.
     */
    List<Diagnostic> getDiagnostics();

    /**
     * Returns all the available diagnostics above a given severity level, in the order they were
     * received.
     * 
     * @param minSeverity
     *            the minimum severity level to consider. Only diagnostics with a severity greater
     *            than or equal to <code>minSeverity</code> are included in the result.
     * @return the available diagnostics with a severity level at least equal to
     *         <code>minSeverity</code>.
     */
    List<Diagnostic> getDiagnostics(Severity minSeverity);

    /**
     * Removes all the diagnostics currently in the log.
     */
    void clear();
}
