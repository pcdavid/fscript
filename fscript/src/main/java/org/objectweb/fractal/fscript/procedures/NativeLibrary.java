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
package org.objectweb.fractal.fscript.procedures;

import java.util.Set;

/**
 * A native library contains a set of {@link NativeProcedure}s with unique names.
 * 
 * @author Pierre-Charles David
 */
public interface NativeLibrary {
    /**
     * Returns the names of all the native procedures contained in this library.
     * 
     * @return the set of names of all the native procedures contained in this library.
     */
    Set<String> getAvailableProcedures();

    /**
     * Finds a procedure from this library given its name.
     * 
     * @param name
     *            the name of the procedure to find.
     * @return the procedure from this library named <code>name</code>, or
     *         <code>null</code> if no procedure of this name is available.
     */
    NativeProcedure getNativeProcedure(String name);
}