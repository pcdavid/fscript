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
package org.objectweb.fractal.fscript.interpreter;

import java.util.HashMap;
import java.util.Map;

import net.jcip.annotations.NotThreadSafe;

import org.objectweb.fractal.fscript.model.Node;

/**
 * Represents the dynamic context in which script execution occurs, including global
 * variables and the current node.
 * 
 * @author Pierre-Charles David
 */
@NotThreadSafe
public class Context {
    private static final String CURRENT_NODE = "*current*";

    private final Map<String, Object> globals;

    private final Map<String, Object> locals;

    public Context(Context parent) {
        this.globals = parent.globals;
        this.locals = new HashMap<String, Object>();
    }

    public Context(Map<String, Object> globals) {
        this.globals = globals;
        this.locals = new HashMap<String, Object>();
    }
    
    public Object get(String var) {
        Object local = getLocal(var);
        return (local != null) ? local : getGlobal(var);
    }
    
    public void set(String var, Object val) {
        if (locals.containsKey(var)) {
            setLocal(var, val);
        } else {
            setGlobal(var, val);
        }
    }
    
    public Object getLocal(String var) {
        return locals.get(var);
    }
    
    public void setLocal(String var, Object val) {
        locals.put(var, val);
    }
    
    public Object getGlobal(String var) {
        return globals.get(var);
    }
    
    public void setGlobal(String var, Object val) {
        globals.put(var, val);
    }

    public Node getCurrentNode() {
        return (Node) getGlobal(CURRENT_NODE);
    }

    public void setCurrentNode(Node node) {
        setGlobal(CURRENT_NODE, node);
    }
}
