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
package org.objectweb.fractal.fscript;

/**
 * Represents the possible severities of a {@link Diagnostic}. The natural ordering is
 * from the least severe to the most severe.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public enum Severity {
    /**
     * Used to report additional information when checking or executing a script.
     */
    INFORMATION,
    /**
     * Indicates that a potential error or dangerous construct has been detected which,
     * while not strictly incorrect, is probably a mistake or could lead to confusion.
     */
    WARNING,
    /**
     * Indicates that a script is invalid and can not be processed (checked or executed)
     * further.
     */
    ERROR
}
