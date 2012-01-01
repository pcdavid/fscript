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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

/**
 * This command prints a summary of useful Fractal configuration parameters in effect. The current
 * implementation is limited to system properties used by Fractal and Julia.
 * 
 * @author Pierre-Charles David
 */
public class ConfigCommand extends AbstractCommand {
    private final static String JULIA_PROVIDER = "org.objectweb.fractal.julia.Julia";

    /**
     * Relevant system properties for each supported provider.
     */
    private final static Multimap<String, String> PROPERTIES = Multimaps.newArrayListMultimap();

    static {
        PROPERTIES.put(JULIA_PROVIDER, "fractal.provider");
        PROPERTIES.put(JULIA_PROVIDER, "julia.loader");
        PROPERTIES.put(JULIA_PROVIDER, "julia.config");
    }

    public void execute(String args) throws Exception {
        showTitle("Current configuration parameters");
        String provider = System.getProperty("fractal.provider");
        if (provider == null) {
            showWarning("System property 'fractal.provider' not set.");
            showWarning("Fractal not correctly initialized.");
            return;
        }
        Collection<String> properties = PROPERTIES.get(provider);
        String[][] table = new String[properties.size() + 1][2];
        // Table headers
        table[0][0] = "Property";
        table[0][1] = "Value";
        // Show values if available
        if (PROPERTIES.containsKey(provider)) {
            int i = 1;
            List<String> sortedNames = Lists.newArrayList(properties);
            Collections.sort(sortedNames);
            for (String property : sortedNames) {
                table[i][0] = property;
                table[i][1] = System.getProperty(property);
                i += 1;
            }
            showTable(table);
        } else {
            table[1][0] = "fractal.provider";
            table[1][1] = provider;
            showTable(table);
            newline();
            showWarning("This provider is not supported by this command.");
            showWarning("No additional information available.");
        }
    }
}
