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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.fractal.fscript.FScriptInterpreter;

public class ConfigCommand extends AbstractCommand {
	private final static String JULIA_PROVIDER = "org.objectweb.fractal.julia.Julia";

	private final static Map<String, List<String>> PROVIDER_PROPERTIES = new HashMap<String, List<String>>();

	static {
		List<String> properties = Arrays.asList("julia.loader", "julia.config");
		PROVIDER_PROPERTIES.put(JULIA_PROVIDER, properties);
		// TODO Add AOKell properties
	}

	public ConfigCommand(Console console, FScriptInterpreter fscript) {
		super(console, fscript);
	}

	public String getName() {
		return "config";
	}

	public String getDescription() {
		return "Shows the Fractal configuration parameters in effect.";
	}

	public void execute(String args) throws Exception {
		console.printMessage("Current configuration:");
		showProperty("fractal.provider", console);
		String provider = System.getProperty("fractal.provider");
		List<String> properties = PROVIDER_PROPERTIES.get(provider);
		if (properties != null) {
			for (String property : properties) {
				showProperty(property, console);
			}
		}
	}

	private void showProperty(String name, Console console) {
		console.printMessage(name + ": " + System.getProperty(name));
	}
}
