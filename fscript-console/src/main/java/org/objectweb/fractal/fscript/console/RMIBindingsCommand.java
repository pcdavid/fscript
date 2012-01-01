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
import org.objectweb.fractal.rmi.registry.NamingService;
import org.objectweb.fractal.rmi.stub.Stub;
import org.objectweb.fractal.util.ComponentHelper;

/**
 * This command lists the currently visible Fractal RMI bindings.
 * 
 * @author Pierre-Charles David
 */
public class RMIBindingsCommand extends AbstractCommand {
    public void execute(String args) throws Exception {
        NamingService ns = (NamingService) engine.getGlobalVariable("*rmiregistry*");
        String uri = (String) engine.getGlobalVariable("*rmiuri*");
        if (ns == null) {
            showWarning("The interpreter is not connected to a Fractal RMI registry.");
            return;
        }
        if (uri == null) {
            showWarning("Internal inconsistency: *rmiregistry* is set but not *rmiuri*.");
        }

        showTitle("Active Fractal RMI bindings (" + uri + ")");
        showTable(createBindingsTable(ns));
    }

    private String[][] createBindingsTable(NamingService ns) {
        String[] bindings = ns.list();
        String[][] table = new String[bindings.length + 1][4];
        // Table headers
        table[0][0] = "Binding name";
        table[0][1] = "Component name";
        table[0][2] = "Location";
        table[0][3] = "Identifier";
        // Table cells
        for (int i = 0; i < bindings.length; i++) {
            String bindingName = bindings[i];
            Component value = ns.lookup(bindingName);
            String id = "n/a";
            if (value instanceof Stub) {
                id = ((Stub) value).getIdentifiers()[0].toString();
            }
            table[i + 1][0] = bindingName;
            table[i + 1][1] = ComponentHelper.toString(value);
            table[i + 1][2] = (value instanceof Stub) ? "Remote" : "Local";
            table[i + 1][3] = id;
        }
        return table;
    }
}
