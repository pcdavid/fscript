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

import java.util.Set;

import org.objectweb.fractal.fscript.nodes.Node;

/**
 * Utility class with methods to deal with FScript values.
 * 
 * @author pcdavid
 */
public class Values {
    private Values() {
    }

    /**
     * Gets a single <code>FractalNode</code> out of an FScript value:
     * <ul>
     * <li>If <code>arg</code> is a <code>FractalNode</code>, returns it.</li>
     * <li>If <code>arg</code> is a non-empty nodeset (i.e. a
     * <code>FractalNode[]</code>), returns one element of the set.</li>
     * <li>Otherwise, returns <code>null</code>.</li>
     * </ul>
     * 
     * @param arg
     *            an FScript value
     * @return a single <code>FractalNode</code> from <code>arg</code>
     */
    @SuppressWarnings("unchecked")
    public static Node getSingleNode(Object arg) {
        if (arg instanceof Node) {
            return (Node) arg;
        } else if (arg instanceof Node[]) {
            Node[] nodes = (Node[]) arg;
            if (nodes.length > 0) {
                return (nodes)[0];
            } else {
                return null;
            }
        } else if (arg instanceof Node[]) {
            return null;
        } else if (arg instanceof Set){
            if (!((Set) arg).isEmpty()) {
                return ((Set<Node>) arg).iterator().next();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Interprets an FScript value as a boolean.
     * 
     * @param object
     * @return
     */
    public static boolean asBoolean(Object object) {
        if (object instanceof Boolean) {
            return ((Boolean) object).booleanValue();
        } else if (object instanceof Object[]) {
            return ((Object[]) object).length > 0;
        } else if (object instanceof String) {
            return ((String) object).length() > 0;
        } else if (object instanceof Number) {
            return ((Number) object).doubleValue() != 0.0;
        } else if (object instanceof Set){
            return !((Set) object).isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Interprets an FScript value as a number.
     * 
     * @param object
     * @return
     */
    public static double asNumber(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        } else if (object instanceof String) {
            try {
                return Double.parseDouble((String) object);
            } catch (NumberFormatException e) {
                return Double.NaN;
            }
        } else {
            return Double.NaN;
        }
    }

    /**
     * Interprets an FScript value as a string.
     * 
     * @param object
     * @return
     */
    public static String asString(Object object) {
        if (object != null) {
            return object.toString();
        } else {
            return "";
        }
    }

}
