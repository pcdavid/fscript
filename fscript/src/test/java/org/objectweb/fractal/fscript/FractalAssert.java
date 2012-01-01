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

import static org.junit.Assert.fail;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.util.Fractal;

/**
 * This utility class provides some Fractal-specific assertion methods to be used in JUnit
 * test cases.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class FractalAssert {
    private FractalAssert() {
        // Not intended for instanciation.
    }

    public static void assertHasInterface(String itfName, Component component) {
        try {
            component.getFcInterface(itfName);
        } catch (NoSuchInterfaceException nsie) {
            fail("Component " + component + " expected to have interface '" + itfName
                    + "'");
        }
    }

    public static void assertHasntInterface(String itfName, Component component) {
        try {
            component.getFcInterface(itfName);
            fail("Component " + component + " expected NOT to have interface '" + itfName
                    + "'");
        } catch (NoSuchInterfaceException nsie) {
        }
    }

    public static void assertIsStopped(Component comp) {
        assertState(comp, LifeCycleController.STOPPED, true);
    }

    public static void assertIsStarted(Component comp) {
        assertState(comp, LifeCycleController.STARTED, true);
    }

    public static void assertState(Component comp, String state, boolean strict) {
        try {
            String actual = Fractal.getLifeCycleController(comp).getFcState();
            if (!actual.equals(state)) {
                fail("Component expected to be " + state + " but is " + actual);
            }
        } catch (NoSuchInterfaceException e) {
            if (strict) {
                fail("Component exptected to be " + state
                        + " but has no LifeCycleController.");
            }
        }
    }

    public static void assertContains(Component parent, Component kid) {
        try {
            ContentController cc = Fractal.getContentController(parent);
            Component[] allKids = cc.getFcSubComponents();
            for (Component element : allKids) {
                if (kid == element) {
                    return;
                }
            }
            fail("Composite " + parent + " does not contain " + kid + ".");
        } catch (NoSuchInterfaceException e) {
            fail("Parent argument is not composite.");
        }
    }
}
