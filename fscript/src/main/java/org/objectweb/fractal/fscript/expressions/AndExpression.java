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
import java.util.List;

import org.objectweb.fractal.fscript.Environment;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.Values;

/**
 * Represents a boolean conjunction of an arbitraty number of clauses. If there are no
 * clauses, the expression always evaluate to <code>true</code>. There is no guarantee
 * on the order of evaluation of the clauses, not even that each clause will be evaluated.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class AndExpression extends AbstractExpression {
    /**
     * The clauses of the conjunction, evaluated for their boolean value.
     */
    private final List<Expression> clauses;

    /**
     * Creates a new <code>AndExpression</code>.
     * 
     * @param clauses
     *            the clauses of the conjunction.
     */
    public AndExpression(List<Expression> clauses) {
        if (clauses == null || clauses.isEmpty()) {
            throw new IllegalArgumentException("Missing clauses for conjuntion.");
        }
        this.clauses = new ArrayList<Expression>(clauses);
    }

    public Object evaluate(Environment env) throws FScriptException {
        checkEnvironment(env);
        for (Expression clause : clauses) {
            if (!Values.asBoolean(clause.evaluate(env))) {
                return false;
            }
        }
        return true;
    }
}
