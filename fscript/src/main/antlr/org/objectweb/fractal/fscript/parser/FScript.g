// ANTLRv3 grammar for FScript.
// -*- antlr -*-
grammar FScript;

options {
  language = Java;
}

@lexer::header{
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
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript.parser;
}

@parser::header{
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
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

import org.antlr.runtime.Token;

import org.objectweb.fractal.fscript.ast.*;
}

@lexer::members {
    public void reportError(RecognitionException e) {
    // API does not allow to throw e from here, throwing RuntimeException instead
        throw new RuntimeException("Lexing recognition problem", e);
    }
}

@parser::members{
    private Object sourceDocument = null;

    /**
     * This method should be called before starting the parsing.
     */
    public void setSourceDocument(Object doc) {
        this.sourceDocument = doc;
    }

    /**
     * Interprets a string litteral found in FScript source code as a
     * Java <code>String</code> value.
     */
    private String stringValue(String litteral) {
        // Remove enclosing quotes.
        String str = litteral.substring(1, litteral.length() - 1);
        // Replace quoted chars with their actual values. We need two
        // quoting levels for backslashes in the first argument: one
        // for standard Java strings, and one because this parameter
        // is interpreted as a regexp.
        str = str.replaceAll("\\\\n", "\n");
        str = str.replaceAll("\\\\r", "\r");
        str = str.replaceAll("\\\\t", "\t");
        str = str.replaceAll("\\\\'", "'");
        str = str.replaceAll("\\\\\"", "\"");
        return str;
    }

    // These helper functions are used to create appropriate SourceLocation objects from
    // the raw information obtained from ANTLR and lower-level rules results.

    private SourceLocation sourceExtent(Token tok) {
        Preconditions.checkNotNull(tok);
        final int line = tok.getLine();
        final int startCol  = tok.getCharPositionInLine() + 1;
        final int endCol = startCol + tok.getText().length();
        return new SourceLocation(sourceDocument, line, startCol, line, endCol);
    }

    private SourceLocation sourceExtent(Token first, Token last) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(last);
        return sourceExtent(sourceExtent(first), sourceExtent(last));
    }

    private SourceLocation sourceExtent(ASTNode first, ASTNode last) {
        Preconditions.checkNotNull(first);
        if (last != null) {
            return sourceExtent(first.getSourceLocation(), last.getSourceLocation());
        } else {
            return first.getSourceLocation();
        }
    }

    private SourceLocation sourceExtent(List<ASTNode> nodes) {
        Preconditions.checkNotNull(nodes);
        Preconditions.checkArgument(!nodes.isEmpty());
        Preconditions.checkContentsNotNull(nodes);
        return sourceExtent(nodes.get(0), nodes.get(nodes.size() - 1));
    }

    private SourceLocation sourceExtent(SourceLocation first, SourceLocation last) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(last);
        Preconditions.checkArgument(first.getSourceDocument() == last.getSourceDocument(),
                "Incompatible bounds: not from the same document.");
        return new SourceLocation(first.getSourceDocument(), first.getStartLine(), first
                .getStartColumn(), last.getEndLine(), last.getEndColumn());
    }

    protected void mismatch(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype, input);
    }

    protected void recoverFromMismatch(IntStream input, RecognitionException e, BitSet follow)
        throws RecognitionException {
        throw e;
    }
}

@rulecatch{
  catch (RecognitionException re) {
    throw re;
  }
  catch (RuntimeException re) {
    if (re.getCause() instanceof RecognitionException) {
      // Error sent from the parser wrapped in a RuntimeException
      throw (RecognitionException) re.getCause();
    } else {
      throw re;
    }
  }
}

/**
 * Identifiers. Used for variables, procedures and axes names.
 */
ID
    :   LETTER (LETTER | DIGIT | '-')*
    ;

/**
 * Literal numbers.
 */
NUMBER
    :   DIGIT+ ( '.' DIGIT* )?
    ;

/**
 * Literal strings: double or single-quoted with support for standard
 * escape characters.
 */
STRING
    :   DQ_STRING | SQ_STRING
    ;

fragment
DIGIT
    :   '0'..'9'
    ;

