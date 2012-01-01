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
package org.objectweb.fractal.fscript.axes;

import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.nodes.AttributeNodeImpl;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;

/**
 * From a <code>ComponentNode</code>, select all the <code>AttributeNode</code>s of
 * this component. If the initial node is not a
 * {@link org.objectweb.fractal.fscript.nodes.ComponentNode}, it is automatically coerced
 * to the corresponding component node.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class AttributesAxis extends AbstractAxis<ComponentNodeImpl, AttributeNodeImpl> {
    public AttributesAxis(DefaultNodeFactory factory) {
        super(factory);
    }

    public String getName() {
        return "attribute";
    }

    public Set<AttributeNodeImpl> selectFrom(ComponentNodeImpl node) {
        Set<AttributeNodeImpl> nodes = createNodeSet();
        Component component = node.getComponent();
        for (String name : node.getAttributesHelper().getAttributesNames()) {
                nodes.add(createAttributeNode(component, name));
        }
        return nodes;
    }
}
