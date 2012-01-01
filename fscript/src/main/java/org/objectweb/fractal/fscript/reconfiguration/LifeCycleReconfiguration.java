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
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.util.Fractal;

/**
 * @author Pierre-Charles David <pierrecharles.david@rd.francetelecom.com>
 */
public class LifeCycleReconfiguration extends AbstractReconfiguration {
    private Component target;

    private boolean start;

    public LifeCycleReconfiguration(Component target, boolean start) {
        assert (target != null);
        this.target = target;
        this.start = start;
    }

    @Override
    protected Object apply(Transaction tx) throws NoSuchInterfaceException,
            IllegalLifeCycleException {
        LifeCycleController lc = Fractal.getLifeCycleController(target);
        boolean running = (lc.getFcState() == LifeCycleController.STARTED);
        if (running != start) {
            if (start) {
                lc.startFc();
                tx.registerUndo(new StopReconfiguration(target));
            } else {
                lc.stopFc();
                tx.registerUndo(new StartReconfiguration(target));
            }
        }
        return null;
    }
}
