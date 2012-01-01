package org.objectweb.fractal.fscript.console;

import org.objectweb.fractal.fscript.FScriptInterpreter;

public class ConnectCommand extends AbstractCommand {
    public ConnectCommand(Console console, FScriptInterpreter fscript) {
        super(console, fscript);
    }

    public void execute(String args) throws Exception {
        int i = args.indexOf(' ');
        String host = args.substring(0, i);
        String substring = args.substring(i + 1);
        int port = Integer.parseInt(substring);
        fscript.connect(host, port);
    }

    public String getDescription() {
        return "Connect to a Fractal RMI registry.";
    }

    public String getName() {
        return "connect";
    }
}