fragment
LETTER
    : '\u0041'..'\u005a'
    | '\u005f'
    | '\u0061'..'\u007a'
    | '\u00c0'..'\u00d6'
    | '\u00d8'..'\u00f6'
    | '\u00f8'..'\u00ff'
    | '\u0100'..'\u1fff'
    | '\u3040'..'\u318f'
    | '\u3300'..'\u337f'
    | '\u3400'..'\u3d2d'
    | '\u4e00'..'\u9fff'
    | '\uf900'..'\ufaff'
    ;

/**
 * Double-quoted literal strings.
 */
fragment
DQ_STRING
    :   ('"'  (ESC | ~('\\'|'"'|'\n'))* '"' )
    ;

/**
 * Single-quoted literal strings.
 */
fragment
SQ_STRING
    :   ('\'' (ESC | ~('\\'|'\''|'\n'))* '\'')
    ;

/**
 * Supported escape sequence in literal strings.
 */
ESC
    :   '\\' ('n' | 'r' | 't' | '\'' | '"')
    ;

/**
 * Whitespace: ignored.
 */
WS
    : (' '|'\t'|'\r'|'\n')+ { skip(); }
    ;

/**
 * Single-line comments: ignored.
 */
SL_COMMENT
    :   '--' (~('\n'|'\r'))* ('\n'|'\r'('\n')?) { skip(); }
    ;

/**
 * An FScript file contains a sequence of procedure definitions.
 */
definitions returns [ List<UserProcedure> result ]
@init {
  result = new ArrayList<UserProcedure>();
}
    :   ( def=definition { result.add($def.result); } )+
    ;

/**
 * A procedure definition, including the signature and the full body.
 */
definition returns [ UserProcedure result ]
@init {
  boolean pure = false;
}
@after {
  SourceLocation loc = sourceExtent(sourceExtent($k), $body.result.getSourceLocation());
  result = new UserProcedure(loc, pure, $name.text, $params.result, $body.result);
}
    :
    (k='function' { pure = true; } | k='action') name=ID '(' params=parameters ')' body=block
    ;

/**
 * An optional sequence of formal parameter names.
 */
parameters returns [ List<String> result ]
@init {
  result = new ArrayList<String>();
}
    :   ( id1=ID { result.add($id1.text); } (',' id2=ID { result.add($id2.text); } )* )?
    ;

/**
 * A block is a sequence of statements enclosed in curly braces.
 */
block returns [ ASTNode result ]
@init {
  List<ASTNode> steps = new ArrayList<ASTNode>();
}
@after {
  final SourceLocation loc = sourceExtent($open, $close);
  result = new Block(loc, steps);
}
    :   open='{' ( step=statement { steps.add($step.result); } )+ close='}'
    ;

/**
 * A single FScript statement.
 */
statement returns [ ASTNode result ]
    :
        // A variable declaration, with an initial value.
       s='var' ID '=' v=expression e=';'
        {
            final SourceLocation loc = sourceExtent($s, $e);
            result = new Assignment(loc, true, $ID.text, $v.result);
        }

        // A variable assignment.
    |   ID '=' v=expression e=';'
        {
            final SourceLocation loc = sourceExtent($ID, $e);
            result = new Assignment(loc, false, $ID.text, $v.result);
        }

        // A single expression used as a statement (for its side-effect).
    |   stat=expression ';'
        {
            result = $stat.result;
        }

        // Explicit return, with optional return value.
    |   s='return' v=expression? e=';'
        {
            final SourceLocation loc = sourceExtent($s, $e);
            result = new ExplicitReturn(loc, $v.result);
        }

        // A conditional statement, with optional 'else' block.
    |   s='if' '(' cond=expression ')' trueBranch=block ('else' falseBranch=block)?
        {
            final ASTNode endNode = (falseBranch != null) ? falseBranch : trueBranch;
            final SourceLocation loc = sourceExtent(sourceExtent($s), endNode.getSourceLocation());
            result = new Conditional(loc, $cond.result, $trueBranch.result, $falseBranch.result);
        }

        // An iteration/looping statement.
    |   s='for' ID ':' values=expression body=block
        {
            final SourceLocation loc = sourceExtent(sourceExtent($s), $body.result.getSourceLocation());
            result = new Loop(loc, $ID.text, $values.result, $body.result);
        }
    ;

/**
 * A top-level expression. The different kinds of expressions are
 * layered according to the precedence rules. This rule can also be
 * considered as the entry-point for the FPath part of the language.
 */
