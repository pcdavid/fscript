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

import java.util.Arrays;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.control.IllegalContentException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.util.Fractal;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class RemoveReconfiguration extends AbstractReconfiguration {
    private Component composite;

    private Component subcomponent;

    public RemoveReconfiguration(Component composite, Component subcomponent) {
        assert (composite != null && subcomponent != null);
        this.composite = composite;
        this.subcomponent = subcomponent;
    }

    @Override
    protected Object apply(Transaction tx) throws IllegalContentException,
            IllegalLifeCycleException, NoSuchInterfaceException {
        ContentController cc = Fractal.getContentController(composite);
        Component[] children = cc.getFcSubComponents();
        if (Arrays.asList(children).contains(subcomponent)) {
            tx.ensureIsStopped(composite);
            cc.removeFcSubComponent(subcomponent);
            tx.registerUndo(new AddReconfiguration(composite, subcomponent));
        }
        return null;
    }
}
