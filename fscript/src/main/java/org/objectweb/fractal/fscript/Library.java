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
package org.objectweb.fractal.fscript;

import java.util.NoSuchElementException;
import java.util.Set;

import org.objectweb.fractal.fscript.ast.UserProcedure;
import org.objectweb.fractal.fscript.procedures.Procedure;

/**
 * A collection of primitive or user-defined procedures. A library can contain both native
 * and user-defined procedures. Only user-defined procedures can be explicitly added or
 * removed. Procedures names are unique among natives and user-defined in a given library.
 * 
 * @author Pierre-Charles David
 */
public interface Library {
    /**
     * Finds a procedure by name in this library.
     * 
     * @param name
     *            the name of the procedure to look for.
     * @return the procedure of the given name from this library, or <code>null</code>
     *         if none matches.
     */
    Procedure lookup(String name);

    /**
     * Returns the names of all the procedures in this library.
     * 
     * @return the names of all the procedures in this library.
     */
    Set<String> getProceduresNames();

    /**
     * Adds a new user-defined procedure to this library. If a user-defined procedure of
     * the same name already exists, it is replaced.
     * 
     * @param proc
     *            the procedure to add to this library
     * @throws IllegalArgumentException
     *             if a native procedure of the same name as <code>proc</code> is
     *             already in this library.
     */
    void define(UserProcedure proc) throws IllegalArgumentException;

    /**
     * Removes a user-defined procedure from this library.
     * 
     * @param procName
     *            the name of the procedure to remove.
     * @throws NoSuchElementException
     *             if no procedure of the given name exists in this library/
     * @throws IllegalArgumentException
     *             if the given name references a native procedure (those can not be
     *             undefined).
     */
    void undefine(String procName) throws NoSuchElementException, IllegalArgumentException;
}