expression returns [ ASTNode result ]
    :   e=orExpression { result = $e.result; }
    ;

/**
 * A disjunction.
 */
orExpression returns [ ASTNode result ]
@init {
  List<ASTNode> clauses = new ArrayList<ASTNode>();
}
@after {
  if (clauses.size() == 1) {
    result = clauses.get(0);
  } else {
    result = new Disjunction(sourceExtent(clauses), clauses);
  }
}
    :      e1=andExpression { clauses.add($e1.result); }
    ( '||' e2=andExpression { clauses.add($e2.result); } )*
    ;

/**
 * A conjunction.
 */
andExpression returns [ ASTNode result ]
@init {
  List<ASTNode> clauses = new ArrayList<ASTNode>();
}
@after {
  if (clauses.size() == 1) {
    result = clauses.get(0);
  } else {
    result = new Conjunction(sourceExtent(clauses), clauses);
  }
}
    :      e1=compExpression { clauses.add($e1.result); }
    ( '&&' e2=compExpression { clauses.add($e2.result); } )*
    ;

/**
 * A comparison.
 */
compExpression returns [ ASTNode result ]
@after {
  if ($e2.result == null) {
    result = $e1.result;
  } else {
    final SourceLocation loc = sourceExtent($e1.result, $e2.result);
    result = new Call(loc, $op.text, Arrays.asList($e1.result, $e2.result));
  }
}
    :   e1=addExpression ( op=('=='|'!='|'<'|'<='|'>'|'>=') e2=addExpression )?
    ;

/**
 * An additive expression.
 */
addExpression returns [ ASTNode result ]
    :              e1=multExpression { result = $e1.result; }
    ( op=('+'|'-') e2=multExpression
    {
        final SourceLocation loc = sourceExtent(result.getSourceLocation(), $e2.result.getSourceLocation());
        result = new Call(loc, $op.text, Arrays.asList(result, $e2.result));
    }
    )*
    ;

/**
 * A multiplicative expression.
 */
multExpression returns [ ASTNode result ]
    :   e1=unaryExpression { result = $e1.result; }
    ( op=('*'|'div') e2=unaryExpression
    {
        final SourceLocation loc = sourceExtent(result.getSourceLocation(), $e2.result.getSourceLocation());
        result = new Call(loc, $op.text, Arrays.asList(result, $e2.result));
    }
    )*
    ;

/**
 * A unary expression.
 */
unaryExpression returns [ ASTNode result ]
    :   s='-' a=atom
        {
            final SourceLocation loc = sourceExtent(sourceExtent($s), $a.result.getSourceLocation());
            result = new Call(loc, "minus", Arrays.asList($a.result));
        }
    |   a1=atom ( op=('|'|'&'|'\\') a2=atom )?
        {
            if ($op == null) {
                result = $a1.result;
            } else {
                final SourceLocation loc = sourceExtent($a1.result.getSourceLocation(), $a2.result.getSourceLocation());
                String func = null;
                if ($op.text.equals("|"))       { func = "union"; }
                else if ($op.text.equals("&"))  { func = "intersection"; }
                else if ($op.text.equals("\\")) { func = "difference"; }
                result = new Call(loc, func, $a1.result, $a2.result);
            }
        }
    ;

/**
 * An atom.
 */
atom returns [ ASTNode result ]
    :
        // A literal value
        l=literal { result = $l.result; }

        // An expression put in parentheses to override the default precedences.
    |   '(' expr=expression ')' { result = $expr.result; }

        // A procedure call, optionally as the starting point of a path.
    |   c=call ('/' p=relativePath )?
        {
            if (p == null) {
                result = $c.result;
            } else {
                final SourceLocation loc = sourceExtent($c.result.getSourceLocation(), sourceExtent($p.result));
                result = new LocationPath(loc, $c.result, $p.result);
            }
        }

        // A variable reference, optionally as the starting point of a path.
    |   v=variable ('/' p=relativePath )?
        {
            if (p == null) {
                result = $v.result;
            } else {
                final SourceLocation loc = sourceExtent($v.result.getSourceLocation(), sourceExtent($p.result));
                result = new LocationPath(loc, $v.result, $p.result);
            }
        }
    ;

/**
 * A path expression, without a starting point.
 */
