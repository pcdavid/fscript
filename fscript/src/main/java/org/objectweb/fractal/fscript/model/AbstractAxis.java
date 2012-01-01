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

import org.objectweb.fractal.fscript.types.Type;

public abstract class AbstractAxis implements Axis {
    protected final Model model;

    protected final String name;

    protected final Type inputType;

    protected final Type outputType;

    public AbstractAxis(Model model, String name, String inputKind, String outputKind) {
        this(model, name, model.getNodeKind(inputKind), model.getNodeKind(outputKind));
    }

    public AbstractAxis(Model model, String name, Type inputType, Type outputType) {
        this.model = model;
        this.name = name;
        this.inputType = inputType;
        this.outputType = outputType;
    }

    public Model getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public Type getInputNodeType() {
        return inputType;
    }

    public Type getOutputNodeType() {
        return outputType;
    }

    public void connect(Node source, Node dest) {
        throw new UnsupportedOperationException();
    }

    public void disconnect(Node source, Node dest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "Axis " + getName() + ": " + getInputNodeType() + " -> " + getOutputNodeType();
    }
}
