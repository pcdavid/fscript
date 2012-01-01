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
 * Represents a Fractal component. Note that in FPath, as opposed to "raw" Fractal,
 * components are reified. This means that a <code>ComponentNode</code> is different
 * from an <code>InterfaceNode</code> representing a <code>Component</code> interface.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public interface ComponentNode extends Node {
    /**
     * Returns the name of the component. If the component has a
     * <code>name-controller</code> interface, then its name is the result of the
     * <code>getFcName()</code> method. Otherwise, it is the empty string.
     * 
     * @return the name of the component.
     */
    String getName();

    /**
     * Returns the state of the component. If the component has a
     * <code>lifecycle-controller</code> interface, the its state is the result of the
     * <code>getFcState()</code>. Otherwise it is <code>STOPPED</code>. The
     * rationale is that if the component does not expose an explicit lifecycle, it means
     * it does not impose lifecycle-related constraints on its reconfigurations.
     * Considering it is <code>stopped</code> means all reconfiguration operations are
     * possible and so corresponds to this interpretation.
     * 
     * @return the current state of the component.
     */
    String getState();
}
