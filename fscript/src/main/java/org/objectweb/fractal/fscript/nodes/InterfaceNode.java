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

import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.type.InterfaceType;

/**
 * Represents a Fractal component interface. FPath expects interfaces to use the default
 * type system, i.e. their type must be (a subtype of) {@link InterfaceType}.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public interface InterfaceNode extends Node {
    /**
     * Returns the name of the interface as defined by the
     * {@link Interface#getFcItfName()} method.
     */
    String getName();

    /**
     * Tests whether this interface is internal or external. This corresponds to the
     * {@link Interface#isFcInternalItf()} method.
     * 
     * @return <code>true</code> if this interface is internal, <code>false</code>
     *         otherwise.
     */
    boolean isInternal();

    /**
     * Returns the signature of this interface, i.e. the fully qualified name of the Java
     * interface type implemented by this component interface. This corresponds to the
     * {@link InterfaceType#getFcItfSignature()} method.
     * 
     * @return the signature of this interface.
     */
    String getSignature();

    /**
     * Tests the cardinality of this interface: returns <code>true</code> if it is a
     * <em>collection</em> interface, <code>false</code> if it is a <em>single</em>
     * interface. This corresponds to the {@link InterfaceType#isFcCollectionItf()}
     * method.
     * 
     * @return <code>true</code> iff this interface is a collection interface.
     */
    boolean isCollection();

    /**
     * Tests the role of this interface: returns <code>true</code> if it is a client
     * interface, <code>false</code> if it is a server interface. This corresponds to
     * the {@link InterfaceType#isFcClientItf()} method.
     * 
     * @return <code>true</code> iff this is a client interface.
     */
    boolean isClient();

    /**
     * Tests the contingency of this interface: returns <code>true</code> if it is an
     * optional interface, <code>false</code> if it is a mandatory interface. This
     * corresponds to the {@link InterfaceType#isFcOptionalItf()} method.
     * 
     * @return <code>true</code> iff this is an optional interface.
     */
    boolean isOptional();
}
