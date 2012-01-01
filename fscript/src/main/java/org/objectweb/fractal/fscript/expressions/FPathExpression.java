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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.nodes.Node;

/**
 * Represents an absolute FPath.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class FPathExpression extends AbstractExpression {
    private final Expression initial;

    private final List<Step> steps;

    /**
     * Creates a new <code>FPathExpression</code>.
     * 
     * @param start
     *            the expression denoting the root of the path.
     * @param steps
     *            the steps consituting the path.
     */
    public FPathExpression(Expression start, List<Step> steps) {
        if (start == null) {
            throw new IllegalArgumentException("Missing initial expression.");
        }
        if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("Missing steps.");
        }
        this.initial = start;
        this.steps = new ArrayList<Step>(steps);
    }

    public Object evaluate(Environment env) throws FScriptException {
        checkEnvironment(env);
        Set<Node> nodeSet = getInitialSet(env);
        for (Step element : steps) {
            nodeSet = element.follow(nodeSet, env);
        }
        return nodeSet;
    }

    /**
     * Computes the initial nodeset.
     * 
     * @param env
     *            the dynamic evaluation environment.
     * @return the initial nodeset.
     */
    @SuppressWarnings("unchecked")
    private Set<Node> getInitialSet(Environment env) throws FScriptException {
        Object start = initial.evaluate(env);
        Set<Node> nodeSet = new HashSet<Node>();
        if (start instanceof Node) {
            nodeSet.add((Node) start);
        } else if (start instanceof Node[]) {
            for (Node node : (Node[]) start) {
                nodeSet.add(node);
            }
        } else if (start instanceof Set) {
            nodeSet.addAll((Set<Node>) start);
        } else {
            throw new RuntimeException();
        }
        return nodeSet;
    }
}
