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

import java.util.ArrayList;
import java.util.List;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.util.Fractal;

/**
 * Used to manage atomic reconfiguration transactions.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class Transaction {
    /**
     * The component which had to be stopped to execute the requested reconfigurations but
     * should be restarted once the transaction is finished.
     */
    private List<Component> stoppedComponents;

    /**
     * The list of reverse actions (undos).
     */
    private List<Reconfiguration> journal;
    
    /**
     * A flag indicating whether or not to honor the #ensureIsStopped() requests.
     */
    private boolean honorImplicitStopRequests = false;
    
    /**
     * Starts the transaction.
     */
    public void start(boolean honorImplicitStopRequests) {
        this.stoppedComponents = new ArrayList<Component>();
        this.journal = new ArrayList<Reconfiguration>();
        this.honorImplicitStopRequests = honorImplicitStopRequests;
    }
    
    public void start() {
        start(false);
    }

    /**
     * Tries to commit the reconfiguration.
     */
    public boolean commit() {
        if (isValidReconfiguration()) {
            restartComponents();
            return true;
        } else {
            rollback();
            return false;
        }
    }

    /**
     * Cancels the reconfiguration and reverts to the initial state of the application.
     */
    public void cancel() {
        rollback();
        stoppedComponents.clear();
    }

    private void rollback() {
        for (int i = journal.size() - 1; i >= 0; i--) {
            Reconfiguration undo = journal.get(i);
            undo.apply();
        }
        restartComponents();
        journal.clear();
    }

    public boolean isValidReconfiguration() {
        // TODO check that stoppedComponents and their children are ready to be
        // restarted.
        // "descendant-or-self::*[interface::*[client()][required()][not(bound())]]"
        return true;
    }

    /**
     * Ensures a component is stopped before an action can be applied. Unless the
     * component is later explicitely <code>stop()</code>-ed by the FScript program, it
     * will be restarted automatically at the end of the reconfiguration.
     * 
     * @param comp
     */
    public void ensureIsStopped(Component comp) throws ReconfigurationFailedException {
        if (!honorImplicitStopRequests) {
            return;
        }
        try {
            LifeCycleController lc = Fractal.getLifeCycleController(comp);
            if (lc.getFcState() != LifeCycleController.STOPPED) {
                try {
                    lc.stopFc();
                    stoppedComponents.add(comp);
                } catch (IllegalLifeCycleException e1) {
                    throw new ReconfigurationFailedException(e1);
                }
            }
        } catch (NoSuchInterfaceException e) {
            // ignore
        }
    }

    private void restartComponents() {
        for (Object element : stoppedComponents) {
            Component comp = (Component) element;
            try {
                Fractal.getLifeCycleController(comp).startFc();
            } catch (IllegalLifeCycleException e) {
                throw new RuntimeException(e);
            } catch (NoSuchInterfaceException e) {
                throw new RuntimeException("Should not happen.");
            }
        }
    }

    public void registerUndo(Reconfiguration action) {
        journal.add(action);
    }
}
