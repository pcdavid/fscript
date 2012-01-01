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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jline.Completor;
import jline.ConsoleReader;
import jline.MultiCompletor;
import jline.SimpleCompletor;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.model.Node;

/**
 * An FScript console ("shell") to manipulate Fractal architectures interactively on the
 * command-line.
 * 
 * @author Pierre-Charles David
 */
public class TextConsole extends AbstractConsole implements Session, Runnable {
    /**
     * The console prompt.
     */
    private static final String PROMPT = "FScript> ";

    /**
     * The welcome banner.
     */
    private static final String BANNER = "FScript console.\n\n" + "Useful commands:\n"
            + "- type ':help' for a list of available commands\n"
            + "- type ':help <cmd>' for detailed help on a specific command\n"
            + "- type ':quit' to exit\n\n";

    /**
     * A JLine console to interact with the user.
     */
    private final ConsoleReader console;

    /**
     * A flag to indicate whether to exit the REPL or not.
     */
    private volatile boolean finished;

    public TextConsole(Component fscript) throws IOException {
        super(fscript);
        this.console = new ConsoleReader();
        register(new QuitCommand());
        addCompletors();
    }

    private void addCompletors() {
        Completor[] allCompletors = new Completor[] { createCommandNameCompletor(),
                createVariableNameCompletor(), createAxisNameCompletor() };
        console.addCompletor(new MultiCompletor(allCompletors));
    }

    private Completor createAxisNameCompletor() {
        return new PrefixCompletor('/') {
            @Override
            protected Set<String> getAllPossibleValues() {
                Set<String> axes = getAxesNames();
                Set<String> completions = new HashSet<String>();
                for (String axis : axes) {
                    completions.add(axis + "::");
                }
                return completions;
            }
        };
    }

    private Completor createVariableNameCompletor() {
        return new PrefixCompletor('$') {
            @Override
            protected Set<String> getAllPossibleValues() {
                return getGlobalVariablesNames();
            }
        };
    }

    private Completor createCommandNameCompletor() {
        String[] commandNames = commands.keySet().toArray(new String[0]);
        for (int i = 0; i < commandNames.length; i++) {
            commandNames[i] = ":" + commandNames[i];
        }
        return new SimpleCompletor(commandNames);
    }

    private class QuitCommand extends AbstractCommand {
        public String getName() {
            return "quit";
        }

        public String getShortDescription() {
            return "Quit the console.";
        }

        @Override
        public String getLongDescription() {
            return getShortDescription();
        }

        public void execute(String args) throws Exception {
            TextConsole.this.finished = true;
        }
    }

    public void run() {
        showMessage(BANNER);
        finished = false;
        while (!finished) {
            try {
                String line = nextInputLine();
                processRequest(line);
            } catch (Exception e) {
                showError("Unexpected error.", e);
                e.printStackTrace();
            }
        }
    }

    private String nextInputLine() {
        try {
            String line = console.readLine(PROMPT);
            if (line != null) {
                return line.trim();
            } else {
                return null;
            }
        } catch (IOException e) {
            showError("IO error while reading user input.", e);
            return null;
        }
    }

    public void printString(String str) {
        try {
            console.printString(str);
        } catch (IOException e) {
            System.err.print("I/O error in the console: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void newline() {
        try {
            console.printNewline();
        } catch (IOException e) {
            System.err.print("I/O error in the console: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void fill(char c, int width) {
        String str = Character.toString(c);
        for (int i = 0; i < width; i++) {
            printString(str);
        }
    }

    public void printLine(String msg) {
        printString(msg);
        newline();
    }

    public void showMessage(String message) {
        printLine(message);
    }

    public void showWarning(String warning) {
        printLine("Warning: " + warning);
    }

    public void showError(String error) {
        printLine("Error: " + error);
    }

    public void showError(String error, Throwable cause) {
        printLine("Error: " + error);
        printLine("Error: " + cause.getMessage());
    }

    @SuppressWarnings("unchecked")
    public void showResult(Object result) {
        if (result instanceof Set<?>) {
            Set<Node> nodes = (Set<Node>) result;
            printLine("=> a node-set with " + nodes.size() + " element(s):");

            List<String> formattedNodes = new ArrayList<String>();
            for (Node node : nodes) {
                formattedNodes.add(node.toString());
            }
            Collections.sort(formattedNodes);
            try {
                console.printColumns(formattedNodes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (result instanceof String) {
            printLine("=> \"" + ((String) result).replace("\"", "\\\"") + "\"");
        } else {
            printLine("=> " + String.valueOf(result));
        }
    }

    public void showTitle(String title) {
        newline();
        printLine(title);
        fill('=', title.length());
        newline();
        newline();
    }

    public void showTable(String[][] table) {
        new TableFormatter(this, table).print();
    }
}
