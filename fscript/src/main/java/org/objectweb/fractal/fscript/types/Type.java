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
package org.objectweb.fractal.fscript.types;

/**
 * This interface is used to represent the type of a value in FPath and FScript.
 * 
 * @author Pierre-Charles David
 */
public interface Type {
    /**
     * Checks whether a given value is of this type.
     * 
     * @param value
     *            the value to test.
     * @return <code>true</code> iff <code>value</code> is of the type represented by
     *         the receiver.
     */
    boolean checkValue(Object value);
}
