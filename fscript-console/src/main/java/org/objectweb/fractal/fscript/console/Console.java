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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jline.ConsoleReader;
import jline.SimpleCompletor;

import org.objectweb.fractal.fscript.FScriptInterpreter;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class Console implements Runnable {
	private static final String DEFAULT_PROVIDER = "org.objectweb.fractal.julia.Julia";

	private static final String BANNER = "FScript console.\n"
			+ "Type ':help' for a list of available commands, ':quit' to exit.\n";

	private final FScriptInterpreter fscript;

	private final ConsoleReader console;

	private final Map<String, Command> commands;

	private volatile boolean finished;

	private String prompt;

	public Console(FScriptInterpreter fscript) throws IOException {
		this.fscript = fscript;
		this.console = new ConsoleReader();
		this.prompt = "FScript> ";
		this.commands = new HashMap<String, Command>();
		registerDefaultCommands();
	}

	private void registerDefaultCommands() {
		register(new HelpCommand(this, fscript));
		register(new QuitCommand(this, fscript));
		register(new ConfigCommand(this, fscript));
		register(new LoadCommand(this, fscript));
		register(new LoadURLCommand(this, fscript));
		register(new EvalCommand(this, fscript));
		register(new ExecCommand(this, fscript));
		register(new RunCommand(this, fscript));
		register(new DotCommand(this, fscript));
		register(new ConnectCommand(this, fscript));
		register(new ListCommand(this, fscript));

		String[] commandNames = commands.keySet().toArray(new String[0]);
		for (int i = 0; i < commandNames.length; i++) {
			commandNames[i] = ":" + commandNames[i];
		}
		console.addCompletor(new SimpleCompletor(commandNames));
	}

	private void register(Command cmd) {
		this.commands.put(cmd.getName(), cmd);
	}

	public void run() {
		printMessage(BANNER);
		finished = false;
		while (!finished) {
			try {
				String line = console.readLine(prompt);
				if (line.trim().length() == 0) {
					continue;
				}
				String[] request = parseRequest(line);
				Command cmd = commands.get(request[0]);
				if (cmd != null) {
					cmd.execute(request[1]);
				} else {
					printMessage("Command '" + request[0] + "' unknown.");
				}
			} catch (Exception e) {
				printError(e);
			}
		}
	}

	private String[] parseRequest(String line) {
		// req[0] => command name
		// req[1] => arguments
		String[] req = new String[2];
		if (line.startsWith(":")) {
			int i = line.indexOf(' ');
			if (i != -1) {
				req[0] = line.substring(1, i);
				req[1] = line.substring(i + 1);
			} else {
				// Plain command
				req[0] = line.substring(1);
				req[1] = "";
			}
		} else if (line.endsWith(";") || line.contains("{")) {
			req[0] = "exec";
			req[1] = line;
		} else {
			req[0] = "eval";
			req[1] = line;
		}
		return req;
	}

	public void printMessage(String msg) {
		print("", msg);
	}

	public void printResult(Object result) {
		print("=> ", formattedValue(result));
	}

	public void printWarning(String warn) {
		print("Warning: ", warn);
	}

	public void printError(Throwable err) {
		print("Error: ", err.getMessage());
		StringWriter sw = new StringWriter();
		err.printStackTrace(new PrintWriter(sw));
		print("Stack trace: ", sw.toString());
	}

	private void print(String prefix, String message) {
		try {
			console.printString(prefix + message);
			console.printNewline();
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	public String formattedValue(Object result) {
		if (result instanceof Object[]) {
			StringBuilder buf = new StringBuilder("{");
			boolean first = true;
			for (Object item : (Object[]) result) {
				if (!first) {
					buf.append(", ");
				} else {
					first = false;
				}
				buf.append(item);
			}
			return buf.append("}").toString();
		} else if (result != null) {
			return result.toString();
		} else {
			return null;
		}
	}

	private class QuitCommand extends AbstractCommand {
		public QuitCommand(Console console, FScriptInterpreter fscript) {
			super(console, fscript);
		}

		public String getName() {
			return "quit";
		}

		public String getDescription() {
			return "Exits the console.";
		}

		public void execute(String args) throws Exception {
			Console.this.finished = true;
		}
	}

	private class HelpCommand extends AbstractCommand {
		public HelpCommand(Console console, FScriptInterpreter fscript) {
			super(console, fscript);
		}

		public String getName() {
			return "help";
		}

		public String getDescription() {
			return "Shows a list of available commands.";
		}

		public void execute(String args) throws Exception {
			console.printMessage("Available commands:");

			List<String> names = new ArrayList<String>(commands.keySet());
			Collections.sort(names);

			// Computes the column where to align descriptions.
			int column = 0;
			for (String name : names) {
				if (name.length() > column) {
					column = name.length();
				}
			}
			column += 1; // Account fot the ':' prefix.
			column += 5; // Add some blank space.

			for (String name : names) {
				Command cmd = commands.get(name);
				StringBuilder sb = new StringBuilder(":");
				sb.append(cmd.getName());
				while (sb.length() < column) {
					sb.append(' ');
				}
				sb.append(cmd.getDescription());
				console.printMessage(sb.toString());
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// Setup default configuration
		Properties config = new Properties();
		config.setProperty("fractal.provider", DEFAULT_PROVIDER);
		if (System.getProperty("java.policy") != null) {
		    System.setSecurityManager(new SecurityManager());
		}
		// Enable external file to override them
		if (args.length > 0) {
			try {
				config.load(new FileInputStream(args[0]));
			} catch (FileNotFoundException e) {
				System.err.println("Configuration file " + args[0]
						+ " not found.");
				System.err.println("Reverting to default configuration.");
			} catch (IOException e) {
				System.err.println("Error while reading configuration file "
						+ args[0] + ".");
				System.err.println("Reverting to default configuration.");
			}
		}
		for (Object element : config.keySet()) {
			String propName = (String) element;
			// Only apply parameters which are not overridden on the JVM command
			// line
			if (System.getProperty(propName) == null) {
				System.setProperty(propName, config.getProperty(propName));
			}
		}
		// Start the console
		new Console(new FScriptInterpreter()).run();
	}
}
