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
 * Indicates that an error occured during the execution of a reconfiguration. The code
 * raising such an exception is responsible to make sure that the state of the target
 * application is returned to the initial state, before that reconfiguration was attempted
 * (atomicity).
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class ReconfigurationFailedException extends RuntimeException {
    private static final long serialVersionUID = 4050484525576434741L;

    public ReconfigurationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReconfigurationFailedException(String message) {
        super(message);
    }

    public ReconfigurationFailedException(Throwable cause) {
        super(cause);
    }
}
