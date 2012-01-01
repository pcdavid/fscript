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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.FScript;

/**
 * Main class to launch the FScript console.
 * 
 * @author Pierre-Charles David
 */
public class Main {
    /**
     * The default 'fractal.provider' to use if none is set.
     */
    private static final String DEFAULT_PROVIDER = "org.objectweb.fractal.julia.Julia";

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            configureFractal(null);
        } else if (args.length == 1) {
            configureFractal(args[0]);
        } else {
            System.err.println("Usage: java " + Main.class.getName() + " [file.properties]");
            System.exit(1);
        }
        Component fscript = FScript.newEngine();
        new TextConsole(fscript).run();
        System.exit(0);
    }

    private static void configureFractal(String propsFile) {
        Properties config = getConfigurationProperties(propsFile);
        for (Object element : config.keySet()) {
            String propName = (String) element;
            if (System.getProperty(propName) == null) {
                System.setProperty(propName, config.getProperty(propName));
            }
        }
    }

    private static Properties getConfigurationProperties(String propsFile) {
        Properties config = new Properties();
        config.setProperty("fractal.provider", DEFAULT_PROVIDER);
        if (propsFile != null) {
            try {
                config.load(new FileInputStream(propsFile));
            } catch (FileNotFoundException e) {
                System.err.println("Configuration file " + propsFile + " not found.");
                System.err.println("Reverting to default configuration.");
            } catch (IOException e) {
                System.err.println("Error while reading configuration file " + propsFile + ".");
                System.err.println("Reverting to default configuration.");
            }
        }
        return config;
    }
}
