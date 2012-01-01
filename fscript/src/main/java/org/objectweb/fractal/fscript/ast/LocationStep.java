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
package org.objectweb.fractal.fscript.ast;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.List;

import net.jcip.annotations.Immutable;


import com.google.common.base.Join;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * Represents a single step in an FPath path.
 * 
 * @see LocationPath
 * @author Pierre-Charles David
 */
@Immutable
public class LocationStep extends CompositeNode<ASTNode> {
    private final String axisName;

    private final ASTNode test;

    /**
     * Creates a new <code>LocationStep</code> AST node.
     * 
     * @param location
     *            the location of the step, including all the predicates, in the source code. May be
     *            null.
     * @param axisName
     *            the name of the axis to follow.
     * @param test
     *            the test on node names. May be null (this corresponds to the use of <code>*</code>
     *            in the source).
     * @param predicates
     *            the sequence of predicates to filter the candidate nodes. May be empty but not
     *            null.
     */
    public LocationStep(final SourceLocation location, final String axisName, final ASTNode test,
            final List<ASTNode> predicates) {
        super(location, "step", predicates);
        checkNotNull(axisName, "Missing axis name.");
        Preconditions.checkArgument(axisName.length() != 0, "Invalid axis name (empty).");
        this.axisName = axisName;
        this.test = test;
    }

    public String getAxisName() {
        return axisName;
    }

    public ASTNode getTest() {
        return test;
    }

    public List<ASTNode> getPredicates() {
        return operands;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitLocationStep(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(axisName, test, operands);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals(obj)) {
            return false;
        } else if (obj instanceof LocationStep) {
            final LocationStep other = (LocationStep) obj;
            return this.axisName.equals(other.axisName) && Objects.equal(this.test, other.test);
        } else {
            return false;
        }
    }

    @Override
    public void toString(Appendable app) throws IOException {
        app.append("(step ");
        app.append(axisName);
        app.append(" (test ");
        if (test != null) {
            test.toString(app);
        } else {
            app.append("*");
        }
        app.append(") ");
        Join.join(app, " ", " ", operands);
        app.append(")");
    }
}
