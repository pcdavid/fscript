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
package org.objectweb.fractal.fscript.console;

import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.model.fractal.InterfaceNode;

/**
 * This command invokes a <code>Runnable</code> service interface from a component. in a separate
 * thread.
 * 
 * @author Pierre-Charles David
 */
public class RunCommand extends AbstractCommand {
    public void execute(String args) throws Exception {
        Object result = engine.execute(args);

        InterfaceNode itfNode = (InterfaceNode) FScript.getSingleNode(result);
        if (itfNode == null) {
            showError("Invalid expression value. Should return a Runnable interface node.");
            showResult(result);
            return;
        }

        Interface itf = itfNode.getInterface();
        if (!(itf instanceof Runnable)) {
            showError("This interface node is not a Runnable.");
            showResult(result);
            showMessage("Interface signature: " + itfNode.getSignature());
            return;
        }

        ensureComponentIsStarted(((InterfaceNode) itfNode).getInterface().getFcItfOwner());
        showMessage("Launching interface " + result + ".");
        Thread th = new Thread((Runnable) itf, "Service " + itfNode);
        th.setDaemon(true);
        th.start();
    }
}
