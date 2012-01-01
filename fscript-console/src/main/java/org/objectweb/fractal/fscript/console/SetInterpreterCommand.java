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

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.util.ComponentHelper;

/**
 * This command changes the underlying FScript implementation (a Fractal component).
 * 
 * @author Pierre-Charles David
 */
public class SetInterpreterCommand extends AbstractCommand {
    public void execute(String args) throws Exception {
        if (args.length() == 0) {
            showError("Missing value for the new interpreter.");
            showMessage("Syntax: " + this + " $anFScriptEngine");
            return;
        }

        Object value = engine.execute(args);
        Component newEngine = ((ComponentNode) FScript.getSingleNode(value)).getComponent();
        if (newEngine == null) {
            showError("Invalid FScript interpreter value.");
            showResult(value);
            return;
        }

        ensureComponentIsStarted(newEngine);
        session.setSessionInterpreter(newEngine);
        showMessage("New interpreter set to " + ComponentHelper.toString(newEngine));
    }
}
