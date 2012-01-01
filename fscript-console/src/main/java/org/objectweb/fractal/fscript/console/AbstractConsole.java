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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.FScriptEngine;
import org.objectweb.fractal.fscript.diagnostics.DiagnosticListener;
import org.objectweb.fractal.fscript.model.Axis;
import org.objectweb.fractal.fscript.model.Model;
import org.objectweb.fractal.util.Fractal;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Comparators;
import com.google.common.collect.Lists;

public abstract class AbstractConsole implements Session {
    /**
     * The current FScript implementation to use to execute user input.
     */
    protected Component fscript;

    /**
     * The console's special commands, by name.
     */
    protected final Map<String, Command> commands;

    public AbstractConsole(Component fscript) {
        Preconditions.checkNotNull(fscript);
        this.fscript = fscript;
        this.commands = new HashMap<String, Command>();
        registerCommands();
    }

    private void registerCommands() {
        // Internal commands
        register(new HelpCommand());

        Properties commandsConfig = new Properties();
        try {
            String resourceName = "commands.properties";
            InputStream commandsStream = getClass().getResourceAsStream(resourceName);
            if (commandsStream == null) {
                showError("Could not find configuration file " + resourceName + ".");
                throw new RuntimeException("Could not find configuration file "
                        + resourceName + ".");
            }
            commandsConfig.load(commandsStream);
        } catch (IOException e) {
            showError("Could not read commands configuration file.", e);
            throw new RuntimeException("Could not read commands configuration file.", e);
        }
        for (Object o : commandsConfig.keySet()) {
            String key = (String) o;
            if (key.endsWith(".class")) {
                String name = key.substring("command.".length(), key.lastIndexOf('.'));
                String klass = commandsConfig.getProperty(key);
                String shortDesc = commandsConfig.getProperty("command." + name
                        + ".shortDesc");
                String longDesc = commandsConfig.getProperty("command." + name
                        + ".longDesc");
                Command cmd = createCommand(name, klass, shortDesc, longDesc);
                if (cmd != null) {
                    register(cmd);
                }
            }
        }
    }

    private Command createCommand(String cmdName, String cmdClass, String cmdShortDesc,
            String cmdLongDesc) {
        try {
            Class<?> klass = Class.forName(cmdClass);
            Command cmd = (Command) klass.newInstance();
            cmd.setName(cmdName);
            cmd.setShortDescription(cmdShortDesc);
            cmd.setLongDescription(cmdLongDesc);
            return cmd;
        } catch (ClassNotFoundException e) {
            showWarning("Unable to load class " + cmdClass + ".");
            showWarning("Command :" + cmdName + " will not be available.");
            return null;
        } catch (Exception e) {
            showWarning("Unable to instanciate and configure class " + cmdClass + ".");
            showWarning("Command :" + cmdName + " will not be available.");
            return null;
        }
    }

    protected void register(Command cmd) {
        cmd.setSession(this);
        cmd.setFScriptEngine(fscript);
        this.commands.put(cmd.getName(), cmd);
    }

    private class HelpCommand extends AbstractCommand {
        @Override
        public String getName() {
            return "help";
        }

        @Override
        public String getShortDescription() {
            return "Shows a list of available commands.";
        }

        @Override
        public String getLongDescription() {
            return super.getShortDescription();
        }

        public void execute(String args) throws Exception {
            if (args.length() == 0) {
                listAvailableCommands();
            } else {
                String name = args.startsWith(":") ? args.substring(1) : args;
                Command cmd = commands.get(name);
                if (cmd != null) {
                    showMessage(cmd.getLongDescription());
                } else {
                    showError("No such command: " + name);
                }
            }
        }

        private void listAvailableCommands() {
            showTitle("Available commands");
            showMessage("Type ':help <cmd>' for more details on a specific command.");
            newline();

            List<Command> sortedCmds = Lists.newArrayList(commands.values());
            Collections.sort(sortedCmds, Comparators
                    .fromFunction(new Function<Command, String>() {
                        public String apply(Command cmd) {
                            return cmd.getName();
                        }
                    }));

            String[][] table = new String[sortedCmds.size() + 1][2];
            table[0][0] = "Command";
            table[0][1] = "Description";
            int i = 0;
            for (Command command : sortedCmds) {
                table[i + 1][0] = command.toString();
                table[i + 1][1] = command.getShortDescription();
                i += 1;
            }
            showTable(table);
        }
    }

    protected class Request {
        private final Command command;
        private final String arguments;

        public Request(String commandName, String arguments) {
            this.command = commands.get(commandName);
            this.arguments = arguments;
        }

        public void execute() throws Exception {
            if (command != null) {
                command.execute(arguments);
            } else {
                showError("Invalid request.");
            }
        }
    }

    protected Request parseRequest(String line) {
        if (line == null) {
            return new Request("quit", "");
        } else if (line.startsWith(":")) {
            int i = line.indexOf(' ');
            if (i != -1) {
                return new Request(line.substring(1, i), line.substring(i + 1));
            } else {
                return new Request(line.substring(1), "");
            }
        } else if (line.endsWith(";") || line.contains("{")) {
            return new Request("exec", line);
        } else {
            return new Request("eval", line);
        }
    }

    public void setFScriptEngine(Component newEngine) {
        this.fscript = newEngine;
        for (Command cmd : commands.values()) {
            cmd.setFScriptEngine(this.fscript);
        }
    }

    public void setSessionInterpreter(Component fscript) {
        for (Command cmd : commands.values()) {
            cmd.setFScriptEngine(fscript);
        }
    }

    public DiagnosticListener getDiagnosticListener() {
        return new SessionDiagnosticListener(this);
    }

    public void processRequest(String line) throws Exception {
        if (line != null && line.length() == 0) {
            return;
        }
        Request request = parseRequest(line);
        if (request != null) {
            request.execute();
        }
    }

    public Set<String> getGlobalVariablesNames() {
        FScriptEngine engine = FScript.getFScriptEngine(fscript);
        return engine.getGlobals();
    }

    public Set<String> getAxesNames() {
        Model model = getModel();
        if (model != null) {
            Set<Axis> axes = model.getAxes();
            Set<String> names = new HashSet<String>();
            for (Axis axis : axes) {
                names.add(axis.getName());
            }
            return names;
        } else {
            return Collections.emptySet();
        }
    }

    private Model getModel() {
        try {
            for (Component c : Fractal.getContentController(fscript).getFcSubComponents()) {
                String name = Fractal.getNameController(c).getFcName();
                if ("model".equals(name)) {
                    return (Model) c.getFcInterface("model");
                }
            }
            return null;
        } catch (Exception e) {
            showWarning("Incompatible FScript implementation.");
            showWarning("Axis name completion disabled.");
            return null;
        }
    }

    public Set<String> getCommandNames() {
        return commands.keySet();
    }
}
