/*
 * Copyright (c) 2008 ARMINES
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

import java.util.HashSet;
import java.util.Set;

public class ComposedAxis extends AbstractAxis {
    private final Axis[] components;

    public ComposedAxis(String name, Axis... bases) {
        super(bases[0].getModel(), name, bases[0].getInputNodeType(), bases[bases.length - 1]
                .getOutputNodeType());
        this.components = new Axis[bases.length];
        System.arraycopy(bases, 0, this.components, 0, bases.length);
        checkSameModel();
        checkTyping();
    }

    public boolean isPrimitive() {
        return false;
    }

    public boolean isModifiable() {
        return false;
    }

    private void checkSameModel() {
        for (Axis axis : components) {
            if (axis.getModel() != model) {
                throw new IllegalArgumentException("Inconstent model used for base axes.");
            }
        }
    }

    private void checkTyping() {
        for (int i = 1; i < components.length; i++) {
            Axis src = components[i - 1];
            Axis dest = components[i];
            if (!src.getOutputNodeType().equals(dest.getInputNodeType())) {
                throw new IllegalArgumentException("Axes " + src + " and " + dest
                        + " can not be composed.");
            }
        }
    }

    public Set<Node> selectFrom(Node source) {
        Set<Node> result = components[0].selectFrom(source);
        for (int i = 1; i < components.length; i++) {
            Set<Node> nextResult = new HashSet<Node>();
            for (Node src : result) {
                nextResult.addAll(components[i].selectFrom(src));
            }
            result = nextResult;
        }
        return result;
    }
}
