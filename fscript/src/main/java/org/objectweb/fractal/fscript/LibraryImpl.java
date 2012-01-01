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
package org.objectweb.fractal.fscript;

import static com.google.common.base.Preconditions.*;
import static java.util.Collections.unmodifiableSet;
import static org.objectweb.fractal.fscript.diagnostics.Diagnostic.information;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.ast.SourceLocation;
import org.objectweb.fractal.fscript.ast.UserProcedure;
import org.objectweb.fractal.fscript.procedures.CoreLibrary;
import org.objectweb.fractal.fscript.procedures.NativeLibrary;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.fractal.fscript.procedures.Procedure;

/**
 * This component is a {@link Library} which contains all the procedures available to
 * FScript programs, either native procedures or user-defined ones which have previously
 * been {@linkplain #define(UserProcedure) defined}.
 * 
 * @author Pierre-Charles David
 */
public class LibraryImpl extends AbstractReporter implements Library, BindingController {
    /**
     * The core library of native, model-independent procedures always available in FPath
     * and FScript.
     */
    private NativeLibrary coreLibrary = new CoreLibrary();

    /**
     * The library of native procedures contributed by the actual model.
     */
    private NativeLibrary modelLibrary;

    /**
     * The procedures defined in FScript which have been loaded in this library.
     */
    private Map<String, UserProcedure> userProcedures = new HashMap<String, UserProcedure>();

    public Set<String> getProceduresNames() {
        HashSet<String> names = new HashSet<String>();
        names.addAll(coreLibrary.getAvailableProcedures());
        names.addAll(modelLibrary.getAvailableProcedures());
        names.addAll(userProcedures.keySet());
        return unmodifiableSet(names);
    }

    public Procedure lookup(String name) {
        checkNotNull(name);
        Procedure proc = lookupNative(name);
        if (proc != null) {
            return proc;
        } else {
            return userProcedures.get(name);
        }
    }

    private NativeProcedure lookupNative(String name) {
        NativeProcedure proc = coreLibrary.getNativeProcedure(name);
        if (proc != null) {
            return proc;
        } else {
            return modelLibrary.getNativeProcedure(name);
        }
    }

    public void define(UserProcedure proc) {
        checkNotNull(proc);
        checkArgument(lookupNative(proc.getName()) == null, "A native procedure named '"
                + proc.getName() + "' already exists.");
        String name = proc.getName();
        UserProcedure previous = userProcedures.put(name, proc);
        if (previous != null) {
            SourceLocation loc = proc.getSourceLocation();
            report(information(loc, "New definition of method '%s' registered.", name));
        }
    }

    public void undefine(String procName) throws NoSuchElementException {
        checkNotNull(procName);
        checkArgument(lookupNative(procName) == null, "Native procedure '" + procName
                + "' can not be undefined.");
        UserProcedure prev = userProcedures.remove(procName);
        if (prev == null) {
            throw new NoSuchElementException("No user procedure named " + procName + " defined.");
        }
    }

    @Override
    public String[] listFc() {
        String[] superItfs = super.listFc();
        String[] itfs = new String[superItfs.length + 1];
        itfs[0] = "model-library";
        System.arraycopy(superItfs, 0, itfs, 1, superItfs.length);
        return itfs;
    }

    @Override
    public Object lookupFc(String itfName) throws NoSuchInterfaceException {
        if ("model-library".equals(itfName)) {
            return this.modelLibrary;
        } else {
            return super.lookupFc(itfName);
        }
    }

    @Override
    public void bindFc(String itfName, Object srvItf) throws NoSuchInterfaceException {
        if ("model-library".equals(itfName)) {
            this.modelLibrary = (NativeLibrary) srvItf;
        } else {
            super.bindFc(itfName, srvItf);
        }
    }

    @Override
    public void unbindFc(String itfName) throws NoSuchInterfaceException {
        if ("model-library".equals(itfName)) {
            this.modelLibrary = null;
        } else {
            super.unbindFc(itfName);
        }
    }
}
