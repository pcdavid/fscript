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
package org.objectweb.fractal.fscript;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class FScriptClassLoader extends URLClassLoader {

    public final static String FILE_PROTOCOL = "file";

    public FScriptClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public void addJar(String path) throws IOException {
        File jarFile = new File(path);
        if (jarFile.exists()) {
            addURL(jarFile.toURI().toURL());
        } else {
            throw new IllegalArgumentException("File " + jarFile + " does not exist.");
        }
    }
}
