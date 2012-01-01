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
package org.objectweb.fractal.fscript.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.ast.ASTNode;
import org.objectweb.fractal.fscript.ast.ASTVisitor;
import org.objectweb.fractal.fscript.ast.Assignment;
import org.objectweb.fractal.fscript.ast.Block;
import org.objectweb.fractal.fscript.ast.Call;
import org.objectweb.fractal.fscript.ast.Conditional;
import org.objectweb.fractal.fscript.ast.Conjunction;
import org.objectweb.fractal.fscript.ast.Constant;
import org.objectweb.fractal.fscript.ast.Disjunction;
import org.objectweb.fractal.fscript.ast.ExplicitReturn;
import org.objectweb.fractal.fscript.ast.LocationPath;
import org.objectweb.fractal.fscript.ast.LocationStep;
import org.objectweb.fractal.fscript.ast.Loop;
import org.objectweb.fractal.fscript.ast.SourceLocation;
import org.objectweb.fractal.fscript.ast.UserProcedure;
import org.objectweb.fractal.fscript.ast.VariableReference;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;

import com.google.common.base.Objects;

public class EvaluatingVisitor implements ASTVisitor {
    /**
     * This exception is thrown when the interpreter executes a <code>return</code>
     * statement, to end the execution of the current procedure.
     */
    private class NonLocalExit extends RuntimeException {
    }

    private static final boolean INLINE_COMMON_FUNCTIONS = false;

    private final Interpreter interpreter;

    private SourceLocation currentLocation;

    private Object currentValue;

    private Context context;

    public EvaluatingVisitor(Interpreter inter, Context context) {
        this.interpreter = inter;
        this.context = context;
    }

    public SourceLocation getCurrentLocation() {
        return currentLocation;
    }

    public Object evaluate(ASTNode script) {
        try {
            script.accept(this);
        } catch (NonLocalExit nle) {
            // Explicit return.
        }
        return currentValue;
    }

    public void visitAssignement(Assignment assign) {
        currentLocation = assign.getSourceLocation();
        assign.getValueExpression().accept(this);
        context.set(assign.getVariableName(), currentValue);
    }

    public void visitBlock(Block block) {
        currentLocation = block.getSourceLocation();
        for (ASTNode step : block.getSteps()) {
            step.accept(this);
        }
    }

