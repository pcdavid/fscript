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

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.util.Fractal;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class UnbindReconfiguration extends AbstractReconfiguration {
    private Component client;

    private String clientItfName;

    public UnbindReconfiguration(Component client, String clientItfName) {
        assert (client != null);
        assert (clientItfName != null && clientItfName.length() > 0);
        this.client = client;
        this.clientItfName = clientItfName;
    }

    @Override
    protected Object apply(Transaction tx) throws NoSuchInterfaceException,
            IllegalBindingException, IllegalLifeCycleException {
        BindingController bc = Fractal.getBindingController(client);
        Interface prevItf = (Interface) bc.lookupFc(clientItfName);
        if (prevItf != null) {
            tx.ensureIsStopped(client);
            bc.unbindFc(clientItfName);
            tx.registerUndo(new BindReconfiguration(client, clientItfName, prevItf));
        }
        return null;
    }
}
