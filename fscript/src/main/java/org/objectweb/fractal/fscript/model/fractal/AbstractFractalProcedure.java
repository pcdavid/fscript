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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.model.fractal;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;

/**
 * Abstract base class to ease the implementation of custom Fractal-related procedures,
 * which depend on the {@link FractalModel}.
 * 
 * @author Pierre-Charles David
 */
public abstract class AbstractFractalProcedure implements NativeProcedure, BindingController {
    protected FractalModel model;

    public String[] listFc() {
        return new String[] { "fractal-model" };
    }

    public void bindFc(String itfName, Object srvItf) throws NoSuchInterfaceException {
        if ("fractal-model".equals(itfName)) {
            this.model = (FractalModel) srvItf;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public Object lookupFc(String itfName) throws NoSuchInterfaceException {
        if ("fractal-model".equals(itfName)) {
            return this.model;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public void unbindFc(String itfName) throws NoSuchInterfaceException {
        if ("fractal-model".equals(itfName)) {
            this.model = null;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }
}