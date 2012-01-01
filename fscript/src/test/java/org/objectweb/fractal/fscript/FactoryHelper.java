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

import java.util.Map;

import org.junit.Assert;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.adl.FactoryFactory;
import org.objectweb.fractal.api.Component;

/**
 * Helper class to instanciate components used during the tests.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class FactoryHelper {
    public static final String HELLO_WORLD_APP = "HelloWorld";

    public static final String COMANCHE_APP = "comanche.Comanche";

    private static Factory factory;

    static {
        System.setProperty("fractal.provider", "org.objectweb.fractal.julia.Julia");
        try {
            factory = FactoryFactory.getFactory(FactoryFactory.FRACTAL_BACKEND);
        } catch (ADLException e) {
            Assert.fail("Unable to create default ADL factory.");
        }
    }

    public static Component newHelloWorld() {
        return newComponent(HELLO_WORLD_APP);
    }

    public static Component newComanche() {
        return newComponent(COMANCHE_APP);
    }

    /**
     * Instanciates a new Fractal component given its ADL descriptor.
     */
    public static Component newComponent(String name) {
        return newComponent(name, null);
    }

    /**
     * Instanciates a new Fractal component given its ADL descriptor and parameters.
     */
    public static Component newComponent(String name, Map context) {
        try {
            return (Component) factory.newComponent(name, context);
        } catch (ADLException e) {
            Assert.fail("Unable to instanciate component " + name + ".");
            return null;
        }
    }
}
