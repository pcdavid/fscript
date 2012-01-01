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

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.diagnostics.DiagnosticListener;
import org.objectweb.util.monolog.api.Logger;

/**
 * Abstract base class to be extended by primitive component implementations which need to access a
 * {@link DiagnosticListener} and a {@link Logger}. Provides the required code to manage the
 * references to these two service through Fractal's {@link BindingController}, and a few
 * convenience methods to log messages and issue diagnostics.
 * 
 * @author Pierre-Charles David
 */
public abstract class AbstractReporter implements BindingController {
    protected DiagnosticListener diagnostics;

    protected Logger logger;

    protected void log(int level, String msg) {
        if (logger != null) {
            logger.log(level, msg);
        }
    }

    protected void report(Diagnostic d) {
        if (diagnostics != null) {
            diagnostics.report(d);
        }
    }

    public String[] listFc() {
        return new String[] { "diagnostics", "logger" };
    }

    public Object lookupFc(String itfName) throws NoSuchInterfaceException {
        if ("diagnostics".equals(itfName)) {
            return this.diagnostics;
        } else if ("logger".equals(itfName)) {
            return this.logger;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public void bindFc(String itfName, Object srvItf) throws NoSuchInterfaceException {
        if ("diagnostics".equals(itfName)) {
            this.diagnostics = (DiagnosticListener) srvItf;
        } else if ("logger".equals(itfName)) {
            this.logger = (Logger) srvItf;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public void unbindFc(String itfName) throws NoSuchInterfaceException {
        if ("diagnostics".equals(itfName)) {
            this.diagnostics = null;
        } else if ("logger".equals(itfName)) {
            this.logger = null;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }
}
