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
package org.objectweb.fractal.fscript.reconfiguration;

/**
 * Represents an atomic reconfiguration which can be applied to a Fractal configuration.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public interface Reconfiguration {
    /**
     * Executes the action. The execution of this method must be atomic: either it
     * succeeds completely and returns a value (possibly <code>null</code>), or it
     * fails and throws an {@link ReconfigurationFailedException}. It that case, it is
     * the responsibility of the action to ensure that the target application is returned
     * to the initial state, before that action was attempted (atomicity).
     * 
     * @return a value, possibly <code>null</code>
     * @throws ReconfigurationFailedException
     *             if an error occured during the execution.
     */
    Object apply() throws ReconfigurationFailedException;
}
