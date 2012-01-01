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
package org.objectweb.fractal.fscript.console;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.FScriptInterpreter;
import org.objectweb.fractal.fscript.Values;
import org.objectweb.fractal.fscript.nodes.NodeImpl;
import org.objectweb.fractal.util.Fractal;

public class RunCommand extends AbstractCommand {
	public RunCommand(Console console, FScriptInterpreter fscript) {
		super(console, fscript);
	}

	public String getName() {
		return "run";
	}

	public String getDescription() {
		return "Launches a component using one of its 'Runnable' interfaces.";
	}

	public void execute(String args) throws Exception {
		int sep = args.indexOf(' ');
		final String itf;
		final String expr;
		if (sep != -1) {
			itf = args.substring(0, sep);
			expr = args.substring(sep + 1);
		} else {
			itf = "run"; // Default interface name.
			expr = args;
		}
		Object root = fscript.evaluate(expr, null);
		Component rootC =((NodeImpl) Values.getSingleNode(root)).getComponent();
		final Runnable run = (Runnable) rootC.getFcInterface(itf);
		console.printMessage("Launching interface " + itf + " of component "
				+ console.formattedValue(rootC) + ".");
		boolean started;
		try {
			started = "STARTED".equals(Fractal.getLifeCycleController(rootC)
					.getFcState());
		} catch (NoSuchInterfaceException e) {
			// Assume the component is in a runnable state.
			started = true;
		}
		if (!started) {
			console.printWarning("Component is not STARTED.");
		}
		Thread th = new Thread(run);
		th.setDaemon(true); // FIXME Support real 'job control', especially
							// shutdown.
		th.start();
	}
}
