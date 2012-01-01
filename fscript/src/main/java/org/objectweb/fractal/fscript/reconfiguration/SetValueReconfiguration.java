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

import javax.naming.OperationNotSupportedException;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.AttributesHelper;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class SetValueReconfiguration extends AbstractReconfiguration {
    private Component target;

    private String attributeName;

    private Object attributeValue;

    public SetValueReconfiguration(Component target, String attributeName,
            Object attributeValue) {
        assert (target != null);
        assert (attributeName != null && attributeName.length() > 0);
        this.target = target;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    @Override
    protected Object apply(Transaction tx) throws IllegalArgumentException,
            NoSuchInterfaceException, OperationNotSupportedException {
        AttributesHelper attr = new AttributesHelper(target);
        if (!attr.hasAttribute(attributeName)) {
            throw new ReconfigurationFailedException(
                    "Target component does not have an attribute named '" + attributeName
                            + "'.");
        }
        Object oldValue = attr.getAttribute(attributeName);
        attr.setAttribute(attributeName, attributeValue);
        tx.registerUndo(new SetValueReconfiguration(target, attributeName, oldValue));
        return null;
    }
}
