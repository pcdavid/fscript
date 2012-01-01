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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.console;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.FScriptEngine;
import org.objectweb.fractal.fscript.diagnostics.DiagnosticListener;
import org.objectweb.fractal.util.Fractal;

import com.google.common.base.Preconditions;

/**
 * Common abstract base class for all the console's special commands.
 * 
 * @author Pierre-Charles David
 */
public abstract class AbstractCommand implements Command, Session {
    /**
     * The command's name.
     */
    private String name;

    /**
     * A short (one-line) description of the command.
     */
    private String shortDescription;

    /**
     * A long (multiple lines) description of the command.
     */
    private String longDescription;

    /**
     * The session to use to interact with the user.
     */
    protected Session session;

    /**
     * The FScript implementation component to use.
     */
    protected Component fscript;

    /**
     * The <code>FScriptEngine</code> interface of the <code>fscript</code>
     * implementation component.
     */
    protected FScriptEngine engine;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortDescription(String desc) {
        if (desc != null) {
            this.shortDescription = desc;
        } else {
            this.shortDescription = "(no description available)";
        }
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public void setLongDescription(String desc) {
        if (desc != null) {
            this.longDescription = desc;
        } else {
            this.longDescription = "(no description available)";
        }
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setSession(Session session) {
        Preconditions.checkNotNull(session, "Null session");
        this.session = session;
    }

    public void setFScriptEngine(Component newFscript) {
        Preconditions.checkNotNull(newFscript, "Null FScript implementation");
        Component previous = this.fscript;
        this.fscript = newFscript;
        try {
            this.engine = FScript.getFScriptEngine(fscript);
        } catch (IllegalArgumentException e) {
            this.fscript = previous;
            throw e;
        }
    }

    protected <T> T getEngineInterface(String name, Class<T> klass) {
        try {
            return klass.cast(fscript.getFcInterface(name));
        } catch (NoSuchInterfaceException nsie) {
            throw new IllegalArgumentException("Invalid FScript implementation: "
                    + "missing '" + name + "' interface.");
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("Invalid FScript implementation: " + "'"
                    + name + "' interface is not a " + klass.getName() + ".");
        }
    }

    protected void ensureComponentIsStarted(Component comp)
            throws IllegalLifeCycleException {
        try {
            LifeCycleController lcc = Fractal.getLifeCycleController(comp);
            if (!"STARTED".equals(lcc.getFcState())) {
                showMessage("Starting the component.");
                lcc.startFc();
            } else {
                showMessage("Component already started.");
            }
        } catch (NoSuchInterfaceException e) {
            showWarning("The component does not have a 'lifecycle-controller'.");
            showWarning("Assuming it is ready to use.");
        }
    }

    @Override
    public String toString() {
        return ":" + getName();
    }

    // Delegate Session implementation to the current session

    public void setSessionInterpreter(Component fscript) {
        session.setSessionInterpreter(fscript);
    }

    public void showMessage(String message) {
        session.showMessage(message);
    }

    public void showResult(Object result) {
        session.showResult(result);
    }

    public void showWarning(String warning) {
        session.showWarning(warning);
    }

    public void showError(String error) {
        session.showError(error);
    }

    public void showError(String error, Throwable cause) {
        session.showError(error, cause);
    }

    public void showTitle(String title) {
        session.showTitle(title);
    }

    public void newline() {
        session.newline();
    }

    public void showTable(String[][] table) {
        session.showTable(table);
    }

    public DiagnosticListener getDiagnosticListener() {
        return session.getDiagnosticListener();
    }
}
