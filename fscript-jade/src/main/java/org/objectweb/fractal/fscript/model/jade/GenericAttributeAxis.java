/*
 * Copyright (c) 2007-208 ARMINES
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

import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.AttributesHelper;
import org.objectweb.fractal.fscript.model.AbstractAxis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.ow2.jasmine.jade.fractal.api.control.GenericAttributeController;

public class GenericAttributeAxis extends AbstractAxis {
    public GenericAttributeAxis(JadeModel model) {
        super(model, "attribute", "component", "attribute");
    }

    public boolean isPrimitive() {
        return true;
    }

    public boolean isModifiable() {
        return false;
    }

    public Set<Node> selectFrom(Node source) {
        Component comp = ((ComponentNode) source).getComponent();
        AttributesHelper attrHelper = new AttributesHelper(comp);
        Set<Node> result = new HashSet<Node>();
        for (String attrName : getAttributesNames(attrHelper)) {
            // Here we explicitly do not use the JadeNodeFactory interface in order to
            // reuse the AttributeHelper, as these can be costly to create.
            Node node = new GenericAttributeNode((JadeModel) model, attrHelper, attrName);
            result.add(node);
        }
        return result;
    }

    private String[] getAttributesNames(AttributesHelper attrHelper) {
        try {
            Component c = attrHelper.getComponent();
            GenericAttributeController gac = (GenericAttributeController) c
                    .getFcInterface("generic-attribute-controller");
            String[] attrs = gac.listFcAtt();
            return (attrs != null) ? attrs : new String[0];
        } catch (NoSuchInterfaceException e) {
            // No GAC, fall back on standard attribute-controller
            return attrHelper.getAttributesNames().toArray(new String[0]);
        }
    }
}
