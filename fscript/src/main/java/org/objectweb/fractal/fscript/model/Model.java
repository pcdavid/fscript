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
package org.objectweb.fractal.fscript.model;

import java.util.Set;

import org.objectweb.fractal.fscript.procedures.NativeLibrary;

/**
 * Represents a component model as a family of directed graphs and operations to modify
 * them. More precisely, a component model defines:
 * <ul>
 * <li>which kind of nodes can be in the graph. Each represent a kind of architectural
 * element reified in the model.</li>
 * <li>which kinds of relations (axes) can exist between these nodes. These represent the
 * relations used in the model to describe the structure of an architecture.</li>
 * <li>what operations are available to reconfigure the graph. Each operation corresponds
 * to a reconfiguration of the architecture allowed by the model.</li>
 * <li>a set of architectural constraints which restricts the possible forms of the
 * graph.</li>
 * </ul>
 * 
 * @author Pierre-Charles David
 */
public interface Model extends NativeLibrary {
    Set<NodeKind> getNodeKinds();

    NodeKind getNodeKind(String name);

    Set<Axis> getAxes();

    Axis getAxis(String name);
}
