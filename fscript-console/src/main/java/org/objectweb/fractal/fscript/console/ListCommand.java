package org.objectweb.fractal.fscript.console;

import org.objectweb.fractal.fscript.FScriptInterpreter;
import org.objectweb.fractal.rmi.registry.NamingService;

public class ListCommand extends AbstractCommand {
    public ListCommand(Console console, FScriptInterpreter fscript) {
        super(console, fscript);
    }

    public void execute(String args) throws Exception {
        NamingService ns = fscript.getCurrentRegistry();
        String[] bindings = ns.list();
        console.printMessage("Active Fractal RMI bindings:");
        for (String name : bindings) {
            console.printMessage(name + "\t\t" + ns.lookup(name));
        }
    }

    public String getDescription() {
        return "Lists all the active Fractal RMI bindings";
    }

    public String getName() {
        return "list";
    }
}
