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
package org.objectweb.fractal.fscript.model.jade;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.AttributesHelper;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;

public class JadeModel extends FractalModel implements JadeNodeFactory {
    @Override
    protected void createAxes() {
        super.createAxes();
        addAxis(new GenericAttributeAxis(this));
        addAxis(new DualAxis(this));
    }

    @Override
    protected void createAdditionalProcedures() {
        super.createAdditionalProcedures();

        DeployAction deploy = new DeployAction();
        try {
            deploy.bindFc("jade-model", this);
        } catch (NoSuchInterfaceException e) {
            throw new AssertionError("Internal inconsistency with DeployAction.");
        }
        addProcedure(deploy);

        UndeployAction undeploy = new UndeployAction();
        try {
            undeploy.bindFc("jade-model", this);
        } catch (NoSuchInterfaceException e) {
            throw new AssertionError("Internal inconsistency with UndeployAction.");
        }
        addProcedure(undeploy);

    }

    public GenericAttributeNode createGenericAttributeNode(Component comp, String attrName) {
        return new GenericAttributeNode(this, new AttributesHelper(comp), attrName);
    }
}
