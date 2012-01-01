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

/**
 * This interface is used by tools to communicate diagnostics to their users (either other tools or
 * the end user). It mimics the interface of the same name from <code>javax.tools</code> in Java 6
 * (which we can not depend on for now).
 * 
 * @author Pierre-Charles David
 */
public interface DiagnosticListener {
    /**
     * Invoked to report a new diagnostic.
     * 
     * @param diagnostic
     *            a diagnostic representing a problem or additional information on the script being
     *            processed.
     */
    void report(Diagnostic diagnostic);
}
