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

import static com.google.common.collect.Sets.immutableSet;
import static org.objectweb.util.monolog.api.BasicLevel.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.ast.ASTNode;
import org.objectweb.fractal.fscript.ast.ExplicitReturn;
import org.objectweb.fractal.fscript.ast.SourceLocation;
import org.objectweb.fractal.fscript.ast.UserProcedure;
import org.objectweb.fractal.fscript.model.ModelChecker;
import org.objectweb.fractal.fscript.parser.IFScriptParser;
import org.objectweb.util.monolog.api.Logger;

/**
 * This component coordinates the different steps of the front-end analysis when a script
 * containing procedure definitions is loaded.
 * 
 * @author Pierre-Charles David
 */
public class FrontendDriver implements ScriptLoader, FragmentLoader, BindingController {
    /**
     * This prefix is used to generate fresh names for temporary procedures.
     * 
     * @see FragmentLoader
     */
    private static final String WRAPPER_PREFIX = "__wrapped__";

    /**
     * A counter used as a suffix to temporary procedures names, to make them unique.
     */
    private final AtomicLong WRAPPER_ID = new AtomicLong();

    /**
     * The parser implements the first phase of the front-end analysis, turning source
     * code into an AST.
     */
    private IFScriptParser fscriptParser;

    /**
     * Implements the second phase of the front-end analysis, checking the AST for
     * language-level errors (e.g. unused variables), and optionally normalizing the AST.
     */
    private LanguageChecker languageChecker;

    /**
     * Implements the third and final phase of the front-end analysis, checking the
     * semantics of the procedures with respect to the model/profile used, and optionally
     * generating pre- and post-conditions to be used at a later phase.
     */
    private ModelChecker modelChecker;

    /**
     * Once validated by this front-end, user-defined procedures are stored in the library
     * component.
     */
    private Library library;

    private Logger logger;

    public Set<String> load(String source) throws InvalidScriptException {
        return load(new StringReader(source));
    }

    public Set<String> load(Reader source) throws InvalidScriptException {
        logger.log(INFO, "Loading new procedure definitions.");
        List<UserProcedure> procs = parseDefinitions(source);
        Set<String> names = new HashSet<String>();
        for (UserProcedure proc : procs) {
            load(proc);
            names.add(proc.getName());
        }
        logger.log(INFO, "Loaded " + names.size() + " procedure definitions.");
        return immutableSet(names);
    }

    private void load(UserProcedure proc) throws InvalidScriptException {
        doLanguageChecks(proc);
        doModelChecks(proc);
        library.define(proc);
    }

    private List<UserProcedure> parseDefinitions(Reader source) throws InvalidScriptException {
        List<UserProcedure> procs = fscriptParser.parseDefinitions(source);
        logger.log(DEBUG, "Front-end phase 1 (parsing) passed.");
        return procs;
    }

    private void doLanguageChecks(UserProcedure proc)
            throws InvalidScriptException {
        if (languageChecker == null) {
            logger.log(WARN, "Front-end phase 2 (language checks) ignored.");
        } else {
            languageChecker.check(proc);
            logger.log(DEBUG, "Front-end phase 2 (language checks) passed.");
        }
    }

    private void doModelChecks(UserProcedure proc) throws InvalidScriptException {
        if (modelChecker == null) {
            logger.log(WARN, "Front-end phase 3 (model checks) ignored.");
        } else {
            logger.log(DEBUG, "Front-end phase 3 (model checks) ignored: not implemented yet.");
        }
    }

    public String loadFragment(Reader source) throws InvalidScriptException {
        String str;
        try {
            str = readSource(source);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
        ASTNode body;
        try {
            body = fscriptParser.parseStatement(new StringReader(str));
        } catch (InvalidScriptException ise) {
            // Try as a plain expression if not a full-blown statement.
            body = fscriptParser.parseExpression(new StringReader(str));
        }
        UserProcedure proc = createTempProcedure(body);
        load(proc);
        return proc.getName();
    }

    public void unloadFragment(String tempProcName) {
        library.undefine(tempProcName);
    }

    private String readSource(Reader source) throws IOException {
        StringWriter sw = new StringWriter();
        char[] buffer = new char[128];
        int read = 0;
        while ((read = source.read(buffer)) != -1) {
            sw.write(buffer, 0, read);
        }
        return sw.toString();
    }

    /**
     * Wraps a simple statement (or expression) into a full procedure definition. Given a
     * statement <code>S</code>, the procedure generated is equivalent to:
     * <code>action __wrapped__<i>N</i>() { return <i>S</i>; }</code>.
     */
    private UserProcedure createTempProcedure(ASTNode statement) {
        String name = freshProcedureName();
        List<String> params = Collections.emptyList();
        SourceLocation loc = statement.getSourceLocation();
        ASTNode body = new ExplicitReturn(loc, statement);
        return new UserProcedure(loc, false, name, params, body);
    }

    private String freshProcedureName() {
        return WRAPPER_PREFIX + WRAPPER_ID.incrementAndGet();
    }

    public String[] listFc() {
        return new String[] { "parser", "language-checker", "model-checker", "library", "logger" };
    }

    public void bindFc(String clItfName, Object srvItf) throws NoSuchInterfaceException,
            IllegalBindingException, IllegalLifeCycleException {
        if ("parser".equals(clItfName)) {
            this.fscriptParser = (IFScriptParser) srvItf;
        } else if ("language-checker".equals(clItfName)) {
            this.languageChecker = (LanguageChecker) srvItf;
        } else if ("model-checker".equals(clItfName)) {
            this.modelChecker = (ModelChecker) srvItf;
        } else if ("library".equals(clItfName)) {
            this.library = (Library) srvItf;
        } else if ("logger".equals(clItfName)) {
            this.logger = (Logger) srvItf;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    public Object lookupFc(String clItfName) throws NoSuchInterfaceException {
        if ("parser".equals(clItfName)) {
            return this.fscriptParser;
        } else if ("language-checker".equals(clItfName)) {
            return this.languageChecker;
        } else if ("model-checker".equals(clItfName)) {
            return this.modelChecker;
        } else if ("library".equals(clItfName)) {
            return this.library;
        } else if ("logger".equals(clItfName)) {
            return this.logger;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    public void unbindFc(String clItfName) throws NoSuchInterfaceException,
            IllegalBindingException, IllegalLifeCycleException {
        if ("parser".equals(clItfName)) {
            this.fscriptParser = null;
        } else if ("language-checker".equals(clItfName)) {
            this.languageChecker = null;
        } else if ("model-checker".equals(clItfName)) {
            this.modelChecker = null;
        } else if ("library".equals(clItfName)) {
            this.library = null;
        } else if ("logger".equals(clItfName)) {
            this.logger = null;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }
}
