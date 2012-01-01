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
package org.objectweb.fractal.fscript;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.fscript.nodes.Node;
import org.objectweb.fractal.util.Fractal;

public class FullScriptTest {
    private Component comanche;

    private FScriptInterpreter fscript;

    private Node comancheNode;

    @Before
    public void setUp() {
        comanche = FactoryHelper.newComanche();
        fscript = new FScriptInterpreter();
        comancheNode = fscript.createComponentNode(comanche);
    }

    private Reader fixture(String name) {
        InputStream stream = FullScriptTest.class.getResourceAsStream(name);
        return new InputStreamReader(stream);
    }

    @Test
    public void replaceScheduler() throws Exception {
        Component fe = getChildByName(comanche, "fe");
        Component oldScheduler = getChildByName(fe, "s");
        assertNotNull("Old scheduler not present.", oldScheduler);
        //
        fscript.loadDefinitions(fixture("replace-scheduler.fscript"));
        fscript.apply("replace-scheduler", new Object[] { comancheNode });
        //
        Component newScheduler = getChildByName(fe, "new-sched");
        assertNotNull("New scheduler not found.", newScheduler);
        assertNull("Old scheduler not removed.", getChildByName(fe, "s"));
        assertNotSame(oldScheduler, newScheduler);
    }

    @Test
    public void brokenReplaceScheduler() throws Exception {
        Component fe = getChildByName(comanche, "fe");
        Component oldScheduler = getChildByName(fe, "s");
        assertNotNull("Old scheduler not present.", oldScheduler);
        //
        fscript.loadDefinitions(fixture("broken-replace-scheduler.fscript"));
        try {
            fscript.apply("replace-scheduler", new Object[] { comancheNode });
            fail("Invalid reconfiguration should have aborted.");
        } catch (FScriptException e) {
            Component newScheduler = getChildByName(fe, "s");
            assertNotNull(newScheduler);
            assertSame(oldScheduler, newScheduler);
        }
    }

    @Test
    public void brokenReplaceSchedulerWithRollback() throws Exception {
        Component fe = getChildByName(comanche, "fe");
        Component oldScheduler = getChildByName(fe, "s");
        assertNotNull("Old scheduler not present.", oldScheduler);
        //
        fscript.loadDefinitions(fixture("broken-replace-scheduler2.fscript"));
        try {
            fscript.apply("broken-replace-scheduler2", new Object[] { comancheNode });
            fail("Invalid reconfiguration should have aborted.");
        } catch (FScriptException e) {
            assertNull("Renaming not reverted.", getChildByName(fe, "old-sched"));
            Component newScheduler = getChildByName(fe, "s");
            assertNotNull(newScheduler);
            assertSame(oldScheduler, newScheduler);
            assertNull("New scheduler additon not reverted.", getChildByName(fe,
                    "comanche.MultiThreadScheduler"));
        }
    }

    private Component getChildByName(Component parent, String name)
            throws NoSuchInterfaceException {
        ContentController cc = Fractal.getContentController(parent);
        for (Component child : cc.getFcSubComponents()) {
            if (name.equals(Fractal.getNameController(child).getFcName())) {
                return child;
            }
        }
        return null;
    }
}
