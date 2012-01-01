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
package org.objectweb.fractal.fscript.procedures;

import org.objectweb.fractal.fscript.types.Signature;

/**
 * Common super-type for both native and user-defined procedures.
 * 
 * @author Pierre-Charles David
 */
public interface Procedure {
    /**
     * Returns the name of the procedure.
     * 
     * @return the name of the procedure.
     */
    String getName();

    /**
     * Tests whether this procedure is a pure function (i.e. without side-effect), or an
     * action (with potential side-effects).
     * 
     * @return <code>true</code> iff this procedure is a pure function.
     */
    boolean isPureFunction();

    /**
     * Returns the signature of this procedure.
     * 
     * @return the signature of this procedure.
     */
    Signature getSignature();
}
