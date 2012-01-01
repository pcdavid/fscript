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
package org.objectweb.fractal.fscript.expressions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.Values;
import org.objectweb.fractal.fscript.axes.Axis;
import org.objectweb.fractal.fscript.axes.Predicate;
import org.objectweb.fractal.fscript.nodes.Node;

/**
 * Represents a step in an FPath.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class Step {
    private final String axisName;

    private final Expression predicate;

    /**
     * Creates a new <code>Step</code>.
     * 
     * @param axisName
     *            the name of the axis.
     * @param test
     *            the test (a name or "*")
     * @param predicates
     *            the optional list of predicates
     */
    public Step(String axisName, List<Expression> predicates) {
        if (axisName == null) {
            throw new IllegalArgumentException("Missing axis name.");
        }
        this.axisName = axisName;
        if (predicates == null || predicates.isEmpty()) {
            this.predicate = new ConstantExpression(true);
        } else {
            this.predicate = new AndExpression(predicates);
        }
    }

    /**
     * Follows this step.
     * 
     * @param nodeSet
     *            the initial nodeset.
     * @param env
     *            the evaluation environment.
     * @return the resulting nodeset.
     */
    public Set<Node> follow(Set<Node> nodeSet, final Environment env) throws FScriptException {
        Set<Node> candidates = new HashSet<Node>();
        Axis axis = env.getAxis(axisName);
        for (Node element : nodeSet) {
            candidates.addAll(axis.selectFrom(element));
        }
        Predicate<Node> predicates = new NodePredicateImpl(env);
        Set<Node> result = new HashSet<Node>();
        for (Node node : candidates) {
            if (predicates.matches(node)) {
                result.add(node);
            }
        }
        return result;
    }

    private class NodePredicateImpl implements Predicate<Node> {
        private Environment env;

        public NodePredicateImpl(Environment env) {
            this.env = env;
        }

        public boolean matches(Node node) {
            try {
                env.setCurrentNode(node);
                boolean result = Values.asBoolean(predicate.evaluate(env));
                env.setCurrentNode(null);
                return result;
            } catch (FScriptException e) {
                return false;
            }
        }
    }
}
