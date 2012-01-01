/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2008 France Telecom
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

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.InvalidScriptException;
import org.objectweb.fractal.fscript.ast.ASTNode;
import org.objectweb.fractal.fscript.ast.Call;
import org.objectweb.fractal.fscript.ast.Conjunction;
import org.objectweb.fractal.fscript.ast.Constant;
import org.objectweb.fractal.fscript.ast.Disjunction;
import org.objectweb.fractal.fscript.ast.VariableReference;

public class FPathParserTest {
    private IFPathParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new FPathParserImpl();
    }

    public ASTNode parse(String src) {
        try {
            return parser.parseExpression(new StringReader(src));
        } catch (InvalidScriptException e) {
            fail("Invalid script: " + e.getMessage());
            return null; // Keep the compiler happy.
        }
    }

    /**
     * Checks that a given source string is parsed as expected.
     * 
     * @param expected
     *            the AST which should be produced from the source.
     * @param source
     *            an FPath expression.
     */
    public void assertAST(ASTNode expected, String source) {
        assertEquals(expected, parse(source));
    }

    /**
     * Checks that a given invalid source is correctly rejected by the parser.
     * 
     * @param source
     *            an incorrect FPath expression.
     */
    public void assertSyntaxError(String source) {
        try {
            parser.parseExpression(new StringReader(source));
            fail("Invalid syntax should be rejected.");
        } catch (InvalidScriptException e) {
            // OK, error detected.
        }
    }

    @Test
    public void litteralNumber() {
        assertAST(new Constant(null, 1.0), "1");
        assertAST(new Constant(null, 1.0), "1.0");
        assertAST(new Constant(null, 1.0), "1.0000000");
        assertAST(new Constant(null, 1.0), "  1.0\t");
    }

    @Test
    public void litteralString() {
        assertAST(new Constant(null, ""), "''");
        assertAST(new Constant(null, ""), "\"\"");
        assertAST(new Constant(null, " "), "' '");
    }

    @Test
    public void litteralStringWithEscapedQuotes() {
        // A double-quote in double-quoted string
        assertAST(new Constant(null, "\""), "\"\\\"\"");
        // A single-quote in double-quoted string
        assertAST(new Constant(null, "'"), "\"\\'\"");
        // A double-quote in single-quoted string
        assertAST(new Constant(null, "\""), "'\\\"'");
        // A single-quote in single-quoted string
        assertAST(new Constant(null, "'"), "'\\''");
    }

    @Test
    public void litteralStringWithEscapedChars() {
        // Tab in double-quoted string
        assertAST(new Constant(null, "\t"), "\"\\t\"");
        // Tab in single-quoted string
        assertAST(new Constant(null, "\t"), "'\\t'");
        // Newline in double-quoted string
        assertAST(new Constant(null, "\n"), "\"\\n\"");
        // Newline in single-quoted string
        assertAST(new Constant(null, "\n"), "'\\n'");
        // Newline in single-quoted string
        assertAST(new Constant(null, "\r"), "\"\\r\"");
        // CR in single-quoted string
        assertAST(new Constant(null, "\r"), "'\\r'");
    }

    @Test
    public void badLitteralString() {
        assertSyntaxError("\"");
        assertSyntaxError("'");
        assertSyntaxError("\"'");
        assertSyntaxError("\"foo\nbar\""); // Multi-line litteral string.
    }

    @Test
    public void badSingleIdentifier() {
        assertSyntaxError("foo");
    }

    @Test
    public void variableReference() {
        assertAST(new VariableReference(null, "foo"), "$foo");
    }

    @Test
    public void conjunction() {
        Conjunction conj = new Conjunction(null, Arrays.asList((ASTNode) new VariableReference(
                null, "foo"), new VariableReference(null, "bar")));
        assertAST(conj, "$foo && $bar");
        assertAST(conj, "$foo&& $bar");
        assertAST(conj, "$foo &&$bar");
    }

    @Test
    public void disjunction() {
        Disjunction dis = new Disjunction(null, Arrays.asList((ASTNode) new VariableReference(null,
                "foo"), (ASTNode) new VariableReference(null, "bar")));
        assertAST(dis, "$foo || $bar");
        assertAST(dis, "$foo|| $bar");
        assertAST(dis, "$foo ||$bar");
    }

    @Test
    public void call() {
        List<ASTNode> noArgs = new ArrayList<ASTNode>();
        assertAST(new Call(null, "foo", noArgs), "foo()");
        assertAST(new Call(null, "foo", Arrays.asList((ASTNode) new Call(null, "bar", noArgs))),
                "foo(bar())");
        assertAST(new Call(null, "foo", Arrays.asList((ASTNode) new Call(null, "bar", noArgs),
                new Call(null, "baz", noArgs))), "foo(bar(), baz())");
    }
}
