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
package org.objectweb.fractal.fscript.reconfiguration;

import java.util.Map;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;

/**
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class InstanciateReconfiguration extends AbstractReconfiguration {
    private final Factory factory;

    private final String template;

    private final Map<String, Object> context;

    public InstanciateReconfiguration(Factory factory, String template,
            Map<String, Object> context) {
        this.factory = factory;
        this.template = template;
        this.context = context;
    }

    @Override
    protected Object apply(Transaction tx) throws ADLException {
        return factory.newComponent(template, context);
    }
}
