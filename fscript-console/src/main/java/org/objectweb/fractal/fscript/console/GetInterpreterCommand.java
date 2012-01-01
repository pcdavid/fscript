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

import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.fscript.model.fractal.NodeFactory;

/**
 * This command makes the underlying FScript implementation (a Fractal component) available to the
 * console user through a variable.
 * 
 * @author Pierre-Charles David
 */
public class GetInterpreterCommand extends AbstractCommand {
    public void execute(String varName) throws Exception {
        if (!varName.matches("^[-_\\p{Alpha}][-_\\p{Alnum}]*")) {
            showError("Invalid variable name.");
            return;
        }
        NodeFactory dnf = getEngineInterface("node-factory", NodeFactory.class);
        ComponentNode node = dnf.createComponentNode(fscript);
        engine.setGlobalVariable(varName, node);
        showResult(node);
    }
}
