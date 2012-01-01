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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.ScriptLoader;

/**
 * This command loads new FScript procedures definitions from a file.
 * 
 * @author Pierre-Charles David
 */
public class LoadCommand extends AbstractCommand {
    private ScriptLoader loader;

    @Override
    public void setFScriptEngine(Component newFscript) {
        super.setFScriptEngine(newFscript);
        loader = getEngineInterface("loader", ScriptLoader.class);
    }

    public void execute(String args) throws Exception {
        Reader reader = null;
        if (args.startsWith("classpath:"))
            reader = getResourceReader(args.substring("classpath:".length()));
        else if (args.matches("^[a-z]+:.*"))
            reader = getURLReader(args);
        else {
            reader = getFileReader(args);
        }
        if (reader == null) {
            return;
        }
        loader.load(reader);
    }

    private Reader getResourceReader(String resource) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(resource);
        if (is != null) {
            return new InputStreamReader(is);
        } else {
            showError("Not such resource in the classpath: " + resource + ".");
            return null;
        }
    }

    private Reader getURLReader(String url) {
        try {
            InputStream is = new URL(url).openStream();
            return new InputStreamReader(is);
        } catch (MalformedURLException e) {
            showError("Invalid URL (" + url + "): " + e.getMessage());
            return null;
        } catch (IOException e) {
            showError("Unable to open a connection on this URL (" + url + "): "
                    + e.getMessage());
            return null;
        }
    }

    private Reader getFileReader(String fileName) {
        try {
            return new FileReader(fileName);
        } catch (FileNotFoundException e) {
            showError("Unable to open file " + e.getMessage());
            return null;
        }
    }
}
