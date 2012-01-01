/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2008 ARMINES
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
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.NodeFactory;
import org.objectweb.fractal.util.Fractal;

public class FullScriptTest extends FractalTestCase {
    private Component comanche;

    private FScriptEngine engine;

    private Node comancheNode;

    private Component fscript;

    private ScriptLoader loader;

    @Before
    public void setUp() throws Exception {
        comanche = new ComancheHelper().comanche;
        fscript = FScript.newEngine();
        engine = FScript.getFScriptEngine(fscript);
        loader = FScript.getScriptLoader(fscript);
        NodeFactory nf = FScript.getNodeFactory(fscript);
        comancheNode = nf.createComponentNode(comanche);
    }

    private Reader fixture(final String name) {
        final InputStream stream = FullScriptTest.class.getResourceAsStream(name);
        return new InputStreamReader(stream);
    }

    @Test
    public void replaceScheduler() throws Exception {
        final Component fe = getChildByName(comanche, "fe");
        final Component oldScheduler = getChildByName(fe, "s");
        assertNotNull("Old scheduler not present.", oldScheduler);
        //
        loader.load(fixture("replace-scheduler.fscript"));
        engine.invoke("replace-scheduler", comancheNode);
        //
        final Component newScheduler = getChildByName(fe, "new-sched");
        assertNotNull("New scheduler not found.", newScheduler);
        assertNull("Old scheduler not removed.", getChildByName(fe, "s"));
        assertNotSame(oldScheduler, newScheduler);
    }

    @Ignore("Expected to fail until new transaction manager is integrated.")
    @Test
    public void brokenReplaceScheduler() throws Exception {
        final Component fe = getChildByName(comanche, "fe");
        final Component oldScheduler = getChildByName(fe, "s");
        assertNotNull("Old scheduler not present.", oldScheduler);
        //
        loader.load(fixture("broken-replace-scheduler.fscript"));
        try {
            engine.invoke("replace-scheduler", comancheNode);
            fail("Invalid reconfiguration should have aborted.");
        } catch (final FScriptException e) {
            assertTrue(e.getMessage().startsWith("Transaction rolled back"));
            final Component scheduler = getChildByName(fe, "s");
            assertNotNull(scheduler);
            assertSame(oldScheduler, scheduler);
            assertNull("New scheduler not removed: incorrect rollback.", getChildByName(fe,
                    "new-sched"));
        }
    }

    @Ignore("Expected to fail until new transaction manager is integrated.")
    @Test
    public void brokenReplaceSchedulerWithRollback() throws Exception {
        final Component fe = getChildByName(comanche, "fe");
        final Component oldScheduler = getChildByName(fe, "s");
        assertNotNull("Old scheduler not present.", oldScheduler);
        //
        loader.load(fixture("broken-replace-scheduler2.fscript"));
        try {
            engine.invoke("broken-replace-scheduler2", comancheNode);
            fail("Invalid reconfiguration should have aborted.");
        } catch (final FScriptException e) {
            assertNull("Renaming not reverted.", getChildByName(fe, "old-sched"));
            final Component newScheduler = getChildByName(fe, "s");
            assertNotNull(newScheduler);
            assertSame(oldScheduler, newScheduler);
            assertNull("New scheduler additon not reverted.", getChildByName(fe,
                    "comanche.MultiThreadScheduler"));
        }
    }

    private Component getChildByName(final Component parent, final String name)
            throws NoSuchInterfaceException {
        final ContentController cc = Fractal.getContentController(parent);
        for (final Component child : cc.getFcSubComponents()) {
            if (name.equals(Fractal.getNameController(child).getFcName())) {
                return child;
            }
        }
        return null;
    }
}
