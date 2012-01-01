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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.fractal.rmi.registry.NamingService;
import org.objectweb.fractal.rmi.registry.Registry;

/**
 * Connects the underlying FScript implementation to a Fractal RMI Registry.
 * 
 * @author Pierre-Charles David
 */
public class RMIConnectCommand extends AbstractCommand {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = Registry.DEFAULT_PORT;
    private static final Pattern CONNECTION_PATTERN = Pattern.compile("^([^:]+)(:[0-9]+)?");

    public void execute(String args) throws Exception {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        if (args.length() > 0) {
            Matcher m = CONNECTION_PATTERN.matcher(args);
            if (!m.matches()) {
                showError("Invalid argument for " + this + ": " + args);
                showMessage("Syntax: " + this + " hostname[:port]");
                return;
            }
            host = m.group(1);
            if (m.group(2) != null) {
                try {
                    port = Integer.parseInt(m.group(2).substring(1));
                } catch (NumberFormatException nfe) {
                    showError("Invalid port number. Not an integer.");
                    host = DEFAULT_HOST;
                    return;
                }
            }
        }

        showMessage("Attempting connection to frmi://" + host + ":" + port);
        NamingService namingService = null;
        try {
            namingService = Registry.getRegistry(host, port);
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }

        try {
            namingService.list();
            showMessage("Connection established.");
        } catch (Exception e) {
            showWarning("Unable to validate the connection.");
        }
        engine.setGlobalVariable("*rmiregistry*", namingService);
        engine.setGlobalVariable("*rmiuri*", "frmi://" + host + ":" + port);
    }
}
