package org.objectweb.fractal.fscript;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.model.fractal.NodeFactory;
import org.objectweb.fractal.util.Fractal;

public class ReplaceTest extends FractalTestCase {

    private Component fscript;

    private HelloWorldHelper hello1;

    private HelloWorldHelper hello2;

    @Before
    public void setUp() throws Throwable {
        fscript = FScript.newEngine();
        hello1 = new HelloWorldHelper();
        hello2 = new HelloWorldHelper();
        NodeFactory nf = FScript.getNodeFactory(fscript);
        FScriptEngine engine = FScript.getFScriptEngine(fscript);
        engine.setGlobalVariable("client1", nf.createComponentNode(hello1.client));
        engine.setGlobalVariable("server1", nf.createComponentNode(hello1.server));
        engine.setGlobalVariable("client2", nf.createComponentNode(hello2.client));
        engine.setGlobalVariable("server2", nf.createComponentNode(hello2.server));

        engine.execute("disconnect($server2, $server2/..)");
        engine.execute("disconnect($client2, $client2/..)");
    }

    @Test
    public void replaceClient() throws FScriptException, NoSuchInterfaceException {
        FScriptEngine engine = FScript.getFScriptEngine(fscript);
        engine.execute("replace($client1, $client2)");
        assertSame("client2 now in hello1", hello1.hello, Fractal.getSuperController(hello2.client)
                .getFcSuperComponents()[0]);
        assertEquals("client1 has not parent", 0, Fractal.getSuperController(hello1.client)
                .getFcSuperComponents().length);
        // TODO Also check bindings.
    }
}
