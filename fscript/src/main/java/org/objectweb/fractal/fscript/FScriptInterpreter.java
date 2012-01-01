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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.fscript.axes.AttributesAxis;
import org.objectweb.fractal.fscript.axes.Axis;
import org.objectweb.fractal.fscript.axes.BindingAxis;
import org.objectweb.fractal.fscript.axes.ChildAxis;
import org.objectweb.fractal.fscript.axes.ComponentAxis;
import org.objectweb.fractal.fscript.axes.InterfacesAxis;
import org.objectweb.fractal.fscript.axes.InternalInterfacesAxis;
import org.objectweb.fractal.fscript.axes.MethodAxis;
import org.objectweb.fractal.fscript.axes.ParentAxis;
import org.objectweb.fractal.fscript.axes.SiblingAxis;
import org.objectweb.fractal.fscript.expressions.Expression;
import org.objectweb.fractal.fscript.nodes.AttributeNode;
import org.objectweb.fractal.fscript.nodes.ComponentNode;
import org.objectweb.fractal.fscript.nodes.ComponentNodeImpl;
import org.objectweb.fractal.fscript.nodes.DefaultNodeFactory;
import org.objectweb.fractal.fscript.nodes.InterfaceNode;
import org.objectweb.fractal.fscript.nodes.InterfaceNodeImpl;
import org.objectweb.fractal.fscript.nodes.MethodNode;
import org.objectweb.fractal.fscript.nodes.Node;
import org.objectweb.fractal.fscript.nodes.NodeFactory;
import org.objectweb.fractal.fscript.parser.FScriptLexer;
import org.objectweb.fractal.fscript.parser.FScriptParser1;
import org.objectweb.fractal.fscript.reconfiguration.ReconfigurationManager;
import org.objectweb.fractal.fscript.statements.Statement;
import org.objectweb.fractal.fscript.statements.UserDefinedProcedure;
import org.objectweb.fractal.rmi.registry.NamingService;
import org.objectweb.fractal.rmi.registry.Registry;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * The main entry point to FScript;
 *
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class FScriptInterpreter implements NodeFactory {

    private Environment toplevel;

    private Map<String, Procedure> procedures;

    private Map<String, Object> context;

    private DefaultNodeFactory factory;

    private Map<String, Axis> axes;

    /**
     * Creates a new FScript interpreter, with just the defaults functions and actions
     * defined.
     */
    public FScriptInterpreter() {
        this.toplevel = new Environment(this);
        this.procedures = new HashMap<String, Procedure>();
        this.context = new HashMap<String, Object>();
        this.factory = new DefaultNodeFactory();
        this.axes = new HashMap<String, Axis>();
        defineAxes();
        try {
            populateInitialEnvironment();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void connect(String host, int port) throws Exception {
        NamingService namingService = Registry.getRegistry(host, port);
        toplevel.setVariable("*rmiregistry*", namingService);
    }
    
    public NamingService getCurrentRegistry() {
        return (NamingService) toplevel.getVariable("*rmiregistry*");
    }

    /**
     * Returns the top-level environment in which the interpreter executes its programs.
     */
    public Environment getEnvironment() {
        return toplevel;
    }

    /**
     * Finds a procedure (function or action) by name.
     *
     * @param procName
     *            the name of the procedure to find.
     * @return the procedure, or <code>null</code> if none exist with that name.
     */
    public Procedure getProcedure(String procName) {
        return procedures.get(procName);
    }

    private void defineAxes() {
        defineAxis(new ComponentAxis(factory));
        defineAxis(new AttributesAxis(factory));
        defineAxis(new InterfacesAxis(factory));
        defineAxis(new InternalInterfacesAxis(factory));
        defineAxis(new BindingAxis(factory));
        //
        defineAxis(new ChildAxis(factory, false, false));
        defineAxis(new ChildAxis(factory, true, false));
        defineAxis(new ChildAxis(factory, false, true));
        defineAxis(new ChildAxis(factory, true, true));
        //
        defineAxis(new ParentAxis(factory, false, false));
        defineAxis(new ParentAxis(factory, true, false));
        defineAxis(new ParentAxis(factory, false, true));
        defineAxis(new ParentAxis(factory, true, true));
        //
        defineAxis(new SiblingAxis(factory, false));
        defineAxis(new SiblingAxis(factory, true));
        //
        defineAxis(new MethodAxis(factory));
    }

    private void defineAxis(Axis axis) {
        axes.put(axis.getName(), axis);
    }

    private final void populateInitialEnvironment() throws IOException,
            ClassNotFoundException, InstantiationException, IllegalAccessException,
            FScriptException {
        registerPrimitiveProcedures("standard-functions.properties");
        registerPrimitiveProcedures("standard-actions.properties");
        InputStream corelib = FScriptInterpreter.class
                .getResourceAsStream("corelib.fscript");
        loadDefinitions(new InputStreamReader(corelib));
    }

    private void registerPrimitiveProcedures(String propertiesFileName)
            throws IOException, ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        Properties props = new Properties();
        props.load(FScriptInterpreter.class.getResourceAsStream(propertiesFileName));
        for (Object key : props.keySet()) {
            String className = (String) props.get(key);
            Class<?> klass = Class.forName(className);
            defineProcedure((Procedure) klass.newInstance());
        }
    }

    public String[] getAxisNames() {
        Set<String> keys = axes.keySet();
        return keys.toArray(new String[keys.size()]);
    }

    public Axis getAxis(String name) {
        return axes.get(name);
    }

    public AttributeNode createAttributeNode(Component c, String attributeName) {
        return factory.createAttributeNode(c, attributeName);
    }

    public ComponentNode createComponentNode(Component c) {
        return factory.createComponentNode(c);
    }

    public InterfaceNode createInterfaceNode(Interface itf) {
        return factory.createInterfaceNode(itf);
    }

    public MethodNode createMethodNode(Interface itf, Method meth) {
        return factory.createMethodNode(itf, meth);
    }

    /**
     * Defines a new procedure (function or action) implemented as a Java objets (as
     * opposed to an FScript program).
     */
    public void defineProcedure(Procedure proc) {
        // procedures.put(proc.getName(), new SignatureCheckedProcedure(proc));
        procedures.put(proc.getName(), proc);
    }

    /**
     * Loads one or more procedure definitions and make them available to the interpreter.
     */
    public void loadDefinitions(Reader input) throws FScriptException {
        List newDefs = parseDefinitions(input);
        for (Iterator iter = newDefs.iterator(); iter.hasNext();) {
            UserDefinedProcedure proc = (UserDefinedProcedure) iter.next();
            defineProcedure(proc);
        }
    }

    /**
     * Evaluates an FPath expression.
     *
     * @param expr
     *            the code of the expression.
     * @param vars
     *            local variables to use during the evaluation (mapping from names to
     *            values).
     * @return the value of the expression.
     * @throws FScriptException
     */
    public Object evaluate(String expr, Map<String, Object> vars) throws FScriptException {
        return evaluate(parseExpression(expr), vars);
    }

    /**
     * Evaluates an FPath expression.
     *
     * @param expr
     *            the pre-parsed expression (see {@link #parseExpression(String)}).
     * @param vars
     *            local variables to use during the evaluation (mapping from names to
     *            values).
     * @return the value of the expression.
     * @throws FScriptException
     */
    public Object evaluate(Expression expr, Map<String, Object> vars)
            throws FScriptException {
        return expr.evaluate(new Environment(toplevel, vars));
    }

    public Object evaluateFrom(String expr, Node startNode) throws FScriptException {
        Expression parsedExpr = parseExpression(expr);
        Environment env = new Environment(toplevel, null);
        env.setCurrentNode(startNode);
        return parsedExpr.evaluate(env);
    }

    public Object evaluateFrom(Expression expr, Node startNode) throws FScriptException {
        Environment env = new Environment(toplevel, null);
        env.setCurrentNode(startNode);
        return expr.evaluate(env);
    }

    public Object evaluateFrom(String expr, Component start) throws FScriptException {
        return evaluateFrom(expr, factory.createComponentNode(start));
    }

    public Object evaluateFrom(String expr, Interface start) throws FScriptException {
        return evaluateFrom(expr, factory.createInterfaceNode(start));
    }

    public Component findComponent(String query, Component start) throws FScriptException {
        return ((ComponentNodeImpl) findNode(query, factory.createComponentNode(start)))
                .getComponent();
    }

    public Interface findInterface(String query, Component start) throws FScriptException {
        return ((InterfaceNodeImpl) findNode(query, factory.createComponentNode(start)))
                .getInterface();
    }

    @SuppressWarnings("unchecked")
    private Node findNode(String query, Node start) throws FScriptException {
        Object result = evaluateFrom(query, start);
        if (result instanceof Set) {
            Set<Node> nodes = (Set<Node>) result;
            if (nodes.isEmpty()) {
                return null;
            } else {
                return nodes.iterator().next();
            }
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Set<Node> findAllNodes(String query, Node start) throws FScriptException {
        Object result = evaluateFrom(query, start);
        if (result instanceof Set) {
            return (Set<Node>) result;
        } else {
            return null;
        }
    }

    /**
     * Applies an procedure by name.
     *
     * @param procName
     *            the name of the procedure, e.g. "bind"
     * @param args
     *            the arguments of the invocation.
     * @return the return value of the procedure call.
     * @throws FScriptException
     */
    public synchronized Object apply(String procName, Object[] args)
            throws FScriptException {
        try {
            Procedure proc = toplevel.getProcedure(procName);
            ReconfigurationManager.getInstance().begin();
            Object result = proc.apply(args, toplevel);
            ReconfigurationManager.getInstance().commit();
            return result;
        } catch (Exception th) {
            ReconfigurationManager.getInstance().cancel();
            throw new FScriptException(th);
        }
    }

    /**
     * Executes a pre-parsed statement.
     *
     * @param stat
     * @param vars
     * @return
     * @throws FScriptException
     */
    public synchronized Object execute(Statement stat, Map<String, Object> vars)
            throws FScriptException {
        try {
            Environment env = toplevel;
            if (vars != null) {
                env = new Environment(toplevel, vars);
            }
            ReconfigurationManager.getInstance().begin();
            stat.execute(env);
            ReconfigurationManager.getInstance().commit();
            return env.getReturnValue();
        } catch (Throwable th) {
            ReconfigurationManager.getInstance().cancel();
            throw new FScriptException(th);
        }
    }

    /**
     * Parses an FScript expression.
     *
     * @param expr
     *            the textual definition of the expression.
     * @return the parsed expression.
     * @throws FScriptException
     *             if the expression is invalid (incorrect syntax for example).
     */
    public Expression parseExpression(String expr) throws FScriptException {
        FScriptLexer lexer = new FScriptLexer(new StringReader(expr));
        FScriptParser1 parser = new FScriptParser1(lexer);
        try {
            return parser.expression();
        } catch (TokenStreamException e) {
            throw new FScriptException("Syntax error.", e);
        } catch (RecognitionException e) {
            throw new FScriptException("Syntax error.", e);
        }
    }

    /**
     * Parses an FScript statement.
     *
     * @param stat
     *            the textual definition of the statement.
     * @return the parsed statement.
     * @throws FScriptException
     *             if the statement is invalid (incorrect syntax for example).
     */
    public Statement parseStatement(String stat) throws FScriptException {
        FScriptLexer lexer = new FScriptLexer(new StringReader(stat));
        FScriptParser1 parser = new FScriptParser1(lexer);
        try {
            return parser.statement();
        } catch (TokenStreamException e) {
            throw new FScriptException("Syntax error.", e);
        } catch (RecognitionException e) {
            throw new FScriptException("Syntax error.", e);
        }
    }

    private List parseDefinitions(Reader input) throws FScriptException {
        FScriptLexer lexer = new FScriptLexer(input);
        FScriptParser1 parser = new FScriptParser1(lexer);
        try {
            return parser.definitions();
        } catch (TokenStreamException e) {
            throw new FScriptException("Syntax error.", e);
        } catch (RecognitionException e) {
            throw new FScriptException("Syntax error.", e);
        }
    }

    public Map getContext() {
        return context;
    }
}