relativePath returns [ List<ASTNode> result ]
@init {
  result = new ArrayList<ASTNode>();
}
    :    step1=locationStep { result.add($step1.result); }
    ('/' step2=locationStep { result.add($step2.result); } )*
    ;

/**
 * A variable reference.
 */
variable returns [ ASTNode result ]
    :   s='$' ID { result = new VariableReference(sourceExtent($s, $ID), $ID.text); }
    ;

/**
 * A single step in a path expression.
 */
locationStep returns [ ASTNode result ]
    :
       // Normal form. Axis defaults to "child::" if not specified.
       (ID '::')? t=test p=predicates

       {
            SourceLocation loc = null;
	    String axis = "child";
            if ($ID != null) {
                axis = $ID.text;
                loc = sourceExtent(sourceExtent($ID), $t.result.getSourceLocation());
            } else {
                loc = $t.result.getSourceLocation();
            }
            if (!$p.result.isEmpty()) {
                loc = sourceExtent(loc, sourceExtent($p.result));
            }
            result = new LocationStep(loc, axis, $t.result, $p.result);
       }

       // Shortcut for "attribute::" axis.
    |  a='@' t=test p=predicates

        {
            SourceLocation loc = sourceExtent(sourceExtent($a), $t.result.getSourceLocation());
            if (!$p.result.isEmpty()) {
                loc = sourceExtent(loc, sourceExtent($p.result));
            }
            result = new LocationStep(loc, "attribute", $t.result, $p.result);
        }

       // Shortcut for "parent::" axis, but only in the form "parent::*"
    |  a='..' p=predicates

        {
            SourceLocation loc = sourceExtent($a);
            if (!$p.result.isEmpty()) {
              loc = sourceExtent(loc, sourceExtent($p.result));
            }
            result = new LocationStep(loc, "parent", null, $p.result);
        }

       // Shortcut for "descendant-or-self::" axis.
    |  a='/' t=test p=predicates

        {
            SourceLocation loc = sourceExtent(sourceExtent($a), $t.result.getSourceLocation());
            if (!$p.result.isEmpty()) {
                loc = sourceExtent(loc, sourceExtent($p.result));
            }
            result = new LocationStep(loc, "descendant-or-self", $t.result, $p.result);
        }
    ;

/**
 * An optional sequence of predicates.
 */
predicates returns [ List<ASTNode> result ]
@init {
  result = new ArrayList<ASTNode>();
}
    :   ('[' pred=expression { result.add($pred.result); } ']')*
    ;

/**
 * The test part of a location step.
 */
test returns [ ASTNode result ]
@init{
  ASTNode nameExpr = null;
  SourceLocation loc = null;
}
@after{
    if (nameExpr != null) {
        loc = nameExpr.getSourceLocation();
        Call currentCall = new Call(loc, "current");
        Call nameCall = new Call(loc, "name", currentCall);
        result = new Call(loc, "==", nameCall, nameExpr);
    } else {
        result = new Constant(loc, true);
    }
}
    :   ( ID         { nameExpr = new Constant(sourceExtent($ID), $ID.text); }
        | s='*'      { nameExpr = null; loc= sourceExtent($s); }
        | v=variable { nameExpr = $v.result; }
        | STRING     { nameExpr = new Constant(sourceExtent($STRING), stringValue($STRING.getText())); }
        )
    ;

/**
 * A literal value: either a number or a string.
 */
literal returns [ ASTNode result ]
    :   NUMBER
        {
            double value = Double.valueOf($NUMBER.getText());
            result = new Constant(sourceExtent($NUMBER), value);
        }
    |   STRING
        {
            String value = stringValue($STRING.getText());
            result = new Constant(sourceExtent($STRING), value);
        }
    ;

/**
 * A procedure call.
 */
call returns [ ASTNode result ]
    :   ID '(' args=arguments close=')' { result = new Call(sourceExtent($ID, $close), $ID.text, $args.result); }
    |   dot='.'                   { result = new Call(sourceExtent($dot), "current", new ArrayList<ASTNode>()); }
    ;

/**
 * The arguments passed in a procedure call.
 */
arguments returns [ List<ASTNode> result ]
@init {
  result = new ArrayList<ASTNode>();
}
    :   (      e1=expression { result.add($e1.result); }
          (',' e2=expression { result.add($e2.result); } )*
        )?
    ;
