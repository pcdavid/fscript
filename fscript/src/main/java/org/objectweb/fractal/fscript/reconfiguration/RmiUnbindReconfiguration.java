/*
 * Copyright (c) 2007 ARMINES
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

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.rmi.registry.NamingService;

public class RmiUnbindReconfiguration extends AbstractReconfiguration {
    private final NamingService namingService;
    private final String name;
    
    public RmiUnbindReconfiguration(NamingService ns, String name) {
        this.namingService = ns;
        this.name = name;
    }

    @Override
    protected Object apply(Transaction tx) throws Throwable {
        Component previous = namingService.unbind(name);
        if (previous != null) {
            tx.registerUndo(new RmiBindReconfiguration(namingService, name, previous));
        }
        return null;
    }

}