    public void visitCall(Call call) {
        currentLocation = call.getSourceLocation();
        String procName = call.getProcedureName();
        Object[] args = evaluateArguments(call.getArguments());
        if (INLINE_COMMON_FUNCTIONS) {
            if ("current".equals(procName)) {
                currentValue = context.getCurrentNode();
            } else if ("name".equals(procName)) {
                currentValue = ((Node) args[0]).getProperty("name");
            } else if ("==".equals(procName)) {
                currentValue = Objects.equal(args[0], args[1]);
            } else {
                try {
                    currentValue = interpreter.apply(procName, args, context);
                } catch (ScriptExecutionError e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                currentValue = interpreter.apply(procName, args, context);
            } catch (ScriptExecutionError e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object[] evaluateArguments(List<ASTNode> arguments) {
        Object[] result = new Object[arguments.size()];
        int i = 0;
        for (ASTNode node : arguments) {
            node.accept(this);
            result[i++] = currentValue;
        }
        return result;
    }

    public void visitConditional(Conditional cond) {
        currentLocation = cond.getSourceLocation();
        cond.getTestExpression().accept(this);
        if (asBoolean(currentValue)) {
            cond.getTrueBranch().accept(this);
        } else if (cond.getFalseBranch() != null) {
            cond.getFalseBranch().accept(this);
        }
    }

    public void visitConjunction(Conjunction conj) {
        currentLocation = conj.getSourceLocation();
        for (ASTNode clause : conj.getClauses()) {
            clause.accept(this);
            if (!asBoolean(currentValue)) {
                break;
            }
        }
    }

    public void visitConstant(Constant constant) {
        currentLocation = constant.getSourceLocation();
        currentValue = constant.getValue();
    }

    public void visitDisjunction(Disjunction dis) {
        currentLocation = dis.getSourceLocation();
        for (ASTNode clause : dis.getClauses()) {
            clause.accept(this);
            if (asBoolean(currentValue)) {
                break;
            }
        }
    }

    public void visitExplicitReturn(ExplicitReturn ret) {
        currentLocation = ret.getSourceLocation();
        ASTNode expr = ret.getValueExpression();
        if (expr != null) {
            expr.accept(this);
        } else {
            currentValue = null;
        }
        throw new NonLocalExit();
    }

    @SuppressWarnings("unchecked")
    public void visitLocationPath(LocationPath path) {
        currentLocation = path.getSourceLocation();
        path.getAnchor().accept(this);
        for (ASTNode step : path.getSteps()) {
            if (asNodeSet(currentValue).isEmpty()) {
                break;
            }
            step.accept(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void visitLocationStep(LocationStep step) {
        currentLocation = step.getSourceLocation();
        Set<Node> sourceNodes = asNodeSet(currentValue);
        Set<Node> selectedNodes = new HashSet<Node>();
        NativeProcedure axisFunction = interpreter.getAxisFunction(step.getAxisName());
        for (Node node : sourceNodes) {
            selectedNodes.addAll(follow(axisFunction, node));
        }
        List<ASTNode> filters = new ArrayList<ASTNode>();
        if (step.getTest() != null) {
            filters.add(step.getTest());
        }
        filters.addAll(step.getPredicates());
        currentValue = filter(selectedNodes, filters);
    }

    @SuppressWarnings("unchecked")
    private Set<Node> follow(NativeProcedure axis, Node source) {
        List<Object> args = Arrays.asList((Object) source);
        try {
            return (Set<Node>) axis.apply(args, context);
        } catch (ScriptExecutionError e) {
            throw new RuntimeException("Error while following axis " + axis.getName());
        }
    }

    private Set<Node> filter(Set<Node> nodes, List<ASTNode> filters) {
        Set<Node> result = new HashSet<Node>();
        for (Node node : nodes) {
            if (passesAllTests(node, filters)) {
                result.add(node);
            }
        }
        return result;
    }

    private boolean passesAllTests(Node node, List<ASTNode> filters) {
        for (ASTNode filter : filters) {
            currentValue = false;
            context.setCurrentNode(node);
            filter.accept(this);
            if (!asBoolean(currentValue)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Set<Node> asNodeSet(Object obj) {
        if (obj instanceof Set) {
            return (Set<Node>) obj;
        } else if (obj instanceof Node) {
            return Collections.singleton((Node) obj);
        } else {
            throw new RuntimeException("Type error.");
        }
    }

    @SuppressWarnings("unchecked")
    public void visitLoop(Loop loop) {
        currentLocation = loop.getSourceLocation();
        loop.getIterationExpression().accept(this);
        for (Node node : asNodeSet(currentValue)) {
            context.setLocal(loop.getIterationVariableName(), node);
            loop.getBody().accept(this);
        }
    }

    public void visitProcedure(UserProcedure proc) {
        throw new AssertionError("Procedure definitions are not executable.");
    }

    public void visitVariableReference(VariableReference variable) {
        currentLocation = variable.getSourceLocation();
        currentValue = context.get(variable.getVariableName());
    }

    /**
     * Interprets an FScript value as a boolean.
     */
    protected static boolean asBoolean(Object object) {
        if (object instanceof Boolean) {
            return ((Boolean) object).booleanValue();
        } else if (object instanceof String) {
            return ((String) object).length() > 0;
        } else if (object instanceof Number) {
            return ((Number) object).doubleValue() != 0.0;
        } else if (object instanceof Set) {
            return !((Set<?>) object).isEmpty();
        } else {
            return false;
        }
    }
}