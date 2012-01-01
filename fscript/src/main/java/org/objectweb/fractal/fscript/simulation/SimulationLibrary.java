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
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript.simulation;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.AbstractReporter;
import org.objectweb.fractal.fscript.Library;
import org.objectweb.fractal.fscript.ast.UserProcedure;
import org.objectweb.fractal.fscript.procedures.NativeLibrary;
import org.objectweb.fractal.fscript.procedures.Procedure;

public class SimulationLibrary extends AbstractReporter implements Library, BindingController {
    private Library baseLibrary;

    private NativeLibrary modelLibrary;

    public Set<String> getProceduresNames() {
        HashSet<String> names = new HashSet<String>();
        names.addAll(baseLibrary.getProceduresNames());
        names.addAll(modelLibrary.getAvailableProcedures());
        return unmodifiableSet(names);
    }

    public Procedure lookup(String name) {
        checkNotNull(name);
        if (name.equals("adl-new") || name.equals("new-composite") || name.equals("cbind")) {
            return null;
        }
        Procedure proc = modelLibrary.getNativeProcedure(name);
        if (proc != null) {
            return proc;
        } else {
            return baseLibrary.lookup(name);
        }
    }

    public void define(UserProcedure proc) {
        throw new UnsupportedOperationException();
    }

    public void undefine(String procName) throws NoSuchElementException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] listFc() {
        String[] superItfs = super.listFc();
        String[] itfs = new String[superItfs.length + 2];
        itfs[0] = "model-library";
        itfs[1] = "base-library";
        System.arraycopy(superItfs, 0, itfs, 2, superItfs.length);
        return itfs;
    }

    @Override
    public Object lookupFc(String itfName) throws NoSuchInterfaceException {
        if ("model-library".equals(itfName)) {
            return this.modelLibrary;
        } else if ("base-library".equals(itfName)) {
            return this.baseLibrary;
        } else {
            return super.lookupFc(itfName);
        }
    }

    @Override
    public void bindFc(String itfName, Object srvItf) throws NoSuchInterfaceException {
        if ("model-library".equals(itfName)) {
            this.modelLibrary = (NativeLibrary) srvItf;
        } else if ("base-library".equals(itfName)) {
            this.baseLibrary = (Library) srvItf;
        } else {
            super.bindFc(itfName, srvItf);
        }
    }

    @Override
    public void unbindFc(String itfName) throws NoSuchInterfaceException {
        if ("model-library".equals(itfName)) {
            this.modelLibrary = null;
        } else if ("base-library".equals(itfName)) {
            this.baseLibrary = null;
        } else {
            super.unbindFc(itfName);
        }
    }
}
