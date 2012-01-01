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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.Reader;

import net.jcip.annotations.ThreadSafe;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.objectweb.fractal.fscript.InvalidScriptException;
import org.objectweb.fractal.fscript.ast.ASTNode;
import org.objectweb.fractal.fscript.ast.SourceLocation;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;

/**
 * Default implementation of {@link IFPathParser} using an ANTLR parser.
 * 
 * @author Pierre-Charles David
 */
@ThreadSafe
public class FPathParserImpl implements IFPathParser {
    public ASTNode parseExpression(Reader source) throws InvalidScriptException {
        checkNotNull(source, "No source provided");
        try {
            FScriptParser parser = createASTBuilder(source);
            return parser.expression();
        } catch (RecognitionException e) {
            throw wrappedLexicalError(e);
        } catch (IOException e) {
            Diagnostic err = Diagnostic.error(SourceLocation.UNKNOWN, e.getMessage());
            throw new InvalidScriptException(err);
        }
    }

    /**
     * Creates an ANTLR parser for the specified source.
     * 
     * @throws IOException
     */
    protected FScriptParser createASTBuilder(Reader source) throws IOException {
        FScriptLexer lexer = new FScriptLexer(new ANTLRReaderStream(source));
        return new FScriptParser(new CommonTokenStream(lexer));
    }

    /**
     * Wraps an ANTLR-specific lexical error into an FScript <code>InvalidScriptException</code>.
     */
    protected InvalidScriptException wrappedLexicalError(RecognitionException e) {
        SourceLocation loc;
        if (e.line == 0 && e.charPositionInLine == -1) {
            // 0:-1 is how ANTLR encodes EOF, meaning the error happened at the end
            loc = SourceLocation.UNKNOWN; // FIXME How to represent EOF using SourceLocations?
        } else {
            loc = new SourceLocation(e.input, e.line, e.charPositionInLine + 1);
        }
        Diagnostic diag = Diagnostic.error(loc, e.toString());
        return new InvalidScriptException(diag);
    }
}
