/*
 * Copyright (c) 2006-2008 ARMINES
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

import static com.google.common.collect.MapConstraints.constrainedMap;
import static org.objectweb.util.monolog.api.BasicLevel.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.simulation.Simulator;
import org.objectweb.fractal.fscript.simulation.Trace;
import org.objectweb.util.monolog.api.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapConstraints;

/**
 * This component implements the main entry point used by FScript clients to execute FPath
 * queries and FScript reconfigurations. If a transaction manager is available, it is
 * automatically used to "wrap" each execution request (i.e. <code>execute()</code> or
 * <code>invoke()</code> calls) into a separate transaction.
 * 
 * @author Pierre-Charles David
 */
public class BackendDriver implements FScriptEngine, BindingController {
    /**
     * Global variables.
     */
    private final Map<String, Object> globals;

    /**
     * This is used by the <code>execute()</code> methods to create temporary procedures
     * out of code fragments supplied by the user.
     */
    private FragmentLoader fragmentLoader;

    /**
     * An optional simulator.
     */
    private Simulator simulator;

    /**
     * The actual execution engine, typically a simple interpreter.
     */
    private Executor executor;

    /**
     * Used if available to wrap the executions in transactions.
     */
    private UserTransaction transactionManager;

    /**
     * Used to log various internal message.
     */
    private Logger logger;

    public BackendDriver() {
        globals = constrainedMap(new HashMap<String, Object>(), MapConstraints.NOT_NULL);
    }

    public Set<String> getGlobals() {
        return Collections.unmodifiableSet(new HashSet<String>(globals.keySet()));
    }

    public Object getGlobalVariable(String name) {
        return globals.get(name);
    }

    public void setGlobalVariable(String name, Object value) {
        if (value != null) {
            globals.put(name, value);
        } else {
            globals.remove(name);
        }
    }

    public Object execute(String source) throws FScriptException {
        return execute(new StringReader(source));
    }

    public Object execute(Reader source) throws FScriptException {
        Preconditions.checkNotNull(source);
        String tempProcName = fragmentLoader.loadFragment(source);
        try {
            return invoke(tempProcName);
        } finally {
            fragmentLoader.unloadFragment(tempProcName);
        }
    }

    /**
     * This is the main method which all the other <code>execute()</code> and
     * <code>invoke()</code> methods eventually call.
     */
    public Object invoke(String procName, Object... args) throws FScriptException {
        Preconditions.checkNotNull(procName, "Missing procedure name.");
        if (simulator != null) {
            simulateInvocation(procName, args);
        }
        logger.log(INFO, "Starting execution of " + procName + "().");
        startTransaction();
        try {
            Object result = executor.invoke(procName, args, globals);
            commitTransaction();
            logger.log(INFO, "Execution finished successfuly.");
            return result;
        } catch (ScriptExecutionError e) {
            logger.log(ERROR, "Reconfiguration failed.");
            rollbackTransaction(e);
            if (transactionManager != null) {
                throw new FScriptException("Transaction rolled back.", e);
            } else {
                throw new FScriptException("Reconfiguration failed.", e);
            }
        } catch (FScriptException e) {
            logger.log(ERROR, "Commit failed.", e);
            throw e;
        } catch (Exception e) {
            logger.log(ERROR, "Unexpected error during execution: " + e.getMessage());
            rollbackTransaction(e);
            if (transactionManager != null) {
                throw new FScriptException("Transaction rolled back.", e);
            } else {
                throw new FScriptException("Reconfiguration failed.", e);
            }
        }
    }

    private void simulateInvocation(String procName, Object[] args) throws ScriptExecutionError {
        logger.log(INFO, "Starting simulation of " + procName + "().");
        try {
            @SuppressWarnings("unused")
            Trace trace = simulator.simulate(procName, args, globals);
            logger.log(INFO, "Simulation OK.");
        } catch (ScriptExecutionError see) {
            logger.log(WARN, "Simulation failed.");
            throw see;
        }
    }

    private void startTransaction() throws FScriptException {
        if (transactionManager == null) {
            logger.log(WARN, "No transaction manager available");
            return;
        }
        logger.log(INFO, "Starting transaction.");
        try {
            transactionManager.begin();
        } catch (NotSupportedException e) {
            throw new FScriptException("Could not start the transaction.", e);
        } catch (SystemException e) {
            throw new FScriptException("Could not start the transaction.", e);
        }
    }

    private void commitTransaction() throws FScriptException {
        if (transactionManager == null) {
            // No need to issue a warning if there is no transaction manager, as one has
            // already been issued when starting the transaction.
            return;
        }
        logger.log(INFO, "Commiting transaction.");
        try {
            transactionManager.commit();
        } catch (RollbackException e) {
            throw new FScriptException("Commit failed: transaction rolled back.", e);
        } catch (HeuristicRollbackException e) {
            throw new FScriptException("Commit failed: transaction rolled back.", e);
        } catch (HeuristicMixedException e) {
            throw new FScriptException("Commit failed: transaction partially rolled back.", e);
        } catch (SystemException e) {
            throw new FScriptException("Commit failed: internal error in the TM.", e);
        }
        logger.log(INFO, "Transaction commited.");
    }

    private void rollbackTransaction(Exception cause) throws FScriptException {
        if (transactionManager == null) {
            // No need to issue a warning if there is no transaction manager, as one has
            // already been issued when starting the transaction.
            return;
        }
        try {
            logger.log(INFO, "Rollbacking transaction.");
            transactionManager.rollback();
        } catch (SystemException se) {
            logger.log(ERROR, "Error during rollback.", se);
            throw new FScriptException("Unable to rollback reconfiguration.", se);
        }
    }

    public String[] listFc() {
        return new String[] { "executor", "simulator", "fragment-loader", "transaction-manager", "logger" };
    }

    public void bindFc(String itfName, Object srvItf) throws NoSuchInterfaceException,
            IllegalBindingException, IllegalLifeCycleException {
        if ("fragment-loader".equals(itfName)) {
            this.fragmentLoader = (FragmentLoader) srvItf;
        } else if ("executor".equals(itfName)) {
            this.executor = (Executor) srvItf;
        } else if ("simulator".equals(itfName)) {
            this.simulator = (Simulator) srvItf;
        } else if ("transaction-manager".equals(itfName)) {
            this.transactionManager = (UserTransaction) srvItf;
        } else if ("logger".equals(itfName)) {
            this.logger = (Logger) srvItf;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public Object lookupFc(String itfName) throws NoSuchInterfaceException {
        if ("fragment-loader".equals(itfName)) {
            return this.fragmentLoader;
        } else if ("executor".equals(itfName)) {
            return this.executor;
        } else if ("simulator".equals(itfName)) {
            return this.simulator;
        } else if ("transaction-manager".equals(itfName)) {
            return this.transactionManager;
        } else if ("logger".equals(itfName)) {
            return this.logger;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    public void unbindFc(String itfName) throws NoSuchInterfaceException, IllegalBindingException,
            IllegalLifeCycleException {
        if ("fragment-loader".equals(itfName)) {
            this.fragmentLoader = null;
        } else if ("executor".equals(itfName)) {
            this.executor = null;
        } else if ("simulator".equals(itfName)) {
            this.simulator = null;
        } else if ("transaction-manager".equals(itfName)) {
            this.transactionManager = null;
        } else if ("logger".equals(itfName)) {
            this.logger = null;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }
}
