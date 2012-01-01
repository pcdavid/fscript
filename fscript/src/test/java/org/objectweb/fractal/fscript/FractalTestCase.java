/*
 * Copyright (c) 2008 ARMINES
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

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Map;

import org.junit.BeforeClass;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.adl.FactoryFactory;
import org.objectweb.fractal.api.Component;

/**
 * An abstract base class intended to be extended by test-cases which rely on Fractal
 * components. It makes sure Fractal is correctly initialized and provides some helper
 * methods to make tests easier to write.
 * 
 * @author Pierre-Charles David
 */
public abstract class FractalTestCase {
    private static Factory defaultFactory;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        initializeFractal();
    }

    public static void initializeFractal() {
        String currentProvider = System.getProperty("fractal.provider");
        if (currentProvider == null || currentProvider.length() == 0) {
            System.setProperty("fractal.provider", "org.objectweb.fractal.julia.Julia");
        }
    }

    /**
     * Instantiates a new Fractal component given its ADL descriptor. This uses the
     * default Fractal ADL factory.
     */
    public static Component newComponent(String name) {
        return newComponent(name, Collections.EMPTY_MAP);
    }

    /**
     * Instantiates a new Fractal component given its ADL descriptor and parameters. This
     * uses the default Fractal ADL factory.
     */
    @SuppressWarnings("unchecked")
    public static Component newComponent(String name, Map context) {
        try {
            return (Component) getDefaultFactory().newComponent(name, context);
        } catch (final ADLException e) {
            fail("Unable to instanciate component " + name + ".");
            return null;
        }
    }

    /**
     * Returns a default Fractal ADL Factory, using the <code>FRACTAL_BACKEND</code>.
     */
    public static Factory getDefaultFactory() {
        if (defaultFactory == null) {
            try {
                defaultFactory = FactoryFactory.getFactory(FactoryFactory.FRACTAL_BACKEND);
            } catch (final ADLException e) {
                fail("Unable to create default ADL defaultFactory.");
            }
        }
        return defaultFactory;
    }
}
