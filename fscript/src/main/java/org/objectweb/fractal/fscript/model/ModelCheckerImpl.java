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
package org.objectweb.fractal.fscript.model;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.Library;
import org.objectweb.fractal.fscript.diagnostics.DiagnosticListener;
import org.objectweb.util.monolog.api.Logger;

public class ModelCheckerImpl implements ModelChecker, BindingController {
    public static final String MODEL_ITF = "model";

    public static final String USER_LIB_ITF = "library";

    public static final String DIAGNOSTICS_ITF = "diagnostics";

    public static final String LOGGER_ITF = "logger";

    private Model model;

    private Library library;

    private DiagnosticListener diagnostics;

    private Logger logger;

    public void bindFc(String clItfName, Object srvItf) throws NoSuchInterfaceException,
            IllegalBindingException, IllegalLifeCycleException {
        if (MODEL_ITF.equals(clItfName)) {
            this.model = (Model) srvItf;
        } else if (USER_LIB_ITF.equals(clItfName)) {
            this.library = (Library) srvItf;
        } else if (DIAGNOSTICS_ITF.equals(clItfName)) {
            this.diagnostics = (DiagnosticListener) srvItf;
        } else if (LOGGER_ITF.equals(clItfName)) {
            this.logger = (Logger) srvItf;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    public String[] listFc() {
        return new String[] { MODEL_ITF, USER_LIB_ITF, DIAGNOSTICS_ITF, LOGGER_ITF };
    }

    public Object lookupFc(String clItfName) throws NoSuchInterfaceException {
        if (MODEL_ITF.equals(clItfName)) {
            return this.model;
        } else if (USER_LIB_ITF.equals(clItfName)) {
            return this.library;
        } else if (DIAGNOSTICS_ITF.equals(clItfName)) {
            return this.diagnostics;
        } else if (LOGGER_ITF.equals(clItfName)) {
            return this.logger;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    public void unbindFc(String clItfName) throws NoSuchInterfaceException,
            IllegalBindingException, IllegalLifeCycleException {
        if (MODEL_ITF.equals(clItfName)) {
            this.model = null;
        } else if (USER_LIB_ITF.equals(clItfName)) {
            this.library = null;
        } else if (DIAGNOSTICS_ITF.equals(clItfName)) {
            this.diagnostics = null;
        } else if (LOGGER_ITF.equals(clItfName)) {
            this.logger = null;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }
}