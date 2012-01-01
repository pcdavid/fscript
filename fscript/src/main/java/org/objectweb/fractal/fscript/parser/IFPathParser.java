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
package org.objectweb.fractal.fscript.parser;

import java.io.Reader;

import org.objectweb.fractal.fscript.InvalidScriptException;
import org.objectweb.fractal.fscript.ast.ASTNode;

/**
 * Parses stand-alone FPath expressions into abstract syntax trees.
 * 
 * @author Pierre-Charles David
 */
public interface IFPathParser {
    ASTNode parseExpression(Reader source) throws InvalidScriptException;
}
