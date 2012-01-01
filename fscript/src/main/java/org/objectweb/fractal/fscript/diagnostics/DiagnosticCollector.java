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
package org.objectweb.fractal.fscript.diagnostics;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.immutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Level;
import org.objectweb.util.monolog.api.Logger;

import com.google.common.base.Predicate;

/**
 * This component receives diagnostics through its {@link DiagnosticListener} interface, stores
 * them, and make them available through its {@link DiagnosticLog} interface. It can also optionally
 * log them as they are received if connected to a {@link Logger}.
 * 
 * @author Pierre-Charles David
 */
public class DiagnosticCollector implements DiagnosticListener, DiagnosticLog, BindingController {
    public static final String LOGGER_ITF = "logger";

    /**
     * All the currently known diagnostics.
     */
    private final List<Diagnostic> diagnostics = new ArrayList<Diagnostic>();

    /**
     * An optional logger.
     */
    private Logger logger;

    public synchronized void report(Diagnostic diagnostic) {
        diagnostics.add(diagnostic);
        if (logger != null) {
            logger.log(monologLevel(diagnostic.getSeverity()), diagnostic.toString());
        }
    }

    public synchronized List<Diagnostic> getDiagnostics() {
        return Collections.unmodifiableList(new ArrayList<Diagnostic>(diagnostics));
    }

    public List<Diagnostic> getDiagnostics(final Severity minSeverity) {
        return immutableList(filter(getDiagnostics(), new Predicate<Diagnostic>() {
            public boolean apply(Diagnostic d) {
                return d.getSeverity().compareTo(minSeverity) >= 0;
            }
        }));
    }

    public void clear() {
        synchronized (this) {
            diagnostics.clear();
        }
    }

    /**
     * Converts a {@link Severity} into the corresponding Monolog {@link Level}.
     */
    private Level monologLevel(Severity severity) {
        switch (severity) {
        case INFORMATION:
            return BasicLevel.LEVEL_INFO;
        case WARNING:
            return BasicLevel.LEVEL_WARN;
        case ERROR:
            return BasicLevel.LEVEL_ERROR;
        default:
            throw new AssertionError("Unhandled severity level: " + severity);
        }
    }

    // Implementation of BindingController

    public String[] listFc() {
        return new String[] { LOGGER_ITF };
    }

    public synchronized Object lookupFc(String clItfName) throws NoSuchInterfaceException {
        if (LOGGER_ITF.equals(clItfName)) {
            return logger;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    public synchronized void bindFc(String clItfName, Object srvItf)
            throws NoSuchInterfaceException, IllegalBindingException, IllegalLifeCycleException {
        if (LOGGER_ITF.equals(clItfName)) {
            logger = (Logger) srvItf;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    public void unbindFc(String clItfName) throws NoSuchInterfaceException,
            IllegalBindingException, IllegalLifeCycleException {
        if (LOGGER_ITF.equals(clItfName)) {
            logger = null;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }
}
