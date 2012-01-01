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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.model.jade;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.AttributesHelper;
import org.objectweb.fractal.fscript.model.fractal.AttributeNode;
import org.ow2.jasmine.jade.fractal.api.control.GenericAttributeController;
import org.ow2.jasmine.jade.fractal.api.control.NoSuchAttributeException;

public class GenericAttributeNode extends AttributeNode {
    private final GenericAttributeController gac;

    public GenericAttributeNode(JadeModel model, AttributesHelper attrHelper,
            String attrName) {
        super(model, attrHelper, attrName);
        this.gac = getGAC(attrHelper.getComponent());
    }

    private GenericAttributeController getGAC(Component c) {
        try {
            return (GenericAttributeController) c
                    .getFcInterface("generic-attribute-controller");
        } catch (NoSuchInterfaceException e) {
            return null;
        }
    }

    public String getType() {
        if (gac != null) {
            return String.class.getName();
        } else {
            return super.getType();
        }
    }

    public Object getValue() {
        if (gac != null) {
            try {
                return gac.getAttribute(getName());
            } catch (NoSuchAttributeException e) {
                throw new IllegalArgumentException();
            }
        } else {
            return super.getValue();
        }
    }
    
    @Override
    public void setValue(Object value) {
        if (gac != null) {
            try {
                gac.setAttribute(getName(), (String) value);
            } catch (NoSuchAttributeException e) {
                throw new IllegalArgumentException();
            }
        } else {
            super.setValue(value);
        }
    }
    
    @Override
    public boolean isReadable() {
        return (gac != null) ? true : super.isReadable();
    }
    
    @Override
    public boolean isWritable() {
        return (gac != null) ? true : super.isWritable();
    }
}
