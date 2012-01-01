/*
 * Copyright (c) 2008 ARMINES
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
package org.objectweb.fractal.fscript.model.fractal;

import java.util.Map;

import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.control.AttributeController;

public interface FractalModelAttributes extends AttributeController {
    /**
     * Returns the context parameter used by the primitive operations which create new
     * components using a Fractal ADL factory.
     * 
     * @return the context argument passed to {@link Factory#newComponent(String, Map)} by
     *         primitives like <code>adl-new()</code>.
     */
    Map<String, Object> getInstanciationContext();
}
