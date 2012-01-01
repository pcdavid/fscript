/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.console;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.control.NameController;
import org.objectweb.fractal.fscript.model.fractal.FractalModelAttributes;
import org.objectweb.fractal.util.Fractal;

/**
 * This command adds an URL at the front of the class-path used by FScript (especially the
 * embedded ADL Factory).
 * 
 * @author Pierre-Charles David
 */
public class ClassPathAddCommand extends AbstractCommand {
    public void execute(String args) throws Exception {
        URL url = null;
        try {
            url = new URL(args);
        } catch (MalformedURLException e) {
            showError("Invalid URL (" + args + "): " + e.getMessage());
            return;
        }
        Map<String, Object> ctx = getInstanciationContext();
        ClassLoader parent = null;
        if (ctx.containsKey("classloader")) {
            parent = (ClassLoader) ctx.get("classloader");
        } else {
            parent = Thread.currentThread().getContextClassLoader();
        }
        ClassLoader cl = new URLClassLoader(new URL[] { url }, parent);
        ctx.put("classloader", cl);
        showMessage("Classpath updated.");
    }

    private Map<String, Object> getInstanciationContext() throws Exception {
        ContentController cc = Fractal.getContentController(fscript);
        for (Component child : cc.getFcSubComponents()) {
            try {
                NameController nc = Fractal.getNameController(child);
                if ("model".equals(nc.getFcName())) {
                    FractalModelAttributes fma = (FractalModelAttributes) Fractal.getAttributeController(child);
                    return fma.getInstanciationContext();
                }
            } catch (NoSuchInterfaceException nsie) {
                continue;
            }
        }
        return null;
    }
}
