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
package org.objectweb.fractal.fscript.nodes;

/**
 * Represents a configuration attribute of a Fractal component.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public interface AttributeNode extends Node {
    /**
     * Returns the name of the attribute. This corresponds to the name of method or method
     * pair implementing the attribute, without the prefix (<code>set/get/is</code>)
     * and starting with a lowercase letter (e.g. the name of the attribute associated to
     * methods <code>getFoo()</code> and <code>setFoo()</code> is <code>"foo"</code>).
     * 
     * @return the name of the attribute.
     */
    String getName();
    
    /**
     * Returns the type of the attribute, as a Java class.
     * 
     * @return the type of the attribute.
     */
    Class<?> getType();

    /**
     * Returns the current value of the attribute.
     * 
     * @return the current value of the attribute.
     */
    Object getValue();
}
