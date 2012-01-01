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
package org.objectweb.fractal.fscript.console;

import org.objectweb.fractal.api.Component;

/**
 * This interface is implemented by the console's special commands, which are not part of the
 * FScript language.
 * 
 * @author Pierre-Charles David
 */
public interface Command {
    /**
     * The name through which the command can be invoked ("<code>:<i>cmdname</i> [<i>arguments</i>]").
     */
    String getName();
    
    /**
     * A short (one line) description of what the command does.
     */
    String getShortDescription();

    /**
     * A longer (multi-line) description of the command. This should include the full
     * syntax of the arguments (if any).
     */
    String getLongDescription();

    /**
     * Sets the name of the command.
     */
    void setName(String name);
    
    /**
     * Sets the short description of the command (should be on a single line).
     */
    void setShortDescription(String desc);
    
    /**
     * Sets the long description of the command (can span multiple lines).
     */
    void setLongDescription(String desc);
    
    /**
     * Executes the command, with the corresponding arguments.
     * 
     * @param args
     *            the arguments passed to the command, i.e. everything after the command invocation
     *            itself (<code>":<i>cmdname</i>"</code>). The arguments are passed as-is; the
     *            command is responsible for their parsing and interpretation.
     */
    void execute(String args) throws Exception;
    
    /**
     * Sets the session to use to interact with the user.
     * 
     * @param session the session to use to report information to the user
     */
    void setSession(Session session);

    /**
     * Sets the actual FScript implementation component that the command should use.
     * 
     * @throws IllegalArgumentException
     *             if the supplied component is not a compatible FScript implementation.
     */
    void setFScriptEngine(Component fscript);
}
